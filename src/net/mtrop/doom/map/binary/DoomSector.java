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
import net.mtrop.doom.util.NameUtils;
import net.mtrop.doom.util.RangeUtils;

/**
 * Doom/Boom 26-byte format implementation of Sector.
 * @author Matthew Tropiano
 */
public class DoomSector implements BinaryObject
{
	/** Byte length of this object. */
	public static final int LENGTH = 26;

	/** Sector Floor height. */
	private int floorHeight;
	/** Sector Ceiling height. */
	private int ceilingHeight;
	/** Sector Floor texture. */
	private String floorTexture;
	/** Sector Ceiling texture. */
	private String ceilingTexture;
	/** Sector light level. */
	private int lightLevel;
	/** Sector special. */
	private int special;
	/** Sector tag. */
	private int tag;
	
	/**
	 * Creates a new sector.
	 */
	public DoomSector()
	{
		floorTexture = TEXTURE_BLANK;
		ceilingTexture = TEXTURE_BLANK;
	}
	
	/**
	 * Reads and creates a new DoomSector from an array of bytes.
	 * This reads from the first 26 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new DoomSector with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSector create(byte[] bytes) throws IOException
	{
		DoomSector out = new DoomSector();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomSector from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomSector} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomSector with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSector read(InputStream in) throws IOException
	{
		DoomSector out = new DoomSector();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new DoomSectors from an array of bytes.
	 * This reads from the first 26 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of DoomSector objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSector[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new DoomSectors from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link DoomSector}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of DoomSector objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomSector[] read(InputStream in, int count) throws IOException
	{
		DoomSector[] out = new DoomSector[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new DoomSector();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/**
	 * Sets this sector's floor height. 
	 * @param floorHeight the new height.
	 * @throws IllegalArgumentException if floorHeight is outside of the range -32768 to 32767.
	 */
	public void setFloorHeight(int floorHeight)
	{
		RangeUtils.checkShort("Floor Height", floorHeight);
		this.floorHeight = floorHeight;
	}
	
	/**
	 * @return the sector's floor height.
	 */
	public int getFloorHeight()
	{
		return floorHeight;
	}

	/**
	 * Sets the sector's ceiling height. 
	 * @param ceilingHeight the new height.
	 * @throws IllegalArgumentException if floorHeight is outside of the range -32768 to 32767.
	 */
	public void setCeilingHeight(int ceilingHeight)
	{
		RangeUtils.checkShort("Ceiling Height", ceilingHeight);
		this.ceilingHeight = ceilingHeight;
	}
	
	/**
	 * @return the sector's ceiling height.
	 */
	public int getCeilingHeight()
	{
		return ceilingHeight;
	}

	/**
	 * Sets the sector's floor texture.
	 * @param floorTexture the new texture.
	 * @throws IllegalArgumentException if the texture name is invalid. 
	 */
	public void setFloorTexture(String floorTexture)
	{
		if (!NameUtils.isValidTextureName(floorTexture))
			throw new IllegalArgumentException("Texture name is invalid.");
		this.floorTexture = floorTexture;
	}
	
	/**
	 * @return the sector's floor texture.
	 */
	public String getFloorTexture()
	{
		return floorTexture;
	}

	/**
	 * Sets the sector's ceiling texture. 
	 * @param ceilingTexture the new texture.
	 * @throws IllegalArgumentException if the texture name is invalid. 
	 */
	public void setCeilingTexture(String ceilingTexture)
	{
		if (!NameUtils.isValidTextureName(ceilingTexture))
			throw new IllegalArgumentException("Texture name is invalid.");
		this.ceilingTexture = ceilingTexture;
	}
	
	/**
	 * @return the sector's ceiling texture. 
	 */
	public String getCeilingTexture()
	{
		return ceilingTexture;
	}

	/**
	 * Sets the sector's light level. 
	 * @param lightLevel the new light level.
	 * @throws IllegalArgumentException if lightLevel is outside the range 0 to 255.
	 */
	public void setLightLevel(int lightLevel)
	{
		RangeUtils.checkShort("Light Level", lightLevel);
		this.lightLevel = lightLevel;
	}
	
	/**
	 * @return the sector's light level.
	 */
	public int getLightLevel()
	{
		return lightLevel;
	}

	/**
	 * Sets the sector's special. 
	 * @param special the new special number.
	 * @throws IllegalArgumentException if special is outside the range 0 to 65535.
	 */
	public void setSpecial(int special)
	{
		RangeUtils.checkShort("Special", special);
		this.special = special;
	}
	
	/**
	 * @return the sector's special. 
	 */
	public int getSpecial()
	{
		return special;
	}

	/**
	 * Sets the sector's tag. 
	 * @param tag the new tag.
	 * @throws IllegalArgumentException if tag is outside the range 0 to 65535.
	 */
	public void setTag(int tag)
	{
		RangeUtils.checkShort("Tag", tag);
		this.tag = tag;
	}
	
	/**
	 * @return the sector's tag.
	 */
	public int getTag()
	{
		return tag;
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
		bb.order(ByteOrder.LITTLE_ENDIAN);
		floorHeight = bb.getShort();
		ceilingHeight = bb.getShort();
		StringBuilder floorName = new StringBuilder();
		for(int i=0; i < 8; i++) { floorName.append(bb.getChar()); }
		floorTexture = NameUtils.nullTrim(floorName.toString()).toUpperCase();
		StringBuilder ceilingName = new StringBuilder();
		for(int i=0; i < 8; i++) { ceilingName.append(bb.getChar()); }
		ceilingTexture = NameUtils.nullTrim(ceilingName.toString()).toUpperCase();
		lightLevel = bb.getShort();
		special = bb.getShort();
		tag = bb.getShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		ByteBuffer bb = ByteBuffer.allocate(2+2+8+8+2+2+2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort((short)floorHeight);
		bb.putShort((short)ceilingHeight);
		bb.put(ByteTools.stringToByteArrayWithFixedSize(floorTexture, 8));
		bb.put(ByteTools.stringToByteArrayWithFixedSize(ceilingTexture, 8));
		bb.putShort((short)lightLevel);
		bb.putShort((short)special);
		bb.putShort((short)tag);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Sector");
		sb.append(' ').append("Ceiling ").append(ceilingHeight).append(" Floor ").append(floorHeight);
		sb.append(' ').append(String.format("%-8s %-8s", ceilingTexture, floorTexture));
		sb.append(' ').append("Light ").append(lightLevel);
		sb.append(' ').append("Special ").append(special);
		sb.append(' ').append("Tag ").append(tag);
		return sb.toString();
	}

}
