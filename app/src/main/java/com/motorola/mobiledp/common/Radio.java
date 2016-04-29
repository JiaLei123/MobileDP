package com.motorola.mobiledp.common;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import com.motorola.mobiledp.ccc.communicationpipe.CommPipeConstants;
import com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe;
import com.motorola.mobiledp.ccc.communicationpipe.NLCommPipe;
import com.motorola.mobiledp.ccc.communicationpipe.TcpClientCommPipe;
import com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager;
import com.motorola.mobiledp.ccc.sequencemanager.PcrSequenceManager;

public class Radio {
	public final static int xnlPort = 8002;
	ISequenceManager manager;
	InetAddress address;

	public Radio(InetAddress addr) {
		// TODO Auto-generated constructor stub
		this.address = addr;
	}

	void openSession() throws IOException, Exception {
		ICommunicationPipe cp = null;
		try {
			TcpClientCommPipe pipe = new TcpClientCommPipe(this.address,
					xnlPort, CommPipeConstants.DefaultReceiveTimeout, true);
			pipe.open(-1);
			cp = new NLCommPipe(pipe, 100, 400);
			manager = new PcrSequenceManager(cp);
			manager.openSession(generateSessionID());
		} catch (Exception e) {
			cp.close();
			throw e;
		}
	}

	void closeSession() {

	}

	private short generateSessionID() {
		Random random = new Random();
		int result = 0;
		do {
			random.nextInt(0xffff);
		} while (result == 0);
		return (short) result;
	}
}
