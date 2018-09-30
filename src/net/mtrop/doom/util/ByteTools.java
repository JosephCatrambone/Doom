package net.mtrop.doom.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ByteTools {
	public static byte[] toLittleEndian(short s) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort(s);
		return bb.array();
	}

	public static byte[] toLittleEndian(int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(i);
		return bb.array();
	}

	public static boolean bitIsSet(int flags, int bitmask) {
		return (flags & bitmask) != 0;
	}

	public static byte[] stringToByteArrayWithFixedSize(String s, int size) {
		ByteBuffer bb = ByteBuffer.allocate(size);
		for(int i=0; i < size; i++) {
			if(i < s.length()) {
				bb.putChar(s.charAt(i));
			} else {
				// The buffer should be null-initialzed and of a fixed size, but this guarantees capacity matches len.
				bb.putChar('\0');
			}
		}
		return bb.array();
	}

	public static ByteBuffer readInputStream(InputStream in) throws IOException {
		ArrayList<Byte> streamBuffer = new ArrayList<>(in.available());
		int bin = in.read();
		while(bin != -1) {
			streamBuffer.add((byte)(0xFF & bin));
			bin = in.read();
		}
		ByteBuffer bb = ByteBuffer.allocate(streamBuffer.size());
		for(byte b : streamBuffer) {
			bb.put(b);
		}
		return bb;
	}

	public static int booleansToInt(boolean... bit) {
		int number = 0;
		for(int i=0; i < bit.length; i++) {
			if(bit[i]) {
				number = number | (1 << i);
			}
		}
		return number;
	}
}
