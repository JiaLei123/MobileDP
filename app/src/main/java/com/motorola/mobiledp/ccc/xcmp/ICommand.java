/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import java.io.UnsupportedEncodingException;

/**
 * interface for XCMP command
 * @author WDGK73
 */
public interface ICommand {
	/**
	 * unpack XCMP command to byte array
	 * @param data byte array to be decoded
	 * @param offset start position of the byte array
	 * @param length length to be decoded
	 * @throws Exception 
	 */
	void decode(byte[] data, int offset, int length) throws Exception;
	
	/**
	 * pack the byte array
	 * @param data byte array to unpack
	 * @return length encoded
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	int encode(byte[] data) throws IllegalArgumentException, UnsupportedEncodingException;
	
	/**
	 * get the opcode
	 * @return opcode
	 */
	short getOpcode();
	
	/**
	 * indicate whether the command needs reply
	 * @return
	 */
	boolean hasReply();
	
	/**
	 * get the time out in milliseconds
	 * @return
	 */
	int getTimeout();
	
	/**
	 * set the time out in milliseconds
	 * @param ms
	 */
	void setTimeout(int ms);
}
