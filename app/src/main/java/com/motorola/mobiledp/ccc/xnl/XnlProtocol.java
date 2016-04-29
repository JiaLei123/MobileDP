/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

/**
 * @author WDGK73
 *
 */
public enum XnlProtocol {
	XnlControl((byte)0),
	Xcmp((byte)1);
	
	private byte value;
	private XnlProtocol(byte val)
	{
		this.value = val;
	}
	
	public static final XnlProtocol valueOf(byte val)
	{
		switch (val) {
		case 0:
			return XnlControl;
		case 1:
			return Xcmp;
		default:
			throw new IllegalArgumentException(String.format("{0} is not valid for Enum XnlProtocal", val));
		}
	}
	
	public byte getValue()
	{
		return this.value;
	}
}
