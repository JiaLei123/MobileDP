/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;

/**
 * @author WDGK73
 *
 */
public abstract class CommandBase implements ICommand {

	protected ReplyResult replyResult;
	private int timeout;
	
	public CommandBase()
	{
		this.replyResult = ReplyResult.Undefined;
	}
	
	public ReplyResult getReplyResult()
	{
		return this.replyResult;
	}
	
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#decode()
	 */
	public abstract void decode(byte[] data, int offset, int length) throws Exception;

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#encode(byte[])
	 */
	@Override
	public int encode(byte[] data) throws IllegalArgumentException, UnsupportedEncodingException
	{
		return encode(data, 0);
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#getOpcode()
	 */
	public abstract short getOpcode();
	
	public short getReplyOpcode()
	{
		return (short)(getOpcode() | XcmpUtility.ReplyOpcodeMask);
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#hasReply()
	 */
	@Override
	public boolean hasReply()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#getTimeout()
	 */
	@Override
	public int getTimeout()
	{
		return this.timeout;
	}
	
	/*(non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ICommand#setTimeout()
	 */
	public void setTimeout(int ms)
	{
		this.timeout = ms;
	}
	
	/**
	 * pack the object list to byte array
	 * @param data byte array to store packing result
	 * @param offset start position of byte array
	 * @return byte array length encoded
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	protected abstract int encode(byte[] data, int offset) throws IllegalArgumentException, UnsupportedEncodingException;

}
