package net.mtrop.doom.map.udmf;

import java.util.Iterator;

import com.blackrook.commons.hash.CaseInsensitiveHashedQueueMap;
import com.blackrook.commons.linkedlist.Queue;

/**
 * This holds a bunch of UDMFStructs for reading Doom information.
 * Also contains a structure for "global" fields in the UDMF, like "namespace".
 * @author Matthew Tropiano
 */
public class UDMFTable
{
	private static final UDMFObject[] EMPTY_STRUCT_LIST = new UDMFObject[0];
	
	/** Root fields table. */
	private UDMFObject globalFields;
	/** UDMF tables. */
	private CaseInsensitiveHashedQueueMap<UDMFObject> innerTable;
	
	/**
	 * Creates a new UDMFTable.
	 */
	public UDMFTable()
	{
		super();
		globalFields = new UDMFObject();
		innerTable = new CaseInsensitiveHashedQueueMap<UDMFObject>();
	}

	/**
	 * Returns the root fields structure.
	 */
	public UDMFObject getGlobalFields()
	{
		return globalFields;
	}
	
	/**
	 * Returns all structures of a specific name into an array.
	 * The names are case-insensitive.
	 * @param name	the name of the structures to retrieve.
	 * @return	the queue of structures with the matching name in the order that
	 * they were added to the structure. If there are none, an empty array
	 * is returned.
	 */
	public UDMFObject[] getStructs(String name)
	{
		Queue<UDMFObject> list = innerTable.get(name);
		if (list == null)
			return EMPTY_STRUCT_LIST;
		UDMFObject[] out = new UDMFObject[list.size()];
		list.toArray(out);
		return out;
	}
	
	/**
	 * Adds a struct of a particular name to this table.
	 * Keep in mind that the order in which these are added is important.
	 * @param name	the name of this type of structure.
	 * @return	a reference to the new structure created.
	 */
	public UDMFObject addStruct(String name)
	{
		return addStruct(name, new UDMFObject());
	}

	/**
	 * Adds a struct of a particular type name to this table.
	 * Keep in mind that the order in which these are added is important.
	 * @param name	the name of this type of structure.
	 * @return	a reference to the added structure.
	 */
	public UDMFObject addStruct(String name, UDMFObject struct)
	{
		innerTable.enqueue(name, struct);
		return struct;
	}
	
	/**
	 * Returns a list of all of the struct type names in the table.
	 */
	public String[] getAllStructNames()
	{
		String[] out = new String[innerTable.size()];
		int i = 0;
		Iterator<String> it = innerTable.keyIterator();
		while (it.hasNext())
			out[i++] = it.next();
		return out;
	}
	
}