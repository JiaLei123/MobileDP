/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

/**
 * @author wdgk73
 *
 */
public class Request {
	private XnlPacket req;
	private XnlPacket response;
	private XnlPacket ack;

	private Object waitResponseObj;
	private Object waitAckObject;


	public Request(XnlPacket packet)
	{
		req = packet;
		response = null;
		ack = null;
		waitAckObject = new Object();
		waitAckObject = new Object();
	}

	public XnlPacket getAck() {
		return ack;
	}

	public void setAck(XnlPacket ack) {
		this.ack = ack;
	}
	
	public XnlPacket getResponse() {
		return response;
	}

	public void setResponse(XnlPacket response) {
		this.response = response;
	}

	public XnlPacket getRequest() {
		return req;
	}
	
	public short getTransID()
	{
		return req.getTransID();
	}
	
	public void waitForResponse(int timeout) throws InterruptedException
	{
		this.waitResponseObj.wait(timeout);
	}
	
	public void waitForResponse() throws InterruptedException
	{
		this.waitResponseObj.wait();
	}
	
	public void waitForAck(int timeout) throws InterruptedException
	{
		this.waitAckObject.wait(timeout);
	}
	
	public void waitForAck() throws InterruptedException
	{
		this.waitAckObject.wait();
	}
	
	public void onResponseReceived()
	{
		this.waitResponseObj.notify();
	}
	
	public void onAckReceived()
	{
		this.waitAckObject.notify();
	}
}
