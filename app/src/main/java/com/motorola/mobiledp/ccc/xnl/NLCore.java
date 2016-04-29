/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

import java.util.concurrent.TimeoutException;

import com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe;

/**
 * @author WDGK73
 * 
 */
public class NLCore extends Thread {
	interface ICallback {
		void callback() throws Exception;
	}

	private boolean abort;
	private boolean stop;
	private RequestsPool reqPool;
	private byte tranIdBase;
	private byte tranIdCtr;
	private short tranID;
	private short masterAddr;
	private short tgtAddr;
	private short devAddr;
	private short logicalAddr;
	private short tempAddr;
	private boolean needAck;
	private byte xnlFlag;
	private byte[] encryptedKey;
	private int timeout;

	private ICommunicationPipe pipe;

	private Object sync;

	private static final int XnlRetries = 3;
	private static final int XnlOpenPortTimeout = 2000;
	private static final int ConnectWaitTimeout = 500;
	public int DefaultTimeout = 100;
	public int AdditionalAckTimeout = 400;
	private static final int DeviceInitStatusTimeout = 1000;
	private static final int XnlDisconnectTimeout = 5000;;

	public NLCore() {
		sync = new Object();
		pipe = null;
		masterAddr = 0;
		devAddr = 0;
		logicalAddr = 0;
		tgtAddr = 0;
		xnlFlag = 0;
		tempAddr = 0;
		tranID = 255;
		encryptedKey = null;
		reqPool = new RequestsPool();
		needAck = true;
		abort = false;
		stop = true;
	}

	public void connect(ICommunicationPipe commPipe) throws Exception {
		abort = false;
		stop = true;
		this.pipe = commPipe;
		if (!this.pipe.isOpen()) {
			this.pipe.open(XnlOpenPortTimeout);
		}
		// Connection steps:
		// 1. get the master;
		// 2. Authentication;
		// 3. conncetion;
		// 4. initialize
		try {
			receiveMasterStatusBroadcast();
		} catch (Exception e) {
			// retry getting the master
			multiTry(new ICallback() {

				@Override
				public void callback() throws Exception {
					// TODO Auto-generated method stub
					sendMasterStatusBroadcast();
				}
			}, new ICallback() {

				@Override
				public void callback() throws Exception {
					// TODO Auto-generated method stub
					receiveMasterStatusBroadcast();
				}
			}, XnlRetries);
		}

		multiTry(new ICallback() {

			@Override
			public void callback() throws Exception {
				// TODO Auto-generated method stub
				sendAuthenticationKeyRequest();
			}
		}, new ICallback() {

			@Override
			public void callback() throws Exception {
				// TODO Auto-generated method stub
				receiveAuthenticationKeyReply();
			}
		}, XnlRetries);

		multiTry(new ICallback() {

			@Override
			public void callback() throws Exception {
				// TODO Auto-generated method stub
				sendConnectionRequest();
			}
		}, new ICallback() {

			@Override
			public void callback() throws Exception {
				// TODO Auto-generated method stub
				receiveConnectionReply();
			}
		}, XnlRetries);

		multiTry(new ICallback() {

			@Override
			public void callback() throws Exception {
				// TODO Auto-generated method stub
				deviceInitialize();
			}
		}, XnlRetries);
	}

	public void disconnect(boolean aborted) throws InterruptedException {
		this.abort = aborted;
		this.stop = true;
		join(XnlDisconnectTimeout);
	}

	public void startAsyncReceive(int timeout) {
		while (this.isAlive()) {
			try {
				disconnect(this.abort);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.timeout = timeout;
		run();
	}

	@Override
	public void run() {
		try {
			while (!stop) {
				// xnl header size = 12
				if (this.pipe.getAvailable() >= XnlPacket.XnlHeaderSize) {

					try {
						XnlPacket packet = readFromDevice(timeout);
						if (packet != null) {
							dispatch(packet);
						}
						sleep(timeout);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		} catch (Exception e) {

		}
	}

	private boolean isDeviceInitStatusBroadcast(XnlPacket packet) {
		if (packet.getnPayload() >= 2) {
			int opcode = XnlUtility.ReadShortFromBytes(packet.getPayload(), 0);
			if (opcode == XnlPacket.DevinitstsOpcode) {
				return true;
			}
		}
		return false;
	}

	private void dispatch(XnlPacket packet) {
		switch (packet.getOpcode()) {
		case DataMessage:
			if (!isDeviceInitStatusBroadcast(packet)) {
				reqPool.responseReceived(packet);
			}
			break;
		case DataMessageAck:
			reqPool.requestAckReceived(packet);
			break;
		case MasterStatusBroadcast:
			this.masterAddr = packet.getSrcAddress();
			this.tgtAddr = this.masterAddr;
			break;
		/*
		 * case DeviceSysMapBroadcast: case DeviceResetMessage: break;
		 */
		default:
			break;
		}
	}

	private void sendMasterStatusBroadcast() throws Exception {
		XnlPacket packet = XnlPacket.createDeviceMasterQuery();
		sendToDevice(packet);
	}

	private void sendAuthenticationKeyRequest() throws Exception {
		XnlPacket packet = XnlPacket
				.createDeviceAuthKeyRequest(this.masterAddr);
		sendToDevice(packet);
	}

	private void receiveAuthenticationKeyReply() throws Exception {
		XnlPacket packet = new XnlPacket();

		while (packet == null
				|| packet.getOpcode() != XnlOpcodes.DeviceAuthKeyReply
				|| packet.getDstAddress() != this.devAddr
				|| packet.getSrcAddress() != this.masterAddr) {
			packet = readFromDevice(DefaultTimeout);
		}

		tempAddr = (short) (packet.getPayload()[0] << 8 + packet.getPayload()[1]);
		byte[] plain = new byte[8];
		System.arraycopy(packet.getPayload(), 2, plain, 0, 8);
		encryptedKey = XnlUtility.Authenticate(plain, (byte) 0);
	}

	private void sendConnectionRequest() throws Exception {
		XnlPacket packet = XnlPacket.createDeviceConnRequest(this.masterAddr,
				tempAddr, 0, XnlPacket.PCApplication, (byte) 0, encryptedKey);
		sendToDevice(packet);
	}

	private void receiveConnectionReply() throws Exception {
		XnlPacket packet = new XnlPacket();
		while (packet == null
				|| packet.getOpcode() != XnlOpcodes.DeviceConnectReply
				|| packet.getDstAddress() != this.tempAddr
				|| packet.getSrcAddress() != this.masterAddr) {
			packet = readFromDevice(DefaultTimeout);
		}
		ConnReplyResult result = XnlPacket.decodeDeviceConnReply(packet);
		this.tranIdBase = result.tranIDBase;
		this.devAddr = result.devAddr;
		this.logicalAddr = result.logicalAddr;
		needAck = (packet.getXnlFlag() & 0x08) == 0 ? true : false;
		this.encryptedKey = null;
	}

	private void receiveMasterStatusBroadcast() throws Exception {
		XnlPacket packet = new XnlPacket();
		while (packet == null
				|| packet.getXnlOpcode() != XnlOpcodes.MasterStatusBroadcast) {
			packet = readFromDevice(ConnectWaitTimeout);
		}
		masterAddr = packet.getSrcAddress();
		tgtAddr = masterAddr;
	}

	private void deviceInitialize() throws Exception {
		XnlPacket packet = new XnlPacket();
		int xcmpOpcode = 0;
		while (true) {
			while (packet == null
					|| packet.getOpcode() != XnlOpcodes.DataMessage
					|| packet.getXnlProtocol() != XnlProtocol.Xcmp
					|| xcmpOpcode != XnlPacket.DevinitstsOpcode) {
				packet = readFromDevice(DeviceInitStatusTimeout);
				if (packet != null && packet.getnPayload() >= 2) {
					xcmpOpcode = XnlUtility.ReadShortFromBytes(
							packet.getPayload(), 0);
				}
				InitStatusBroadcastResult result = XnlPacket
						.decodeDeviceInitStatusBroadcast(packet);

				// init status
				if (result.initType == 0x00) {
					updateXnlFlag();
					XnlPacket send = XnlPacket.createDeviceInitStatusBroadcast(
							masterAddr, devAddr, packet.getTransID());
					sendToDevice(send);
				}
				// init completed
				else if (result.initType == 0x01) {
					break;
				}
				xcmpOpcode = 0;
			}
		}

	}

	public short sendDataMsg(byte[] buf, int length) throws Exception {
		XnlPacket packet;
		updateTransactionID();
		updateXnlFlag();

		packet = new XnlPacket();
		packet.setnPayload(length);
		packet.setPayload(new byte[length]);
		System.arraycopy(buf, 0, packet.getPayload(), 0, length);
		packet.setXnlOpcode(XnlOpcodes.DataMessage);
		packet.setXnlProtocol(XnlProtocol.Xcmp);
		packet.setXnlFlag(xnlFlag);
		packet.setDstAddress(tgtAddr);
		packet.setSrcAddress(devAddr);
		packet.setTransID(tranID);
		reqPool.apend(packet);

		int i = XnlRetries;
		do {
			try {
				sendDataMsg(packet);
				break;
			} catch (Exception e) {
				// TODO: handle exception
				i--;
				if (i <= 0) {
					throw e;
				}
			}
		} while (true);

		return packet.getTransID();
	}

	private void sendDataMsg(XnlPacket packet) throws Exception {
		sendToDevice(packet);
		if (needAck
				&& null == reqPool.getRequestAck(packet.getTransID(),
						DefaultTimeout + AdditionalAckTimeout + 5)) {
			throw new TimeoutException("get ack time out");
		}
	}

	private void sendToDevice(XnlPacket packet) throws Exception {
		if (this.abort) {
			throw new Exception("nl core alread aborted");
		}
		byte[] buffer = packet.toBytes();
		// TODO send the buffer
		synchronized (this.sync) {
			this.pipe.send(buffer, 0, buffer.length);
		}
	}

	private XnlPacket readFromDevice(int timeout) throws Exception {
		if (this.abort) {
			throw new Exception("nl core alread aborted");
		}
		XnlPacket packet = null;
		synchronized (sync) {
			byte[] data = this.pipe.receive(timeout);
			if (data != null) {
				packet = new XnlPacket();
				packet.readFromBytes(data);

				if (packet.getOpcode() == XnlOpcodes.DataMessage && needAck) {
					XnlPacket ack = XnlPacket.createDataAck(packet);
					sendToDevice(ack);
				}
			}
		}
		return packet;
	}

	protected void updateTransactionID() {
		tranIdCtr++;
		if (tranIdCtr >= (byte) 0xff) {
			tranIdCtr = 0;
		}
		tranID = (short) (tranIdBase * 256 + tranIdCtr);
		if (tranID == 0) {
			tranIdCtr++;
		}
		tranID = (short) (tranIdBase * 256 + tranIdCtr);
	}

	protected void updateXnlFlag() {
		xnlFlag++;
		if (xnlFlag >= 7) {
			xnlFlag = 0;
		}
	}

	public void multiTry(ICallback cb1, ICallback cb2, int times)
			throws Exception {
		do {
			try {
				cb1.callback();
				cb2.callback();
				break;
			} catch (Exception e) {
				times--;
				if (times < 0) {
					throw e;
				}
			}
		} while (true);
	}

	public void multiTry(ICallback cb1, int times) throws Exception {
		do {
			try {
				cb1.callback();
				break;
			} catch (Exception e) {
				times--;
				if (times < 0) {
					throw e;
				}
			}
		} while (true);
	}

	public byte[] receive(short tranID, int timeout) throws Exception {
		int times = XnlRetries;
		XnlPacket response = null;
		while (true) {
			try {
				response = receiveDataMsgResponse(tranID, timeout);
				break;
			} catch (Exception e) {
				// TODO: handle exception
				times--;
				if (times == 0) {
					e.printStackTrace();
					throw e;
				}
			}
		}

		byte[] bytes = new byte[response.getnPayload()];
		System.arraycopy(response.getPayload(), 0, bytes, 0, bytes.length);
		return bytes;
	}

	private XnlPacket receiveDataMsgResponse(short tranID, int timeout)
			throws Exception {
		XnlPacket response = reqPool.getResponse(tranID, timeout);
		if (response == null) {
			throw new Exception("get response timeout");
		}
		return response;
	}
}
