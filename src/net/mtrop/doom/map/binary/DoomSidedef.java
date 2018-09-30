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
import net.mtrop.doom.util.NameUtils;
import net.mtrop.doom.util.RangeUtils;

/**
 * Doom/Boom 30-byte format implementation of a Sidedef.
 * @author Matthew Tropiano
 */
public class DoomSidedef implements BinaryObject 
{
	/** Byte length of this object. */
	public static final int LENGTH = 30;

	/** Sidedef X Offset. */
	private int offsetX;
	/** Sidedef Y Offset. */
	private int offsetY;
	/** Sidedef top texture. */
	private String textureTop;
	/** Sidedef bottom texture. */
	private String textureBottom;
	/** Sidedef middle texture. */
	private String textureMiddle;
	/** Sidedef's sector reference. */
	private int sectorIndex;
	
	/**
	 * Creates a new sidedef.
	 */
	public DoomSidedef()
	{
		textureTop = TEXTURE_BLANK;
		textureBottom = TEXTURE_BLANK;
		textureMiddle = TEXTURE_BLANK;
		sectorIndex = NULL_REFERENCE;
	}
	
	/**
	 * Reads and creates a new DoomSidedef from an array of bytes.
	 * This reads from the first 30 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new DoomSidedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSidedef create(byte[] bytes) throws IOException
	{
		DoomSidedef out = new DoomSidedef();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomSidedef from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomSidedef} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomSidedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSidedef read(InputStream in) throws IOException
	{
		DoomSidedef out = new DoomSidedef();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new DoomSidedefs from an array of bytes.
	 * This reads from the first 30 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of DoomSidedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSidedef[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new DoomSidedefs from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link DoomSidedef}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of DoomSidedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSidedef[] read(InputStream in, int count) throws IOException
	{
		DoomSidedef[] out = new DoomSidedef[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new DoomSidedef();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/**
	 * Sets the sidedef's texture X offset.
	 * @param offsetX the new X offset.
	 * @throws IllegalArgumentException if the offset is outside the range -32768 to 32767.
	 */
	public void setOffsetX(int offsetX)
	{
		RangeUtils.checkShort("X-offset", offsetX);
		this.offsetX = offsetX;
	}
	
	/**
	 * @return the sidedef's texture X offset.
	 */
	public int getOffsetX()
	{
		return offsetX;
	}

	/**
	 * Sets the sidedef's texture Y offset.
	 * @param offsetY the new Y offset.
	 * @throws IllegalArgumentException if the offset is outside the range -32768 to 32767.
	 */
	public void setOffsetY(int offsetY)
	{
		RangeUtils.checkShort("Y-offset", offsetY);
		this.offsetY = offsetY;
	}
	
	/**
	 * @return the sidedef's texture Y offset.
	 */
	public int getOffsetY()
	{
		return offsetY;
	}

	/**
	 * Sets the top texture name.
	 * @param textureTop the new texture name.
	 * @throws IllegalArgumentException if the texture name is invalid. 
	 */
	public void setTextureTop(String textureTop)
	{
		if (!NameUtils.isValidTextureName(textureTop))
			throw new IllegalArgumentException("Texture name is invalid.");
		this.textureTop = textureTop;
	}
	
	/**
	 * @return the top texture name.
	 */
	public String getTextureTop()
	{
		return textureTop;
	}

	/**
	 * Sets the bottom texture name.
	 * @param textureBottom the new texture name.
	 * @throws IllegalArgumentException if the texture name is invalid. 
	 */
	public void setTextureBottom(String textureBottom)
	{
		if (!NameUtils.isValidTextureName(textureBottom))
			throw new IllegalArgumentException("Texture name is invalid.");
		this.textureBottom = textureBottom;
	}
	
	/**
	 * @return the bottom texture name.
	 */
	public String getTextureBottom()
	{
		return textureBottom;
	}

	/**
	 * Sets the middle texture name.
	 * @param textureMiddle the new texture name.
	 * @throws IllegalArgumentException if the texture name is invalid. 
	 */
	public void setTextureMiddle(String textureMiddle)
	{
		if (!NameUtils.isValidTextureName(textureMiddle))
			throw new IllegalArgumentException("Texture name is invalid.");
		this.textureMiddle = textureMiddle;
	}
	
	/**
	 * @return the middle texture name.
	 */
	public String getTextureMiddle()
	{
		return textureMiddle;
	}

	/**
	 * Sets the sector reference index for this sidedef.
	 * @param sectorIndex the sector reference index.
	 * @throws IllegalArgumentException if the offset is outside the range 0 to 65535.
	 */
	public void setSectorIndex(int sectorIndex)
	{
		RangeUtils.checkShort("Sector Index", sectorIndex);
		this.sectorIndex = sectorIndex;
	}
	
	/**
	 * @return the index of the sector.
	 */
	public int getSectorIndex()
	{
		return sectorIndex;
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

	/*** getEightByteName
	 * Fetches eight characters exactly and trims the nulls before returning the string.
	 * Will mutate bb.
	 * @param bb
	 * @return
	 */
	public static String getEightByteName(ByteBuffer bb) {
		StringBuilder nameBuilder = new StringBuilder(8);
		for(int i=0; i < 8; i++) {
			char c = bb.getChar();
			if (c != '\0') {
				nameBuilder.append(c);
			}
		}
		return nameBuilder.toString();
	}

	@Override
	public void readBytes(InputStream in) throws IOException
	{
		ByteBuffer br = ByteBuffer.allocate(2+2+8+8+8+2);
		in.read(br.array(), 0, br.capacity());
		br.order(ByteOrder.LITTLE_ENDIAN);
		offsetX = br.getShort();
		offsetY = br.getShort();
		textureTop = getEightByteName(br).toUpperCase();
		textureBottom = getEightByteName(br).toUpperCase();
		textureMiddle = getEightByteName(br).toUpperCase();
		sectorIndex = br.getShort();
	}

	public static void putEightByteName(ByteBuffer bb, String name) {
		for(int i=0; i < 8; i++) {
			if(i < name.length()) {
				bb.putChar(name.charAt(i));
			} else {
				bb.putChar('\0');
			}
		}
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		ByteBuffer bb = ByteBuffer.allocate(2+2+8+8+8+2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort((short)offsetX);
		bb.putShort((short)offsetY);
		putEightByteName(bb, textureTop);
		putEightByteName(bb, textureBottom);
		putEightByteName(bb, textureMiddle);
		bb.putShort((short)sectorIndex);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Sidedef");
		sb.append(' ').append("Offset (").append(offsetX).append(", ").append(offsetY).append(")");
		sb.append(' ').append(String.format("%-8s %-8s %-8s", textureTop, textureBottom, textureMiddle));
		sb.append(' ').append("Sector ").append(sectorIndex);
		return sb.toString();
	}
	
}
