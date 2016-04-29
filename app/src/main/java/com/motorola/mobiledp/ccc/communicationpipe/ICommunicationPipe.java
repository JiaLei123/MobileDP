/**
 * 
 */
package com.motorola.mobiledp.ccc.communicationpipe;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author WDGK73
 *
 */
public interface ICommunicationPipe {
	int getTimeout();
	void setTimeout(int milli);
	boolean isOpen();
	void close() throws IOException;
	void open(int milli) throws SocketException, IOException, Exception;
	void send(byte[] buffer, int start, int length) throws IOException, Exception;
	byte[] receive() throws SocketException, IOException;
	/**
	 * 
	 * @param timeout in milliseconds
	 * @return
	 * @throws SocketException 
	 * @throws IOException 
	 */
	byte[] receive(int timeout) throws SocketException, IOException;
	int receive(byte[] buffer, int start) throws SocketException, IOException;
	int receive(byte[] buffer, int start, int timeout) throws SocketException, IOException;
	byte[] request(byte[] send, int start, int length) throws SocketException, IOException;
	byte[] request(byte[] send, int start, int length, int timeout) throws SocketException, IOException, Exception;
	int getAvailable() throws IOException;
}
