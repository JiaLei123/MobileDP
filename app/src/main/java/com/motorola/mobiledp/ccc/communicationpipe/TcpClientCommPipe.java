/**
 * 
 */
package com.motorola.mobiledp.ccc.communicationpipe;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * @author WDGK73
 * 
 */
public class TcpClientCommPipe extends TcpComPipe {

	private InetAddress tgtAddress;
	private int tgtPort;
	private Socket client;

	public TcpClientCommPipe(InetAddress addr, int port, int recvTimeout,
			boolean delay) {
		this.tgtAddress = addr;
		this.tgtPort = port;
		this.timeout = recvTimeout;
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

		this.client.close();
		this.opened = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#open(int)
	 */
	@Override
	public void open(int milli) throws SocketException, IOException {
		// TODO Auto-generated method stub
		this.client = new Socket();
		client.setTcpNoDelay(this.delay);

		if (milli <= 0) {
			this.timeout = 3 * CommPipeConstants.DefaultOpenTimeout;
		}

		SocketAddress address = new InetSocketAddress(this.tgtAddress,
				this.tgtPort);

		// retry 3 times to connect to device
		while (true) {
			try {
				this.client.connect(address,
						CommPipeConstants.DefaultOpenTimeout);
				this.is = this.client.getInputStream();
				this.os = this.client.getOutputStream();
				this.opened = true;
				break;
			} catch (IllegalArgumentException e) {
				throw e;
			} catch (IOException e) {
				// TODO: handle exception
				milli -= CommPipeConstants.DefaultOpenTimeout;
				if (milli <= 0) {
					throw e;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive
	 * (int)
	 */
	@Override
	public byte[] receive(int timeout) throws SocketException, IOException {
		// TODO Auto-generated method stub
		int backup = this.client.getSoTimeout();
		if (timeout != this.client.getSoTimeout()) {
			this.client.setSoTimeout(timeout);
		}

		byte[] data = null;
		int size = 0;
		if (extractHeader()) {
			size = readPacketSize();
		} else {
			size = this.getAvailable();
		}
		if (size > 0) {
			data = new byte[size];
			readFromStream(data, 0, size);
		}
		this.client.setSoTimeout(backup);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive
	 * (byte[], int, int)
	 */
	@Override
	public int receive(byte[] buffer, int start, int timeout)
			throws SocketException, IOException {
		// TODO Auto-generated method stub
		int backup = this.client.getSoTimeout();
		if (timeout != this.client.getSoTimeout()) {
			this.client.setSoTimeout(timeout);
		}

		int size = 0;
		if (extractHeader()) {
			size = readPacketSize();
		} else {
			size = this.getAvailable();
		}
		if (size > 0) {
			readFromStream(buffer, start, size);
		}
		this.client.setSoTimeout(backup);
		return size;
	}
}
