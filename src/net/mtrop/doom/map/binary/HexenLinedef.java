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
import java.util.Arrays;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.ByteTools;
import net.mtrop.doom.util.RangeUtils;

/**
 * Hexen/ZDoom 16-byte format implementation of Linedef.
 * @author Matthew Tropiano
 */
public class HexenLinedef extends CommonLinedef implements BinaryObject 
{
	/** Byte length of this object. */
	public static final int LENGTH = 16;

	/** Special activation: player walks over. */
	public static final int ACTIVATION_PLAYER_CROSSES = 0;
	/** Special activation: player uses. */
	public static final int ACTIVATION_PLAYER_USES = 1;
	/** Special activation: monster walks over. */
	public static final int ACTIVATION_MONSTER_CROSSES = 2;
	/** Special activation: projectile hits. */
	public static final int ACTIVATION_PROJECTILE_HITS = 3;
	/** Special activation: player bumps. */
	public static final int ACTIVATION_PLAYER_BUMPS = 4;
	/** Special activation: projectile crosses. */
	public static final int ACTIVATION_PROJECTILE_CROSSES = 5;
	/** Special activation: player uses (with passthru). */
	public static final int ACTIVATION_PLAYER_USES_PASSTHRU = 6;
	
	public static final String[] ACTIVATION_NAME = new String[]{
		"PlayerCrosses",
		"PlayerUses",
		"MonsterCrosses",
		"ProjectileHits",
		"PlayerBumps",
		"ProjectileCrosses",
		"PlayerUsesPassthru"
	};

	public static final int[] ACTIVATION_FLAGS = new int[]{
		0x00000,
		0x00400,
		0x00800,
		0x00C00,
		0x01000,
		0x01400,
		0x01800
	};
	
	/** Flag: Line special is repeated. */
	protected boolean repeatable;
	/** Flag: (ZDoom) Line is activated by players and monsters. */
	protected boolean activatedByMonsters;
	/** Flag: (ZDoom) Line blocks everything. */
	protected boolean blocksEverything;

	/** Special activation type. */
	protected int activationType;

	/** Thing action special arguments. */
	protected int[] arguments;

	/**
	 * Creates a new linedef.
	 */
	public HexenLinedef()
	{
		arguments = new int[5];
	}

	/**
	 * Reads and creates a new HexenLinedef from an array of bytes.
	 * This reads from the first 14 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new HexenLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static HexenLinedef create(byte[] bytes) throws IOException
	{
		HexenLinedef out = new HexenLinedef();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new HexenLinedef from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link HexenLinedef} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new HexenLinedef with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static HexenLinedef read(InputStream in) throws IOException
	{
		HexenLinedef out = new HexenLinedef();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new HexenLinedefs from an array of bytes.
	 * This reads from the first 14 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of HexenLinedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static HexenLinedef[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new HexenLinedefs from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link HexenLinedef}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of HexenLinedef objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static HexenLinedef[] read(InputStream in, int count) throws IOException
	{
		HexenLinedef[] out = new HexenLinedef[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new HexenLinedef();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/**
	 * @return true if this line's special is repeatable, false if not.
	 */
	public boolean isRepeatable()
	{
		return repeatable;
	}
	
	/**
	 * Sets if this line's special is repeatable.
	 * @param repeatable true to set, false to clear.
	 */
	public void setRepeatable(boolean repeatable)
	{
		this.repeatable = repeatable;
	}

	/**
	 * Gets the activation type of this line.
	 * @return the activation type of the line's special.
	 */
	public int getActivationType() 
	{
		return activationType;
	}
	
	/**
	 * Sets the activation type of this line.
	 * See the <code>ACTIVATION</code> constants. 
	 * @param activationType the new activation type of the line's special.
	 * @throws IllegalArgumentException if <code>activationType</code> is less than 0 or greater than 6.
	 */
	public void setActivationType(int activationType) 
	{
		RangeUtils.checkRange("Activation Type", ACTIVATION_PLAYER_CROSSES, ACTIVATION_PLAYER_USES_PASSTHRU, activationType);
		this.activationType = activationType;
	}
	
	/**
	 * @return true if this line's special is activated by monsters, false if not.
	 */
	public boolean isActivatedByMonsters()
	{
		return activatedByMonsters;
	}
	
	/**
	 * Sets if this line's special is activated by monsters.
	 * @param activatedByMonsters true to set, false to clear.
	 */
	public void setActivatedByMonsters(boolean activatedByMonsters)
	{
		this.activatedByMonsters = activatedByMonsters;
	}
	
	/**
	 * @return true if this line blocks everything, false if not.
	 */
	public boolean isBlocksEverything()
	{
		return blocksEverything;
	}
	
	/**
	 * Sets if this line blocks everything.
	 * @param blocksEverything true to set, false to clear.
	 */
	public void setBlocksEverything(boolean blocksEverything)
	{
		this.blocksEverything = blocksEverything;
	}
	
	/**
	 * Sets the linedef special type. 
	 * @param special the new special number.
	 * @throws IllegalArgumentException if special is outside the range 0 to 255.
	 */
	public void setSpecial(int special)
	{
		RangeUtils.checkByteUnsigned("Special", special);
		this.special = special;
	}

	/**
	 * Sets the special arguments.
	 * @param arguments the arguments to set (5 maximum)
	 * @throws IllegalArgumentException if length of arguments is greater than 5, or any argument is less than 0 or greater than 255. 
	 */
	public void setArguments(int ... arguments)
	{
		if (arguments.length > 5)
			 throw new IllegalArgumentException("Length of arguments is greater than 5.");
		
		int i;
		for (i = 0; i < arguments.length; i++)
		{
			RangeUtils.checkByteUnsigned("Argument " + i, arguments[i]);
			this.arguments[i] = arguments[i];
		}
		for (; i < 5; i++)
		{
			this.arguments[i] = 0;
		}
		
	}

	/**
	 * Gets the special arguments copied into a new array. 
	 * @return gets the array of special arguments.
	 */
	public int[] getArguments()
	{
		int[] out = new int[5];
		System.arraycopy(arguments, 0, out, 0, 5);
		return out;
	}

	/**
	 * Gets a special argument.
	 * @param n the argument index (up to 4) 
	 * @return the argument value.
	 * @throws ArrayIndexOutOfBoundsException if <code>n</code> is less than 0 or greater than 4. 
	 */
	public int getArgument(int n)
	{
		return arguments[n];
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
		
		vertexStartIndex = bb.getShort(); // TODO: Unsigned.
		vertexEndIndex = bb.getShort(); // TODO: Unsigned.
		
		// bitflags
		int flags = bb.getShort(); // TODO: Unsigned
		impassable = ByteTools.bitIsSet(flags, (1 << 0));
		monsterBlocking = ByteTools.bitIsSet(flags, (1 << 1));
		twoSided = ByteTools.bitIsSet(flags, (1 << 2));
		upperUnpegged = ByteTools.bitIsSet(flags, (1 << 3));
		lowerUnpegged = ByteTools.bitIsSet(flags, (1 << 4));
		secret = ByteTools.bitIsSet(flags, (1 << 5));
		soundBlocking = ByteTools.bitIsSet(flags, (1 << 6));
		notDrawn = ByteTools.bitIsSet(flags, (1 << 7));
		mapped = ByteTools.bitIsSet(flags, (1 << 8));
		repeatable = ByteTools.bitIsSet(flags, (1 << 9));
		activatedByMonsters = ByteTools.bitIsSet(flags, (1 << 13));
		blocksEverything = ByteTools.bitIsSet(flags, (1 << 15));
		
		activationType = (0x01C00 & flags) >> 10;
		
		special = bb.get(); // TODO: Unsigned
		arguments[0] = bb.get(); // TODO: Unsigned
		arguments[1] = bb.get(); // TODO: Unsigned
		arguments[2] = bb.get(); // TODO: Unsigned
		arguments[3] = bb.get(); // TODO: Unsigned
		arguments[4] = bb.get(); // TODO: Unsigned

		sidedefFrontIndex = bb.getShort();
		sidedefBackIndex = bb.getShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException 
	{
		ByteBuffer bb = ByteBuffer.allocate(2+2+2+6+2+2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort((short)vertexStartIndex); // TODO: Unsigned
		bb.putShort((short)vertexEndIndex); // TODO: Unsigned
		
		int flags = ByteTools.booleansToInt(
			impassable,
			monsterBlocking,
			twoSided,
			upperUnpegged,
			lowerUnpegged,
			secret,
			soundBlocking,
			notDrawn,
			mapped,
			repeatable,
			false,
			false,
			false,
			activatedByMonsters,
			false,
			blocksEverything
		);
		
		flags |= ACTIVATION_FLAGS[activationType];
		
		bb.putShort((short)flags); // TODO: Unsigned.
		
		bb.put((byte)special);
		bb.put((byte)arguments[0]);
		bb.put((byte)arguments[1]);
		bb.put((byte)arguments[2]);
		bb.put((byte)arguments[3]);
		bb.put((byte)arguments[4]);

		bb.putShort((short)sidedefFrontIndex);
		bb.putShort((short)sidedefBackIndex);

		out.write(bb.array());
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
		if (repeatable) sb.append(' ').append("REPEATABLE");
		if (activatedByMonsters) sb.append(' ').append("PLAYERSANDMONSTERACTIVATE");
		if (blocksEverything) sb.append(' ').append("BLOCKEVERYTHING");
		
		sb.append(' ').append("Special ").append(special);
		sb.append(' ').append("Args ").append(Arrays.toString(arguments));
		sb.append(' ').append("Activation ").append(ACTIVATION_NAME[activationType]);
		return sb.toString();
	}
	
}
