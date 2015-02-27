/*******************************************************************************
 * Copyright (c) 2015 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.udmf;

/**
 * Contains common vertex attributes on some UDMF structures.
 * @author Matthew Tropiano
 */
public interface UDMFCommonVertexAttributes extends UDMFCommonAttributes
{
	/** Vertex position: x-coordinate. */
	public static final String ATTRIB_POSITION_X = "x";
	/** Vertex position: y-coordinate. */
	public static final String ATTRIB_POSITION_Y = "y";

}
