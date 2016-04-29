package com.motorola.mobiledp.ccc.xnl;

import java.util.Vector;


/**
 * Xnl packet structure:
 * Opcode:					2 bytes
 * Protocol ID:			    1 byte	
 * xnl flags:				1 byte
 * destination address:	    2 bytes
 * source address:			2 bytes
 * transaction ID:			2 bytes
 * payload length:			2 bytes
 * payload:					(payload length) bytes
 * @author WDGK73
 *
 */
public class XnlPacket {
	private int nPayload;

	private byte[] payload;
	private XnlOpcodes xnlOpcode;
	private XnlProtocol xnlProtocol;
	private byte xnlFlag;
	private short dstAddress;
	private short srcAddress;
	private short transID;
	
	public static final int DevinitstsOpcode = 0xB400;
	private static final int DevinitstsOpcodeLength = 2;
	private static final int DevinitstsXcmpVersionLength = 4;
	private static final int DevinitstsInitTypeFieldLength = 1;
	private static final byte DeviceInitStatus = 0;
	public static final byte PCApplication = 0x0A;
	private static final byte UnknownDevice = 0;
	private static final int XnlDeviceInitStatusBroadcastPayloadSize = 7;
	
	private static final int XnlConnectReplyPayloadSize = 14;
	
	public static final int XnlHeaderSize = 12;
	public static final byte ConnectionRequestNeedAckXnlFlags = 0x08;
	
	public XnlPacket()
	{
		xnlOpcode = XnlOpcodes.Reserved;
	}
	
	public XnlOpcodes getOpcode()
	{
		return this.xnlOpcode;
	}
	
	public void setOpcode(short val)
	{
		this.xnlOpcode = XnlOpcodes.valueOf(val);
	}
	
	public void setOpcode(XnlOpcodes code)
	{
		this.xnlOpcode = code;
	}

	public int getnPayload() {
		return nPayload;
	}

	public void setnPayload(int nPayload) {
		this.nPayload = nPayload;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public XnlOpcodes getXnlOpcode() {
		return xnlOpcode;
	}

	public void setXnlOpcode(XnlOpcodes xnlOpcode) {
		this.xnlOpcode = xnlOpcode;
	}

	public XnlProtocol getXnlProtocol() {
		return xnlProtocol;
	}

	public void setXnlProtocol(XnlProtocol xnlProtocol) {
		this.xnlProtocol = xnlProtocol;
	}

	public byte getXnlFlag() {
		return xnlFlag;
	}

	public void setXnlFlag(byte xnlFlag) {
		this.xnlFlag = xnlFlag;
	}

	public short getDstAddress() {
		return dstAddress;
	}

	public void setDstAddress(short dstAddress) {
		this.dstAddress = dstAddress;
	}

	public short getSrcAddress() {
		return srcAddress;
	}

	public void setSrcAddress(short srcAddress) {
		this.srcAddress = srcAddress;
	}

	public void setTransID(short transID) {
		this.transID = transID;
	}
	public short getTransID()
	{
		return this.transID;
	}
	
	public byte[] toBytes()
	{
		Vector<Byte> buf = new Vector<Byte>();

		XnlUtility.appendShort2Bytes(buf, xnlOpcode.getValue());
		buf.add(xnlProtocol.getValue());
		buf.add(xnlFlag);
		XnlUtility.appendShort2Bytes(buf, dstAddress);
		XnlUtility.appendShort2Bytes(buf, srcAddress);
		XnlUtility.appendShort2Bytes(buf, transID);
		XnlUtility.appendShort2Bytes(buf, (short)nPayload);
		if (nPayload > 0 && payload != null)
		{
			for (byte b : payload) {
				buf.add(b);
			}
		}
		
		// convert to byte[]
		byte[] bytes = new byte[buf.size()];
		for (int i = 0; i < buf.size(); i++) {
			bytes[i] = buf.get(i);
		}
		return bytes;
	}
	
	public void readFromBytes(byte[] bytes)
	{
		if (bytes != null && bytes.length >= XnlHeaderSize)
		{
			readHeaderFromBytes(bytes);
			
			this.payload = new byte[this.nPayload];
			System.arraycopy(bytes, 12, this.payload, 0, this.nPayload);
		}
	}
	
	public void readHeaderFromBytes(byte[] bytes)
	{
		if (bytes != null && bytes.length >= XnlHeaderSize)
		{
			this.xnlOpcode = XnlOpcodes.valueOf(XnlUtility.ReadShortFromBytes(bytes, 0));
			this.xnlProtocol = XnlProtocol.valueOf(bytes[2]);
			this.xnlFlag = bytes[3];
			this.dstAddress = XnlUtility.ReadShortFromBytes(bytes, 4);
			this.srcAddress = XnlUtility.ReadShortFromBytes(bytes, 6);
			this.transID = XnlUtility.ReadShortFromBytes(bytes, 8);
			this.nPayload = XnlUtility.ReadShortFromBytes(bytes, 10);
		}
	}
	
	public void readBodyFromBytes(byte[] bytes)
	{
		if (bytes.length == this.nPayload)
		{
			this.payload = bytes;
		}
		else {
			throw new IllegalArgumentException("bytes length mismatch");
		}
	}
	
	public static final XnlPacket createDeviceMasterQuery()
	{
		XnlPacket packet = new XnlPacket();
		packet.xnlOpcode = XnlOpcodes.DeviceMasterQuery;
		packet.xnlProtocol = XnlProtocol.XnlControl;
		packet.xnlFlag = 0;
		packet.dstAddress = 0;
		packet.srcAddress = 0;
		packet.transID = 0;
		packet.nPayload = 0;
		
		return packet;
	}
	
	public static final XnlPacket createDeviceAuthKeyRequest(short masterAddress)
	{
		XnlPacket packet = new XnlPacket();
		packet.xnlOpcode = XnlOpcodes.DeviceAuthKeyRequest;
		packet.xnlProtocol = XnlProtocol.XnlControl;
		packet.xnlFlag = 0;
		packet.dstAddress = masterAddress;
		packet.srcAddress = 0;
		packet.transID = 0;
		packet.nPayload = 0;
		
		return packet;
	}
	
	public static final XnlPacket createDeviceConnRequest(short masterAddress, short tempSourceaAddress, int preferredXnlAddress, byte deviceType, byte authenticationLevel, byte[] encryptedAuthenticationValue)
	{
		XnlPacket packet = new XnlPacket();
		packet.xnlOpcode = XnlOpcodes.DeviceConnectRequest;
		packet.xnlProtocol = XnlProtocol.XnlControl;
		packet.xnlFlag = ConnectionRequestNeedAckXnlFlags;
		packet.dstAddress = masterAddress;
		packet.srcAddress = tempSourceaAddress;
		packet.transID = 0;
		packet.nPayload = 0x0C;
		packet.payload = new byte[packet.nPayload];
		packet.payload[0] = (byte)(preferredXnlAddress / 256);
		packet.payload[1] = (byte)(preferredXnlAddress % 256);
		packet.payload[2] = deviceType;
		packet.payload[3] = authenticationLevel;

		System.arraycopy(encryptedAuthenticationValue, 0, packet.payload, 4, encryptedAuthenticationValue.length);

		return packet;
	}
	
	public static ConnReplyResult decodeDeviceConnReply(XnlPacket packet)
	{
		if (packet == null || packet.nPayload < XnlConnectReplyPayloadSize)
		{
			throw new IllegalArgumentException("packet is corrupted");
		}
		
		ConnReplyResult result = new ConnReplyResult();
		result.code = packet.payload[0];
		result.tranIDBase = packet.payload[1];
		result.devAddr = XnlUtility.ReadShortFromBytes(packet.payload, 2);
		result.logicalAddr = XnlUtility.ReadShortFromBytes(packet.payload, 4);
		result.encryptedAuth = new byte[8];
		System.arraycopy(packet.payload, 6, result.encryptedAuth, 0, result.encryptedAuth.length);
		
		return result;
	}
	
	public static XnlPacket createDataAck(XnlPacket recved)
	{
		XnlPacket ack = new XnlPacket();
		ack.xnlOpcode = XnlOpcodes.DataMessageAck;
		ack.xnlFlag = recved.xnlFlag;
		ack.srcAddress = recved.dstAddress;
		ack.dstAddress = recved.srcAddress;
		ack.xnlProtocol = recved.xnlProtocol;
		ack.transID = recved.transID;
		ack.nPayload = 0;
		return ack;
		
	}
	
	public static XnlPacket createDeviceInitStatusBroadcast(short masterAddr, short srcAddr, short tranID)
	{
		XnlPacket packet = new XnlPacket();
		packet.xnlOpcode = XnlOpcodes.DataMessage;
		packet.xnlProtocol = XnlProtocol.Xcmp;
		packet.xnlFlag = 0;
		packet.dstAddress = masterAddr;
		packet.srcAddress = srcAddr;
		packet.transID = tranID;
		packet.nPayload = 11;
		packet.payload = new byte[packet.nPayload];
		// opcode
		packet.payload[0] = (byte)(DevinitstsOpcode >> 8);
		packet.payload[1] = (byte)DevinitstsOpcode;
		// XCMP version
		packet.payload[2] = 0x00;
		packet.payload[3] = 0x00;
		packet.payload[4] = 0x00;
		packet.payload[5] = 0x00;
		// Init Type
		packet.payload[6] = DeviceInitStatus;
		// Device Type
		packet.payload[7] = PCApplication;
		// Device Status
		packet.payload[8] = 0x00;
		packet.payload[9] = 0x00;
		// Device Information
		packet.payload[10] = 0x00;
		return packet;
	}
	
	public static InitStatusBroadcastResult decodeDeviceInitStatusBroadcast(XnlPacket packet)
	{
		if (packet == null || packet.nPayload < XnlDeviceInitStatusBroadcastPayloadSize)
		{
			throw new IllegalArgumentException("packet corrupted");
		}
		InitStatusBroadcastResult result = new InitStatusBroadcastResult();
		result.deviceType = UnknownDevice;
		result.initType = packet.payload[6];
		
		if (packet.nPayload > DevinitstsOpcodeLength + DevinitstsXcmpVersionLength + DevinitstsInitTypeFieldLength)
		{
			result.deviceType = packet.payload[7];
		}
		
		return result;
	}
}
