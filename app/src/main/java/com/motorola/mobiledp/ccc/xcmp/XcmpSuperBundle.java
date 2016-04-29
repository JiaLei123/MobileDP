/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * Super bundle implementation
 * @author WDGK73
 *
 */
public class XcmpSuperBundle extends CommandBase implements ISuperBundle {

	private Vector<ICommand> children;
	public static final int HeaderSize = 4;
	private boolean continueIfFail;
	private int nReplies;
	public static final short OPCODE = 0x002E;
	public XcmpSuperBundle()
	{
		children = new Vector<ICommand>();
		continueIfFail = true;
	}
	
	public XcmpSuperBundle(boolean continueIfFail)
	{
		children = new Vector<ICommand>();
		this.continueIfFail = continueIfFail;
	}

	public int childrenCount()
	{
		return children.size();
	}
	
	public int getRepliesCount()
	{
		return this.nReplies;
	}
	
	public ICommand get(int index)
	{
		return children.get(index);
	}
	
	public void addChild(ICommand cmd)
	{
		if (cmd == null)
		{
			throw new IllegalArgumentException("cmd can't be null");
		}
		children.add(cmd);
	}
	
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ISuperBundle#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int total = HeaderSize;
		for (ICommand cmd : children) {
			total += 2 + ((ISuperBundle)cmd).getSize();
		}
		return total;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.CommandBase#decode(byte[], int, int)
	 */
	@Override
	public void decode(byte[] data, int offset, int length) throws Exception {
		// TODO Auto-generated method stub
		if (data.length < offset + length)
		{
			throw new IllegalArgumentException("space of data not enough");
		}
		replyResult = XcmpUtility.CheckReplyHeader(data, offset, getOpcode());
		offset += XcmpUtility.ReplyHeaderSize;
		nReplies = data[offset];
		offset++;
		for(int i = 0; i < nReplies; i++)
		{
			int size = (data[offset] << 8) + data[offset + 1];
			offset += 2;
			((CommandBase)this.children.get(i)).decode(data, offset, size);
			offset += size;
		}
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.CommandBase#getOpcode()
	 */
	@Override
	public short getOpcode() {
		// TODO Auto-generated method stub
		return OPCODE;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.CommandBase#encode(byte[], int)
	 */
	@Override
	protected int encode(byte[] data, int offset)
			throws IllegalArgumentException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if (data.length < offset + HeaderSize)
		{
			throw new IllegalArgumentException("data length is not enough");
		}
		
		int total = HeaderSize;
		
		// encode opcode
		XcmpUtility.encode(data, offset, getOpcode());
		offset += 2;
		
		// encode number of children
		XcmpUtility.encode(data, offset, (byte)this.childrenCount());
		offset++;
		
		// encode continue if fail
		XcmpUtility.encode(data, offset, (byte)(this.continueIfFail ? 1 : 0));
		
		// encode data as |cmd payload length|cmd payload|
		for (ICommand cmd : children) {
			int length = ((CommandBase)cmd).encode(data, offset + 2);
			XcmpUtility.encode(data, offset, (short)length);
			
			offset += 2 + length;
			total += 2 + length;
		}
		
		return total;
	}

}
