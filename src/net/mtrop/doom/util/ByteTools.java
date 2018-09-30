package net.mtrop.doom.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
}
