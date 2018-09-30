/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.ByteTools;
import net.mtrop.doom.util.RangeUtils;

/**
 * Doom/Boom 14-byte format implementation of Linedef.
 * @author Matthew Tropiano
 */
public class DoomLinedef extends CommonLinedef implements BinaryObject
{
	/** Byte length of this object. */
	public static final int LENGTH = 14;

	/** Flag: (Boom) Line passes activation through to other lines. */
	protected boolean passThru;
	/** Linedef special tag. */
	protected int tag;

	/**
	 * Creates a new linedef.
	 */
	public DoomLinedef()
	{
	}

	/**
	 * Reads and creates a new DoomLinedef from an array of bytes.
	 * This reads from the first 14 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new DoomLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef create(byte[] bytes) throws IOException
	{
		DoomLinedef out = new DoomLinedef();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomLinedef from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomLinedef} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef read(InputStream in) throws IOException
	{
		DoomLinedef out = new DoomLinedef();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new DoomLinedefs from an array of bytes.
	 * This reads from the first 14 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of DoomLinedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new DoomLinedefs from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link DoomLinedef}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of DoomLinedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomLinedef[] read(InputStream in, int count) throws IOException
	{
		DoomLinedef[] out = new DoomLinedef[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new DoomLinedef();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/**
	 * Sets this linedef's special tag.
	 * @param tag the new tag.
	 */
	public void setTag(int tag)
	{
		RangeUtils.checkShortUnsigned("Tag", tag);
		this.tag = tag;
	}

	/**
	 * @return this linedef's special tag.
	 */
	public int getTag()
	{
		return tag;
	}

	/**
	 * Sets if this line's activated special does not block the activation search.
	 * @param passThru true to set, false to clear.
	 */
	public void setPassThru(boolean passThru)
	{
		this.passThru = passThru;
	}

	/**
	 * @return true if this line's activated special does not block the activation search, false if so.
	 */
	public boolean isPassThru()
	{
		return passThru;
	}

	@Override
	public byte[] toBytes()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try { writeBytes(bos); } catch (IOException e) { /* Shouldn't happen. */ }
		return bos.toByteArray();
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		readBytes(bin);
		bin.close();
	}

	@Override
	public void readBytes(InputStream in) throws IOException
	{
		ByteBuffer bb = ByteTools.readInputStream(in);
		
		vertexStartIndex = (short)(bb.getShort() & 0xFFFF); // Unsigned short
		vertexEndIndex = (short)(bb.getShort() & 0xFFFF);
		
		// bitflags
		int flags = (short)(bb.getShort() & 0xFFFF);
		impassable = ByteTools.bitIsSet(flags, (1 << 0));
		monsterBlocking = ByteTools.bitIsSet(flags, (1 << 1));
		twoSided = ByteTools.bitIsSet(flags, (1 << 2));
		upperUnpegged = ByteTools.bitIsSet(flags, (1 << 3));
		lowerUnpegged = ByteTools.bitIsSet(flags, (1 << 4));
		secret = ByteTools.bitIsSet(flags, (1 << 5));
		soundBlocking = ByteTools.bitIsSet(flags, (1 << 6));
		notDrawn = ByteTools.bitIsSet(flags, (1 << 7));
		mapped = ByteTools.bitIsSet(flags, (1 << 8));
		passThru = ByteTools.bitIsSet(flags, (1 << 9));
		
		special = (short)(bb.getShort() & 0xFFFF);
		tag = (short)(bb.getShort() & 0xFFFF);
		sidedefFrontIndex = bb.getShort();
		sidedefBackIndex = bb.getShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException 
	{
		ByteBuffer bb = ByteBuffer.allocate(2+2+4+2+2+2+2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort((short)vertexStartIndex); // TODO: Unsigned.
		bb.putShort((short)vertexEndIndex); // TODO: Unsigned.

		bb.putShort((short)ByteTools.booleansToInt(
			impassable,
			monsterBlocking,
			twoSided,
			upperUnpegged,
			lowerUnpegged,
			secret,
			soundBlocking,
			notDrawn,
			mapped,
			passThru
		)); // TODO: Unsigned.

		bb.putShort((short)special); // TODO: Unsigned.
		bb.putShort((short)tag); // TODO: Unsigned.
		bb.putShort((short)sidedefFrontIndex);
		bb.putShort((short)sidedefBackIndex);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Linedef");
		sb.append(' ').append(vertexStartIndex).append(" to ").append(vertexEndIndex);
		sb.append(' ').append("Sidedef ");
		sb.append(' ').append("Front ").append(sidedefFrontIndex);
		sb.append(' ').append("Back ").append(sidedefBackIndex);
		
		if (impassable) sb.append(' ').append("IMPASSABLE");
		if (monsterBlocking) sb.append(' ').append("MONSTERBLOCK");
		if (twoSided) sb.append(' ').append("TWOSIDED");
		if (upperUnpegged) sb.append(' ').append("UPPERUNPEGGED");
		if (lowerUnpegged) sb.append(' ').append("LOWERUNPEGGED");
		if (secret) sb.append(' ').append("SECRET");
		if (soundBlocking) sb.append(' ').append("SOUNDBLOCKING");
		if (notDrawn) sb.append(' ').append("NOTDRAWN");
		if (mapped) sb.append(' ').append("MAPPED");
		if (passThru) sb.append(' ').append("PASSTHRU");
		
		sb.append(' ').append("Special ").append(special);
		sb.append(' ').append("Tag ").append(tag);
		return sb.toString();
	}
	
}
