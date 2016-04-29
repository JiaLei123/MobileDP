/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

import java.util.Vector;

/**
 * @author WDGK73
 * 
 */
public final class XnlUtility {

	public static final void appendShort2Bytes(Vector<Byte> bytes, short dWord) {
		bytes.add((byte) (dWord >> 8));
		bytes.add((byte) dWord);
	}

	public static final void appendDInt2Bytes(Vector<Byte> bytes, int dWord) {
		bytes.add((byte) (dWord >> 24));
		bytes.add((byte) (dWord >> 16));
		bytes.add((byte) (dWord >> 8));
		bytes.add((byte) dWord);
	}

	public static final short ReadShortFromBytes(byte[] bytes, int offset) {
		short s;
		s = (short) ((bytes[offset] << 8) + bytes[offset + 1]);
		return s;
	}

	public static final int ReadIntFromBytes(byte[] bytes, int offset) {
		int i;
		i = (bytes[offset] << 24) + (bytes[offset + 1] << 16)
				+ (bytes[offset + 2] << 8) + bytes[offset + 3];
		return i;
	}

	public static final byte[] Authenticate(byte[] plain, byte level) {
		byte[] encrypted = new byte[8];
		int[] key = new int[4];
		int delta = 0;
		switch (level) {
		case 0:
			key[0] = 0x5A96301D;
			key[1] = 0x0CF2AA55;
			key[2] = 0xBF936CC6;
			key[3] = 0xBD5ECD5B;
			delta = 0x790ab771;
			break;
		case 1:// external - New key for the Matrix R1.2.
			key[0] = 0x152C7E9D;
			key[1] = 0x38BE41C7;
			key[2] = 0x71E96CA4;
			key[3] = 0x6CAC1AFC;
			delta = 0x9e3779b9;
			break;

		case 2:// external privileged
			key[0] = 0xC3381C1D;
			key[1] = 0x1C8B6323;
			key[2] = 0x45B0FCA2;
			key[3] = 0xC4CA75D3;
			delta = 0x9e3779b9;
			break;
		}
		int y = ReadIntFromBytes(plain, 0), z = ReadIntFromBytes(plain, 4), sum = 0;
		int a = key[0], b = key[1], c = key[2], d = key[3], n = 32;

		while (n-- > 0) {
			sum += delta;
			y += (z << 4) + a ^ z + sum ^ (z >> 5) + b;
			z += (y << 4) + c ^ y + sum ^ (y >> 5) + d;
		}

		encrypted[0] = (byte)y;
		encrypted[1] = (byte)(y >> 8);
		encrypted[2] = (byte)(y >> 16);
		encrypted[3] = (byte)(y >> 24);
		
		encrypted[4] = (byte)z;
		encrypted[5] = (byte)(z >> 8);
		encrypted[6] = (byte)(z >> 16);
		encrypted[7] = (byte)(z >> 24);
		return encrypted;
	}
}
