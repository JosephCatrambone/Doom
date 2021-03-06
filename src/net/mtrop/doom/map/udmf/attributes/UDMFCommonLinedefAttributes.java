/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.udmf.attributes;

/**
 * Contains common linedef attributes on some UDMF structures.
 * @author Matthew Tropiano
 */
public interface UDMFCommonLinedefAttributes extends UDMFCommonAttributes
{
	/** Linedef flag: blocks creatures (players/monsters) a.k.a. "Impassable". */
	public static final String ATTRIB_FLAG_BLOCKING = "blocking";
	/** Linedef flag: blocks monsters. */
	public static final String ATTRIB_FLAG_BLOCK_MONSTERS = "blockmonsters";
	/** Linedef flag: two sided. */
	public static final String ATTRIB_FLAG_TWO_SIDED = "twosided";
	/** Linedef flag: upper texture unpegged. */
	public static final String ATTRIB_FLAG_UNPEG_TOP = "dontpegtop";
	/** Linedef flag: lower texture unpegged. */
	public static final String ATTRIB_FLAG_UNPEG_LOWER = "dontpegbottom";
	/** Linedef flag: Secret (shows up as 1-sided, blocking on automap). */
	public static final String ATTRIB_FLAG_SECRET = "secret";
	/** Linedef flag: Block sound propagation. */
	public static final String ATTRIB_FLAG_BLOCK_SOUND = "blocksound";
	/** Linedef flag: Don't draw on automap. */
	public static final String ATTRIB_FLAG_DONT_DRAW = "dontdraw";
	/** Linedef flag: Already revealed on automap. */
	public static final String ATTRIB_FLAG_MAPPED = "mapped";

	/** Linedef Special type. */
	public static final String ATTRIB_SPECIAL = "special";
	/** Linedef first vertex. */
	public static final String ATTRIB_VERTEX_START = "v1";
	/** Linedef second vertex. */
	public static final String ATTRIB_VERTEX_END = "v2";
	/** Linedef Front Sidedef Reference. */
	public static final String ATTRIB_SIDEDEF_FRONT = "sidefront";
	/** Linedef Back Sidedef Reference. */
	public static final String ATTRIB_SIDEDEF_BACK = "sideback";

}
