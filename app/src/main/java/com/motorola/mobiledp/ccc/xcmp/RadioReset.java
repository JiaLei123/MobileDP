/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;

/**
 * reset radio
 * @author WDGK73
 *
 */
public class RadioReset extends CommandBase {

	public static final short OPCODE = 0x000D; 
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.xcmp.CommandBase#decode(byte[], int, int)
	 */
	@Override
	public void decode(byte[] data, int offset, int length) throws Exception {
		// TODO Auto-generated method stub
		this.replyResult = XcmpUtility.CheckReplyHeader(data, 0, getReplyOpcode());
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
		return XcmpUtility.encode(data, offset, getOpcode());
	}

}
