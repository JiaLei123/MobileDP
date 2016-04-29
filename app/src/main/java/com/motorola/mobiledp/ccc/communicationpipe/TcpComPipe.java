/**
 * 
 */
package com.motorola.mobiledp.ccc.communicationpipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * @author WDGK73
 * 
 */
public abstract class TcpComPipe implements ICommunicationPipe {

	private final static int MaxPacketSize = 1500;
	private int PacketHeaderSize = 2;
	private byte[] sendBuffer = new byte[MaxPacketSize];

	protected int timeout;
	protected boolean opened;
	protected boolean delay;
	protected InputStream is;
	protected OutputStream os;

	public boolean extractHeader() {
		return PacketHeaderSize == 2;
	}

	public void setExtractHeader(boolean need) {
		if (need) {
			PacketHeaderSize = 2;
		} else {
			PacketHeaderSize = 0;
		}
	}

	@Override
	public int getAvailable() throws IOException {
		// TODO Auto-generated method stub
		if (!this.isOpen()) {
			return 0;
		}
		return this.is.available();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#getTimeout
	 * ()
	 */
	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return timeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#setTimeout
	 * (int)
	 */
	@Override
	public void setTimeout(int milli) {
		// TODO Auto-generated method stub
		this.timeout = milli;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#isOpen()
	 */
	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return opened;
	}

	public abstract void close() throws IOException;

	public abstract void open(int milli) throws SocketException, IOException;

	public abstract byte[] receive(int timeout) throws SocketException, IOException;

	public abstract int receive(byte[] buffer, int start, int timeout) throws SocketException, IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#send(byte
	 * [], int, int)
	 */
	@Override
	public void send(byte[] buffer, int start, int length) throws IOException {
		// TODO Auto-generated method stub
		if (buffer.length < start + length)
		{
			throw new IllegalArgumentException("start + length > data length to be sent");
		}
		
		if (sendBuffer.length < length + PacketHeaderSize)
		{
			throw new IllegalArgumentException("PacketHeaderSize + data length to be sent > length of send buffer");
		}
		
		// mutual send
		synchronized (sendBuffer) {
			if (extractHeader())
			{
				sendBuffer[0] = (byte)(length >> 8);
				sendBuffer[1] = (byte)(length & 0xff);
			}
			System.arraycopy(buffer, start, sendBuffer, PacketHeaderSize, length);
			os.write(sendBuffer, 0, length + PacketHeaderSize);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive()
	 */
	@Override
	public byte[] receive() throws SocketException, IOException
	{
		return receive(this.timeout);
	}

	public int receive(byte[] buffer, int start) throws SocketException, IOException
	{
		return receive(buffer, start, this.timeout);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#request
	 * (byte[], int, int)
	 */
	@Override
	public byte[] request(byte[] send, int start, int length) throws SocketException, IOException {
		// TODO Auto-generated method stub
		return request(send, start, length, this.timeout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#request
	 * (byte[], int, int, int)
	 */
	@Override
	public byte[] request(byte[] send, int start, int length, int timeout) throws SocketException, IOException {
		// TODO Auto-generated method stub
		byte[] recved = null;
		
		this.send(send, start, length);
		
		recved = receive(timeout);
		
		return recved;
	}
	
	protected int readPacketSize() throws IOException
	{
		byte[] size = new byte[2];
		int cnt = readFromStream(size, 0, 2);
		if (cnt > 0)
		{
			return (size[0] << 8) + size[1];
		}
		return 0;
	}
	
	protected int readFromStream(byte[] data, int offset, int length) throws IOException
	{
		int done = 0;
		do {
			int cnt = this.is.read(data, offset, length - done);
			if (cnt > 0)
			{
				offset += cnt;
				done += cnt;
			}
		} while (offset < length);
		return done;
	}
}
