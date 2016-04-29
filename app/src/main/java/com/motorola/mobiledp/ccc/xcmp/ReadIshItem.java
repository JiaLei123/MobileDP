/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;

/**
 * @author WDGK73
 *
 */
public class ReadIshItem extends CommandBase implements ISuperBundle {

	public static final short OPCODE = 0x0100;
	private byte partition;
	private short type;
	private short id;
	private short nBytes;
	private short offset;
	private byte[] ishData;
	private short itemSize;

	public ReadIshItem()
	{
		this.ishData = new byte[0];
	}
	
	public ReadIshItem(byte partition, short type, short id, short nBytes, short offset)
	{
		this.partition = partition;
		this.type = type;
		this.id = id;
		this.nBytes = nBytes;
		this.offset = offset;
	}

	public byte[] getIshData() {
		return ishData;
	}
	
	public int getItemSize()
	{
		return (int)this.itemSize;
	}
	
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.ISuperBundle#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 14 + (int)nBytes;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.CommandBase#decode(byte[], int, int)
	 */
	@Override
	public void decode(byte[] data, int offset, int length) throws Exception {
		// TODO Auto-generated method stub
		replyResult = XcmpUtility.CheckReplyHeader(data, offset, getReplyOpcode());
		
		if (length > XcmpUtility.ReplyHeaderSize)
		{
			ParameterWrapper<Byte> par = new ParameterWrapper<Byte>(this.partition);
			ParameterWrapper<Short> t = new ParameterWrapper<Short>(this.type);
			ParameterWrapper<Short> i = new ParameterWrapper<Short>(this.id);
			ParameterWrapper<Short> n = new ParameterWrapper<Short>(this.nBytes);
			ParameterWrapper<Short> o = new ParameterWrapper<Short>(this.offset);
			ParameterWrapper<Short> p = new ParameterWrapper<Short>(this.itemSize);
			ParameterWrapper<byte[]> ish = new ParameterWrapper<byte[]>(this.ishData);
			
			XcmpUtility.decode(data, XcmpUtility.ReplyHeaderSize, length - XcmpUtility.ReplyHeaderSize, par, t, i, n, o, p, ish);
			
			this.partition = par.getValue();
			this.type = t.getValue();
			this.id = i.getValue();
			this.nBytes = n.getValue();
			this.offset = o.getValue();
			this.itemSize = p.getValue();
			this.ishData = ish.getValue();
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
		return XcmpUtility.encode(data, offset, getOpcode(), this.partition, this.type, this.id, this.nBytes, this.offset);
	}

}
