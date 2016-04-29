/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;

/**
 * utility for encoding or decoding XCMP command
 * @author WDGK73
 *
 */
public final class XcmpUtility {
	
	public static final int ReplyHeaderSize = 3;
	public static final int ReplyOpcodeMask = 0x8000;
	
	/**
	 * /**
	 * encode the object list to byte array
	 * @param data byte array to store the result
	 * @param offset start position of data
	 * @param args object list to be encoded
	 * @return encoded length
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public static final int encode(byte[] data, int offset, Object ...args) 
			throws IllegalArgumentException, UnsupportedEncodingException
	{
		if (data == null)
		{
			throw new IllegalArgumentException("data can't be null");
		}
		if (args == null)
		{
			throw new IllegalArgumentException("args can't be null");
		}
		for(Object arg:args)
		{
			if (arg instanceof Byte)
			{
				data[offset++] = ((Byte)arg).byteValue();
			}
			else if (arg instanceof Short)
			{
				short s = ((Short)arg).shortValue();
				data[offset++] = (byte)(s >> 8);
				data[offset++] = (byte)s;
			}
			else if (arg instanceof Integer)
			{
				int i = ((Integer)arg).intValue();
				data[offset++] = (byte)(i >> 24);
				data[offset++] = (byte)(i >> 16);
				data[offset++] = (byte)(i >> 8);
				data[offset++] = (byte)i;		
			}
			else if (arg instanceof String)
			{
				// use ascii encoding
				byte[] ba = ((String)arg).getBytes("US-ASCII");
				System.arraycopy(ba, 0, data, offset, ba.length);
				offset += ba.length;
			}
			else if (arg instanceof char[])
			{
				// use ascii encoding
				char[] ca = (char[])arg;
				for(char ch:ca)
				{
					data[offset++] = (byte)(ch >> 8);
					//data[index++] = (byte)ch;
				}
			}
			else if (arg instanceof byte[])
			{
				byte[] ba = ((byte[])arg);
				System.arraycopy(ba, 0, data, offset, ba.length);
				offset += ba.length;
			}
			else
			{
				throw new IllegalArgumentException("data type not supported");
			}
		}
		return offset;
	}
	
	/**
	 * decode byte array to object list
	 * @param data
	 * @param offset
	 * @param length
	 * @param args
	 * @throws IllegalArgumentException
	 */
	public static final void decode(byte[] data, int offset, int length, ParameterWrapper<?> ...args)
			throws IllegalArgumentException
	{
		if (data == null)
		{
			throw new IllegalArgumentException("data can't be null");
		}
		if (args == null)
		{
			throw new IllegalArgumentException("args can't be null");
		}
		length = length + offset -1;
		for(ParameterWrapper<?> arg:args)
		{
			if (arg.getValue() == null)
			{
				throw new IllegalArgumentException("ParameterWrapper contains null reference value");
			}
			
			if (arg.getValue() instanceof Byte)
			{
				((ParameterWrapper<Byte>)arg).setValue(data[offset++]);
			}
			else if (arg.getValue() instanceof Short)
			{
				short s;
				s = (short)((data[offset] << 8) + data[offset + 1]);
				offset += 2;
				((ParameterWrapper<Short>)arg).setValue(s);
			}
			else if (arg.getValue() instanceof Integer)
			{
				int i;
				i = ((data[offset] << 24) 
						+ (data[offset + 1] << 16) 
						+ (data[offset + 2] << 8) 
						+ data[offset + 3]);
				offset += 4;
				((ParameterWrapper<Integer>)arg).setValue(i);
			}
			else if (arg.getValue() instanceof String)
			{
				// use ascii encoding
				StringBuilder sb = new StringBuilder();
				byte b;
				do {
					b = data[offset++];
					sb.append((char)b);
				} while (b > 0);
				((ParameterWrapper<String>)arg).setValue(sb.toString());
			}
			else if (arg.getValue() instanceof byte[])
			{
				byte[] ba = ((ParameterWrapper<byte[]>)arg).getValue();
				// ba.length == 0 means read all bytes till the end
				if (ba.length == 0)
				{
					ba = new byte[length - offset + 1];
				}
				System.arraycopy(data, offset, ba, 0, ba.length);
				((ParameterWrapper<byte[]>)arg).setValue(ba);
				offset += ba.length;
			}
			else
			{
				throw new IllegalArgumentException("data type not supported");
			}
			if (offset > length)
			{
				throw new IndexOutOfBoundsException("exceed the legnth specified");
			}
		}
	}
	
	public static final ReplyResult CheckReplyHeader(byte[] buffer, int offset, short expectedReplyOpcode) throws Exception
	{
		if (buffer == null)
        {
            throw new IllegalArgumentException("buffer is null");
        }
		if (buffer.length - offset < ReplyHeaderSize)
		{
			throw new IllegalArgumentException("reply header size invalid");
		}
		short replyOpcode = (short)((buffer[offset] << 8) + buffer[offset + 1]);
		if (replyOpcode != expectedReplyOpcode)
		{
			throw new Exception("reply opcode mismatch");
		}
		return ReplyResult.valueOf(buffer[offset + 2]);
	}
}
