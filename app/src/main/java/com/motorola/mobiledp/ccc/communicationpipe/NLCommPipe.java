/**
 * 
 */
package com.motorola.mobiledp.ccc.communicationpipe;

import java.io.IOException;
import java.net.SocketException;

import com.motorola.mobiledp.ccc.xnl.Xnl;

/**
 * @author WDGK73
 *
 */
public class NLCommPipe implements ICommunicationPipe {

	private ICommunicationPipe tcpPipe;
	private int defaultTimeout;
	private int additionalACKTimeout;
	private boolean connected;
	private short tranID;
	
	private byte[] recvBuffer;
	private Xnl xnl;
	
	public NLCommPipe(ICommunicationPipe lower, int defaultTimeout, int ackTimeout)
	{
		if (lower == null)
		{
			throw new IllegalArgumentException("lower communication pipe can't be null");
		}
		xnl = new Xnl();
		this.defaultTimeout = defaultTimeout;
		this.additionalACKTimeout = ackTimeout;
	}
	
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#getTimeout()
	 */
	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return tcpPipe.getTimeout();
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#setTimeout(int)
	 */
	@Override
	public void setTimeout(int milli) {
		// TODO Auto-generated method stub
		tcpPipe.setTimeout(milli);
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#isOpen()
	 */
	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return this.connected;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#open(int)
	 */
	@Override
	public void open(int milli) throws Exception {
		// TODO Auto-generated method stub
		int retries = 3;
		xnl.setTimer(this.defaultTimeout, this.additionalACKTimeout);
		while (!connected)
		{		
			try {
				xnl.connect(tcpPipe);
				connected = true;
			} catch (Exception e) {
				// TODO: handle exception
				try {
					xnl.disconnect();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				retries--;
				if (retries <= 0)
				{
					throw e;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#send(byte[], int, int)
	 */
	@Override
	public void send(byte[] buffer, int start, int length) throws Exception {
		// TODO Auto-generated method stub
		if (buffer == null)
		{
			throw new IllegalArgumentException("data to send can't be null");
		}
		if (length <= 0 || start + length > buffer.length)
		{
			throw new IllegalArgumentException("length of data to send is wrong");
		}
		
		int retries = 3;
		tranID = 0;
		while (true)
		{
			try {
				synchronized (buffer) {
					tranID = xnl.send(buffer, length);
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				retries--;
				if (retries <= 0)
				{
					throw e;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive()
	 */
	@Override
	public byte[] receive() throws SocketException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive(int)
	 */
	@Override
	public byte[] receive(int timeout) throws SocketException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive(byte[], int)
	 */
	@Override
	public int receive(byte[] buffer, int start) throws SocketException,
			IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#receive(byte[], int, int)
	 */
	@Override
	public int receive(byte[] buffer, int start, int timeout)
			throws SocketException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#request(byte[], int, int)
	 */
	@Override
	public byte[] request(byte[] send, int start, int length)
			throws SocketException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#request(byte[], int, int, int)
	 */
	@Override
	public byte[] request(byte[] send, int start, int length, int timeout)
			throws Exception {
		// TODO Auto-generated method stub
		if (send == null)
		{
			throw new IllegalArgumentException("data to send can't be null");
		}
		if (length <= 0 || start + length > send.length)
		{
			throw new IllegalArgumentException("length of data to send is wrong");
		}
		int retries = 3;
		tranID = 0;
		while (true)
		{
			try {
				synchronized (send) {
					tranID = xnl.send(send, length);
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				retries--;
				if (retries <= 0)
				{
					throw e;
				}
			}
		}
		return xnl.receive(tranID, timeout);
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe#getAvailable()
	 */
	@Override
	public int getAvailable() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
