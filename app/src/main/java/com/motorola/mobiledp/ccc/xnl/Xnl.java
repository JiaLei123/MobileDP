package com.motorola.mobiledp.ccc.xnl;

import com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe;

public class Xnl {
	private NLCore core;
	private boolean connected;
	private Object sync;
	
	private final int BroadcastPollPeriod = 5;
	
	public Xnl()
	{
		core = null;
		connected = false;
		sync = new Object();
	}
	
	public void connect(ICommunicationPipe pipe) throws Exception
	{
		synchronized (sync)
		{
			if (pipe == null)
			{
				throw new IllegalArgumentException("pipe is set to null");
			}
			
			if (connected)
			{
				throw new Exception("xnl is already connected");
			}
			
			core = new NLCore();
			core.connect(pipe);
			
			core.startAsyncReceive(BroadcastPollPeriod);
			connected = true;
		}
	}
	
	public void disconnect() throws InterruptedException
	{
		synchronized (sync)
		{
			boolean abort = !connected;
			if (core != null)
			{
				core.disconnect(abort);
				connected = false;
			}
		}
	}
	
	public short send(byte[] buffer, int length) throws Exception
	{
		if (buffer == null || length == 0)
		{
			throw new IllegalArgumentException("buffer can't be null and the length must be equal or greater than 0");
		}
		
		short tranID = 0;
		
		synchronized (sync) {
			if (!connected)
			{
				throw new Exception("xnl is not connected!");
			}
			
			tranID = core.sendDataMsg(buffer, length);
		}
		
		return tranID;
	}
	
	public byte[] receive(short tranID, int timeout) throws Exception
	{
		synchronized (sync) {
			if (!connected)
			{
				throw new Exception("xnl is not connected!");
			}
			
			return core.receive(tranID, timeout);
		}
	}
	
	public void setTimer(int defaultTimout, int additionalAckTimeout)
	{
		core.AdditionalAckTimeout = additionalAckTimeout;
		core.DefaultTimeout = defaultTimout;
	}
	
}
