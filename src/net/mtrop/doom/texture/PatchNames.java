/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.texture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.blackrook.commons.Common;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.map.AbstractMappedVector;
import com.blackrook.io.SuperReader;
import com.blackrook.io.SuperWriter;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.NameUtils;

/**
 * A list of names of available patch entries for texture composition.
 * @author Matthew Tropiano
 */
public class PatchNames implements BinaryObject, ResettableIterable<String>, Sizable
{
	/** List of names. */
	protected AbstractMappedVector<String, String> nameList;

	/**
	 * Creates a new PatchNames with a default starting capacity.
	 */
	public PatchNames()
	{
		this.nameList = new AbstractMappedVector<String, String>(32)
		{
			@Override
			protected String getMappingKey(String object) 
			{
				return object;
			}
		};
	}

	/**
	 * Reads and creates a new PatchNames from an array of bytes.
	 * This reads a full patch name set from the array.
	 * @param bytes the byte array to read.
	 * @return a new PatchNames object.
	 * @throws IOException if the stream cannot be read.
	 */
	public static PatchNames create(byte[] bytes) throws IOException
	{
		PatchNames out = new PatchNames();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomTextureList from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a full patch name set are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new PatchNames object.
	 * @throws IOException if the stream cannot be read.
	 */
	public static PatchNames read(InputStream in) throws IOException
	{
		PatchNames out = new PatchNames();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Clears this list of patches.
	 */
	public void clear()
	{
		nameList.clear();
	}

	/**
	 * Adds a patch entry.
	 * @param name the entry name.
	 * @return the index of the added entry, or an existing index if it was already in the list.
	 * @throws IllegalArgumentException if the provided name is not a valid entry name.
	 * @see NameUtils#isValidEntryName(String) 
	 */
	public int addEntry(String name)
	{
		if (!NameUtils.isValidEntryName(name))
			throw new IllegalArgumentException("name is not a valid entry name.");
		
		if (nameList.contains(name))
			return nameList.getIndexOf(name);
		
		int out = nameList.size();
		nameList.add(name);
		return out;
	}
	
	/**
	 * Gets the patch entry at a specific index.
	 * @param index the index to look up.
	 * @return the corresponding index or <code>null</code> if no corresponding entry. 
	 */
	public String getEntry(int index)
	{
		return nameList.getByIndex(index);
	}
	
	/**
	 * Gets the index of a patch name in this lump by its name.
	 * Search is sequential.
	 * @param name the name of the patch.
	 * @return a valid index if found, or -1 if not.
	 */
	public int getIndexOfEntry(String name)
	{
		return nameList.getIndexOf(name);
	}

	/**
	 * Attempts to remove an entry by its name.
	 * Note that this will shift the indices of the other entries. 
	 * @param name the name of the entry.
	 * @return true if removed, false if not.
	 */
	public boolean removeEntry(String name)
	{
		return nameList.remove(name);
	}
	
	/**
	 * Removes an entry at an index.
	 * Note that this will shift the indices of the other entries. 
	 * @param index the index to use. 
	 * @return the entry removed, or <code>null</code> if no entry at that index.
	 */
	public String removeEntryByIndex(int index)
	{
		return nameList.removeIndex(index);
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
		Common.close(bin);
	}

	@Override
	public void readBytes(InputStream in) throws IOException
	{
		clear();
		SuperReader sr = new SuperReader(in, SuperReader.LITTLE_ENDIAN);
		int n = sr.readInt();
		while (n-- > 0)
			addEntry(NameUtils.toValidEntryName(NameUtils.nullTrim(sr.readASCIIString(8))));
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeInt(size());
		for (String s : this)
			sw.writeBytes(NameUtils.toASCIIBytes(s, 8));
	}

	@Override
	public ResettableIterator<String> iterator()
	{
		return nameList.iterator();
	}

	@Override
	public int size() 
	{
		return nameList.size();
	}

	@Override
	public boolean isEmpty() 
	{
		return nameList.isEmpty();
	}

}
