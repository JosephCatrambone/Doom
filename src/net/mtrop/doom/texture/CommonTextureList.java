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
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Iterator;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.NameUtils;

import com.blackrook.commons.Common;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.map.AbstractMappedVector;
import com.blackrook.io.SuperWriter;

/**
 * This is the lump that contains a collection of textures.
 * All textures are stored in here, usually named TEXTURE1 or TEXTURE2 in a WAD.
 * Most creation methods in this object are factory-style, due to the diversity of implemented texture formats.
 * @author Matthew Tropiano
 */
public abstract class CommonTextureList<T extends CommonTexture<?>> implements BinaryObject, Iterable<T>, Sizable
{
	/** Internal list. */
	private AbstractMappedVector<T, String> list;
	
	/**
	 * Creates a new TextureList with a default starting capacity.
	 */
	public CommonTextureList()
	{
		this(32);
	}

	/**
	 * Creates a new TextureList with a specific starting capacity.
	 * @param capacity the starting capacity.
	 */
	@SuppressWarnings("unchecked")
	public CommonTextureList(int capacity)
	{
		this.list = (AbstractMappedVector<T, String>)new AbstractMappedVector<CommonTexture<?>, String>()
		{
			@Override
			protected String getMappingKey(CommonTexture<?> object)
			{
				return object.getName();
			}
		};
	}

	/**
	 * Adds a created texture to this texture list.
	 * Must be called from {@link #createTexture(String)}.
	 * @param texture the texture to add.
	 */
	protected void addCreatedTexture(T texture)
	{
		list.add(texture);
	}
	
	/**
	 * Clears this list of textures.
	 */
	public void clear()
	{
		list.clear();
	}

	/**
	 * Gets the index of a texture in this list by its name.
	 * @param name the name of the texture.
	 * @return a valid index if found, or -1 if not.
	 */
	public int getIndexOf(String name)
	{
		return list.getIndexUsingKey(name);
	}
	
	/**
	 * Gets a texture in this list by its name.
	 * @param name the name of the texture.
	 * @return a valid index if found, or -1 if not.
	 */
	public T getTextureByName(String name)
	{
		return list.getUsingKey(name);
	}
	
	/**
	 * Gets a texture entry in this list by its index.
	 * @param index the index to use. 
	 * @return a valid texture if found, or <code>null</code> if no texture at that index.
	 */
	public T getTextureByIndex(int index)
	{
		return list.getByIndex(index);
	}
	
	/**
	 * Attempts to remove a texture entry by its name.
	 * Note that this will shift the indices of the other entries. 
	 * @param name the name of the entry.
	 * @return true if removed, false if not.
	 */
	public T removeEntry(String name)
	{
		return list.removeUsingKey(name);
	}

	/**
	 * Removes a texture entry at an index.
	 * Note that this will shift the indices of the other entries. 
	 * @param index the index to use. 
	 * @return the entry removed, or <code>null</code> if no entry at that index.
	 */
	public T removeEntryByIndex(int index)
	{
		return list.removeIndex(index);
	}

	/**
	 * Sorts the textures in this texture list using natural ordering.
	 */
	public void sort()
	{
		list.sort();
	}
	
	/**
	 * Sorts the textures in this texture list using the provided comparator.
	 * @param comp the comparator to use.
	 */
	public void sort(Comparator<? super T> comp)
	{
		list.sort(comp);
	}
	
	/**
	 * Creates a new texture in this list with no patches, at the end of the list.
	 * @param name the name of the texture.
	 * @return a new, empty texture object added to this list. 
	 * @throws IllegalArgumentException if the provided name is not a valid name for a texture.
	 * @see NameUtils#isValidTextureName(String)
	 */
	public abstract T createTexture(String name);
	
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
	public void writeBytes(OutputStream out) throws IOException
	{
		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeInt(size());
		
		byte[][] data = new byte[size()][];
	
		int n = 0;
		for (T t : this)
			data[n++] = t.toBytes();
		
		int offset = (size()+1) * 4;
		
		for (byte[] b : data)
		{
			sw.writeInt(offset);
			offset += b.length;
		}
	
		for (byte[] b : data)
			sw.writeBytes(b);
	}

	@Override
	public Iterator<T> iterator() 
	{
		return list.iterator();
	}

	@Override
	public int size() 
	{
		return list.size();
	}
	
	@Override
	public boolean isEmpty() 
	{
		return list.isEmpty();
	}
	
}
