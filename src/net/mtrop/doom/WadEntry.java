/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.mtrop.doom.util.NameUtils;

/**
 * Abstraction of a single entry from a WAD.
 * This entry contains NO DATA - this is a descriptor for the data in the originating WAD.
 * @author Matthew Tropiano
 */
public class WadEntry
{
	/** The name of the entry. */
	String name;
	/** The offset into the original WAD for the start of the data. */
	int offset;
	/** The size of the entry content in bytes. */
	int size;
	
	private WadEntry(String name, int offset, int size)
	{
		this.name = name;
		this.offset = offset;
		this.size = size;
	}

	/**
	 * Creates a WadEntry.
	 * @param name the name of the entry.
	 * @param offset the offset into the WAD in bytes.
	 * @param size the size of the entry in bytes.
	 * @return the constructed WadEntry.
	 * @throws IllegalArgumentException if the name is invalid or the offset or size is negative.
	 */
	public static WadEntry create(String name, int offset, int size)
	{
		if (!NameUtils.isValidEntryName(name))
			throw new IllegalArgumentException("Entry name \""+name+"\" does not fit entry requirements.");
		if (offset < 0)
			throw new IllegalArgumentException("Entry offset is negative.");
		if (size < 0)
			throw new IllegalArgumentException("Entry size is negative.");
		
		return new WadEntry(name, offset, size); 
	}
	
	/**
	 * Creates a WadEntry from a piece of raw entry data from a WAD.
	 * Reads the first 16 bytes.
	 * @param data the byte representation of the entry.
	 * @return the constructed WadEntry.
	 * @throws IOException if the data cannot be read for some reason.
	 */
	public static WadEntry create(byte[] data) throws IOException
	{
		ByteBuffer bb = ByteBuffer.wrap(data);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int offset = bb.getInt();
		int size = bb.getInt();
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < 8; i++) {
			char c = bb.getChar();
			if(c != '\0') {
				sb.append(c);
			}
		}
		String name = sb.toString().toUpperCase();
		return new WadEntry(name, offset, size); 
	}
	
	/**
	 * @return the name of the entry.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the offset into the original WAD for the start of the data.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @return the size of the entry content in bytes.
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * Tests if this entry is a "marker" entry. Marker entries have 0 size.
	 * @return true if size = 0, false if not.
	 */
	public boolean isMarker()
	{
		return size == 0;
	}

	/**
	 * Returns this entry's name as how it is represented in a WAD.
	 * @return a byte array of length 8 containing the output data.
	 */
	public byte[] getNameBytes()
	{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);

		for(char c : name.toCharArray()) {
			bb.putChar(c);
		}
		for(int i=name.length(); i < 8; i++) {
			bb.put((byte)0x00);
		}

		return bb.array();
	}

	/**
	 * Returns this entry as a set of serialized bytes - how it is represented in a WAD.
	 * @return a byte array of length 16 containing the output data.
	 */
	public byte[] getBytes()
	{
		ByteBuffer bb = ByteBuffer.allocate(4+4+8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(offset);
		bb.putInt(size);
		bb.put(getNameBytes());
		return bb.array();
	}
	
	@Override
	public String toString()
	{
		return String.format("WadEntry %-8s Offset: %d, Size: %d", name, offset, size);
	}
	
}
