/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

import android.util.SparseArray;

/**
 * @author WDGK73
 *
 */
public class RequestsPool {
	private SparseArray<Request> requests;
	
	public RequestsPool()
	{
		requests = new SparseArray<Request>();
	}
	
	public void apend(XnlPacket packet)
	{
		synchronized (requests) {
			if (null == requests.get(packet.getTransID()))
			{
				requests.append(packet.getTransID(), new Request(packet));
			}
		}
	}
	
	public void remove(int id)
	{
		synchronized (requests) {
			requests.remove(id);
		}
	}
	
	public void responseReceived(XnlPacket response)
	{
		synchronized (requests) {
			Request req = requests.get(response.getTransID());
			if (req != null)
			{
				if (req.getResponse() == null)
				{
					req.setResponse(response);
					req.onResponseReceived();
				}
			}
		}
	}
	
	public void requestAckReceived(XnlPacket ack)
	{
		synchronized (requests) {
			Request req = requests.get(ack.getTransID());
			if (req != null)
			{
				if (req.getAck() == null 
						&& req.getRequest().getXnlProtocol() == XnlProtocol.Xcmp 
						&& req.getRequest().getSrcAddress() == ack.getDstAddress())
				{
					req.setAck(ack);
					req.onAckReceived();
				}
			}
		}
	}
	
	public XnlPacket getResponse(short tranID, int timeout) throws Exception {
		Request req = null;
		synchronized (requests) {
			req = requests.get(tranID);
		}
		if (req == null)
		{
			throw new Exception("the request doesn't exist in requests pool");
		}
		
		req.waitForResponse(timeout);
		if (req.getResponse() != null)
		{
			synchronized (requests) {
				requests.remove(tranID);
			}
			return req.getResponse();
		}
		return null;
	}
	
	public XnlPacket getRequestAck(short tranID, int timeout) throws Exception
	{
		Request req = null;
		synchronized (requests) {
			req = requests.get(tranID);
		}
		if (req == null)
		{
			throw new Exception("the request doesn't exist in requests pool");
		}
		
		req.waitForAck(timeout);
		return req.getAck();
	}
}
