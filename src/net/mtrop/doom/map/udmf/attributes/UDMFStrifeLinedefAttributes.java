/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.udmf.attributes;

/**
 * Contains linedef attributes for Doom namespaces.
 * @author Matthew Tropiano
 */
public interface UDMFStrifeLinedefAttributes extends UDMFCommonLinedefAttributes
{
	/** Linedef flag: Linedef is translucent. */
	public static final String ATTRIB_FLAG_TRANSLUCENT = "translucent";
	/** Linedef flag: Linedef is a railing that can be jumped over. */
	public static final String ATTRIB_FLAG_JUMPOVER = "jumpover";
	/** Linedef flag: Linedef blocks floating enemies. */
	public static final String ATTRIB_FLAG_BLOCK_FLOAT = "blockfloaters";

}
