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
import java.util.Iterator;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.Common;
import com.blackrook.commons.list.List;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.NameUtils;
import net.mtrop.doom.util.RangeUtils;

/**
 * Common contents of texture definitions.
 * @author Matthew Tropiano
 */
public abstract class CommonTexture<P extends CommonPatch> implements BinaryObject, Iterable<P>, Comparable<CommonTexture<?>>
{
	/** Texture name. */
	protected String name;
	/** Width of texture. */
	protected int width;
	/** Height of texture. */
	protected int height;
	/** List of patches. */
	protected List<P> patches;

	/**
	 * Creates a new blank texture.
	 */
	protected CommonTexture()
	{
		this("UNNAMED");
	}
	
	/**
	 * Creates a new texture.
	 * @param name the new texture name.
	 * @throws IllegalArgumentException if the texture name is invalid.
	 */
	public CommonTexture(String name)
	{
		if (!NameUtils.isValidTextureName(name))
			throw new IllegalArgumentException("Invalid texture name.");
		
		this.name = name;
		width = 0;
		height = 0;
		patches = new List<P>(2);
	}
	
	/**
	 * @return the name of this texture.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the width of this texture in pixels.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Sets the width of this texture in pixels.
	 * @param width the new texture width.
	 * @throws IllegalArgumentException if the width is outside the range 0 to 65535.
	 */
	public void setWidth(int width)
	{
		RangeUtils.checkShortUnsigned("Width", width);
		this.width = width;
	}
	
	/**
	 * @return the height of this texture in pixels.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Sets the height of this texture in pixels.
	 * @param height the new texture height.
	 * @throws IllegalArgumentException if the height is outside the range 0 to 65535.
	 */
	public void setHeight(int height)
	{
		RangeUtils.checkShortUnsigned("Height", height);
		this.height = height;
	}

	/**
	 * Creates a new patch entry on this texture, at the end of the list.
	 * The patch has no information set on it, including its name index value and offsets.
	 * @return a newly-added Patch object.
	 */
	public abstract P createPatch();
	
	/**
	 * Shifts the ordering of a patch on this texture.
	 * The ordering of the patches in this texture will change depending on the indexes provided.
	 * @param oldIndex the index to shift.
	 * @param newIndex the destination index.
	 * @see AbstractVector#shift(int, int)
	 */
	public void shiftPatch(int oldIndex, int newIndex)
	{
		patches.shift(oldIndex, newIndex);
	}
	
	/**
	 * Removes a patch entry from this texture by index.
	 * The ordering of the patches in this texture will change depending on the index provided.
	 * @param i	the index of the patch to remove.
	 * @return the patch removed, or null if no patch at that index.
	 */
	public P removePatch(int i)
	{
		return patches.removeIndex(i);
	}
	
	/**
	 * Gets a patch from this texture.
	 * @param i	the index of the patch.
	 * @return the corresponding patch, or null if no patch at that index.
	 */
	public P getPatch(int i)
	{
		return patches.getByIndex(i);
	}
	
	/**
	 * @return the amount of patches on this texture.
	 */
	public int getPatchCount()
	{
		return patches.size();
	}

	@Override
	public Iterator<P> iterator()
	{
		return patches.iterator();
	}

	@Override
	public int compareTo(CommonTexture<?> o)
	{
		return name.compareTo(o.name);
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
	
	
	
}
