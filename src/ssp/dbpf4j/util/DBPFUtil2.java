package ssp.dbpf4j.util;

import java.util.Vector;

import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.properties.DBPFFloatProperty;
import ssp.dbpf4j.properties.DBPFLongProperty;
import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.properties.DBPFStringProperty;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFType;

/**
 * Various tools for DBPF.<br>
 * 
 * @author Stefan Stefan
 * @version 1.5.0, 27.08.2010
 * 
 */
public class DBPFUtil2 {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// TGI
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Check if the given TGI is same as another TGI.<br>
	 * 
	 * If any of the ids is -1, it will be ignored, means this id does not
	 * matter anything.<br>
	 * If one of the array has NOT the length 3, FALSE will be returned.
	 * 
	 * @param tgiEntry
	 *            The TGI of an entry
	 * @param tgiCheck
	 *            The TGI to check with
	 * @return TRUE, if the both are same; FALSE, otherwise
	 */
	public static boolean isTGI(long[] tgiEntry, long[] tgiCheck) {
		if (tgiEntry.length == 3 && tgiCheck.length == 3) {
			boolean tidOK = (tgiCheck[0] == -1) || (tgiEntry[0] == tgiCheck[0]);
			boolean gidOK = (tgiCheck[1] == -1) || (tgiEntry[1] == tgiCheck[1]);
			boolean iidOK = (tgiCheck[2] == -1) || (tgiEntry[2] == tgiCheck[2]);
			if (tidOK && gidOK && iidOK) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the count of the given TGI in this entryList.<br>
	 * 
	 * @param entryList
	 *            The entryList
	 * @return The count
	 */
	public static int getTGICount(Vector<DBPFEntry> entryList, long[] tgi) {
		int count = 0;
		if (tgi.length == 3) {
			for (DBPFEntry entry : entryList) {
				if (isTGI(entry.getTGI(), tgi)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Creates an TGI from the given tid, gid and iid.<br>
	 * 
	 * @param tid
	 *            The TypeID
	 * @param gid
	 *            The GroupID
	 * @param iid
	 *            The InstanceID
	 * @return The tgi array
	 */
	public static long[] createTGI(long tid, long gid, long iid) {
		long[] tgi = new long[3];
		tgi[0] = tid;
		tgi[1] = gid;
		tgi[2] = iid;
		return tgi;
	}

	/**
	 * Modifys the given tgi with the given values and return a new TGI.<br>
	 * A value of -1 will not change this id.
	 * 
	 * @param tgi
	 *            The TGI
	 * @param tid
	 *            The new TID or -1
	 * @param gid
	 *            The new GID or -1
	 * @param iid
	 *            The new IID or -1
	 * @return The modified TGI
	 */
	public static long[] getModifiedTGI(long[] tgi, long tid, long gid, long iid) {
		long[] modified = new long[3];
		for (int i = 0; i < modified.length; i++) {
			modified[i] = tgi[i];
		}
		if (tid != -1) {
			modified[0] = tid;
		}
		if (gid != -1) {
			modified[1] = gid;
		}
		if (iid != -1) {
			modified[2] = iid;
		}
		return modified;
	}
	
	/**
	 * Returns a string with TGI.<br>
	 * The format is: TID GID IID with one space between each ids with length of
	 * 8.
	 * 
	 * @param tgi
	 *            The tgi
	 */
	public static String toString(long[] tgi) {
		return (DBPFUtil.toHex(tgi[0], 8) + " " + DBPFUtil.toHex(tgi[1], 8)
				+ " " + DBPFUtil.toHex(tgi[2], 8));
	}

	/**
	 * Returns the TID of the given type.<br>
	 * 
	 * @param type
	 *            The type
	 * @return The TID
	 * @deprecated Use type.getTID instead
	 */
	public static long getTID(DBPFType type) {
		return type.getTGI()[0];
	}

	/**
	 * Returns the GID of the given type.<br>
	 * 
	 * @param type
	 *            The type
	 * @return The GID
	 * @deprecated Use type.getGID instead
	 */
	public static long getGID(DBPFType type) {
		return type.getTGI()[1];
	}

	/**
	 * Returns the IID of the given type.<br>
	 * 
	 * @param type
	 *            The type
	 * @return The IID
	 * @deprecated Use type.getIID instead
	 */
	public static long getIID(DBPFType type) {
		return type.getTGI()[2];
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// PRINT
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Prints the data in a special HEX format with 16 hex values.<br>
	 * 
	 * @param data
	 *            The data
	 */
	public static void printData(short[] data) {
		System.out.println("## ## ## ## ## ## ## ## | ## ## ## ## ## ## ## ##");
		int count = 0;
		for (Short s : data) {
			System.out.print(DBPFUtil.toHex(s, 2) + " ");
			count++;
			if (count == 8) {
				System.out.print("| ");
			} else if (count == 16) {
				count = 0;
				System.out.println("");
			}
		}
		System.out.println("");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Entry
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Search an entryList for an entry with the given TGI.<br>
	 * 
	 * If any of the ids is -1, it will be ignored, means this id does not
	 * matter anything.
	 * 
	 * @param entryList
	 *            The entryList
	 * @param tgi
	 *            An array with three long values for TID,GID,IID, can be used
	 *            with DBPFEntries
	 * @return The founded entries, might be nothing then size is zero
	 */
	public static DBPFEntry[] searchEntry(Vector<DBPFEntry> entryList,
			long[] tgi) {
		Vector<DBPFEntry> foundEntries = new Vector<DBPFEntry>();
		for (int i = 0; i < entryList.size(); i++) {
			DBPFEntry entry = entryList.get(i);
			if (isTGI(entry.getTGI(), tgi)) {
				foundEntries.addElement(entry);
			}
		}
		// Copy vector to array
		DBPFEntry[] retEntries = new DBPFEntry[foundEntries.size()];
		foundEntries.copyInto(retEntries);
		return retEntries;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Type
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * Search a typeList for a type with the given TGI.<br>
	 * 
	 * If any of the ids is -1, it will be ignored, means this id does not
	 * matter anything.
	 * 
	 * @param typeList
	 *            The typeList
	 * @param tgi
	 *            An array with three long values for TID,GID,IID
	 * @return The founded types; if nothing found: length is zero
	 */
	public static DBPFType[] searchType(Vector<DBPFType> typeList, long[] tgi) {
		Vector<DBPFType> foundEntries = new Vector<DBPFType>();
		for (int i = 0; i < typeList.size(); i++) {
			DBPFType type = typeList.get(i);
			if (isTGI(type.getTGI(), tgi)) {
				foundEntries.addElement(type);
			}
		}
		// Copy vector to array
		DBPFType[] retEntries = new DBPFType[foundEntries.size()];
		foundEntries.copyInto(retEntries);
		return retEntries;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Property
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Returns the long values of this property.<br>
	 * The property must have three long values, else the array has the length
	 * zero!
	 * 
	 * @param property
	 *            The property to get value
	 * @return The long values
	 */
	public static long[] getTGIValue(DBPFProperty property) {
		long[] ret = new long[0];
		if (property != null && property instanceof DBPFLongProperty) {
			DBPFLongProperty prop = (DBPFLongProperty) property;
			if (prop.getCount() == 3) {
				ret = new long[3];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = prop.getLong(i);
				}
			}
		}
		return ret;
	}

	/**
	 * Search for a property with the given nameValue.<br>
	 * 
	 * @param exemplar
	 *            The DBPFExemplar
	 * @param nameValue
	 *            The nameValue
	 * @return The property if found, or NULL, if not found
	 * @deprecated Use exemplar.getPropertyByID instead
	 */
	public static DBPFProperty searchProperty(DBPFExemplar exemplar,
			long nameValue) {		
		if (exemplar != null) {
			return exemplar.getPropertyByID(nameValue);
		}
		return null;
	}

	/**
	 * Search for a property with the given nameValue and check for the type.<br>
	 * 
	 * @param exemplar
	 *            The DBPFExemplar
	 * @param nameValue
	 *            The nameValue
	 * @param propType
	 *            The type of the prop to check against with
	 * @return The property if found, or NULL, if not found
	 */
	public static DBPFProperty searchProperty(DBPFExemplar exemplar,
			long nameValue, DBPFProperty propType) {
		if (exemplar != null) {
			DBPFProperty prop = exemplar.getPropertyByID(nameValue);
			if (prop != null) {
				if (propType == null) {
					return prop;
				} else if (propType != null && prop.getClass().equals(propType.getClass())) {
					return prop;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the long value of a property in the exemplar.<br>
	 * Search the exemplar for the property. If found and LongProperty then
	 * return the value for the given index.
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue
	 * @param index
	 *            The index
	 * @return The long value or -1, if not found
	 */
	public static long getLongValue(DBPFExemplar exemplar, long nameValue,
			int index) {
		return getLongValue(exemplar.getPropertyByID(nameValue), index);
	}

	/**
	 * Returns the long value of a given property.<br>
	 * 
	 * @param prop
	 *            The property
	 * @param index
	 *            The index
	 * @return The long value or -1, if not DBPFLongProperty
	 */
	public static long getLongValue(DBPFProperty prop, int index) {
		if (prop != null && prop instanceof DBPFLongProperty) {
			return ((DBPFLongProperty) prop).getLong(index);
		}
		return -1;
	}

	/**
	 * Sets a long value in a property of an exemplar.<br>
	 * If the repSize is smaller than the index, false will be returned!
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue of the property
	 * @param value
	 *            The value to set
	 * @param index
	 *            The index of the value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setLongValue(DBPFExemplar exemplar, long nameValue,
			long value, int index) {
		DBPFProperty prop = exemplar.getPropertyByID(nameValue);
		if (prop != null && prop instanceof DBPFLongProperty) {
			if (index < prop.getCount()) {
				((DBPFLongProperty) prop).setLong(value, index);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the float value of a property in the exemplar.<br>
	 * Search the exemplar for the property. If found and FloatProperty then
	 * return the value for the given index.
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue
	 * @param index
	 *            The index
	 * @return The float value or -1, if not found
	 */
	public static float getFloatValue(DBPFExemplar exemplar, long nameValue,
			int index) {
		return getFloatValue(exemplar.getPropertyByID(nameValue), index);
	}

	/**
	 * Returns the float value of a given property.<br>
	 * 
	 * @param prop
	 *            The property
	 * @param index
	 *            The index
	 * @return The float value or -1, if not DBPFFloatProperty
	 */
	public static float getFloatValue(DBPFProperty prop, int index) {
		if (prop != null && prop instanceof DBPFFloatProperty) {
			return ((DBPFFloatProperty) prop).getFloat(index);
		}
		return -1;
	}

	/**
	 * Sets a float value in a property of an exemplar.<br>
	 * If the repSize is smaller than the index, false will be returned!
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue of the property
	 * @param value
	 *            The value to set
	 * @param index
	 *            The index of the value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setFloatValue(DBPFExemplar exemplar, long nameValue,
			float value, int index) {
		DBPFProperty prop = exemplar.getPropertyByID(nameValue);
		if (prop != null && prop instanceof DBPFFloatProperty) {
			if (index < prop.getCount()) {
				((DBPFFloatProperty) prop).setFloat(value, index);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the string value of a property in the exemplar.<br>
	 * Search the exemplar for the property. If found and StringProperty then
	 * return the string.
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue
	 * @return The string or null, if not found
	 */
	public static String getStringValue(DBPFExemplar exemplar, long nameValue) {
		return getStringValue(exemplar.getPropertyByID(nameValue));
	}

	/**
	 * Returns the string of a given property.<br>
	 * 
	 * @param prop
	 *            The property
	 * @return The string or null, if not found
	 */
	public static String getStringValue(DBPFProperty prop) {
		if (prop != null && prop instanceof DBPFStringProperty) {
			return ((DBPFStringProperty) prop).getString();
		}
		return null;
	}

	/**
	 * Sets a string in a property of an exemplar.<br>
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param nameValue
	 *            The nameValue of the property
	 * @param value
	 *            The value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setStringValue(DBPFExemplar exemplar, long nameValue,
			String value) {
		DBPFProperty prop = exemplar.getPropertyByID(nameValue);
		if (prop != null && prop instanceof DBPFStringProperty) {
			((DBPFStringProperty) prop).setString(value);
			return true;
		}
		return false;
	}

	/**
	 * Return the long value of the boolean.<br>
	 * If boolean is TRUE this will return 1L, else 0L.
	 * 
	 * @param b
	 *            The boolean
	 * @return 1L, if boolean is TRUE; 0L, otherwise
	 * @deprecated Replaced by DBPFUtil.toLong(b)
	 */
	public static long getLongValue(boolean b) {
		return DBPFUtil.toLong(b);
	}

	/**
	 * Returns the boolean for the given long.<br>
	 * If long is 1L this will return TRUE, else FALSE.
	 * 
	 * @param l
	 *            The long value
	 * @return TRUE, if value = 1L; FALSE, otherwise
	 * @deprecated Replaced by DBPFUtil.toBool(l)
	 */
	public static boolean getBooleanValue(long l) {
		return DBPFUtil.toBool(l);
	}

	/**
	 * Adds a property to the exemplar properties.<br>
	 * If the property already exists, it will not add again!
	 * 
	 * @param exemplar
	 *            The exemplar to add the property
	 * @param prop
	 *            The property to add
	 * @return TRUE, if added; FALSE, if already exists and not added
	 */
	public static boolean addProperty(DBPFExemplar exemplar, DBPFProperty prop) {
		// search, if prop already exists
		DBPFProperty propFound = exemplar.getPropertyByID(prop.getID());
		if (propFound != null) {
			return false;
		}
		// create new property list from existing with one more property
		DBPFProperty[] propListOld = exemplar.getPropertyList();
		DBPFProperty[] propListNew = new DBPFProperty[propListOld.length + 1];
		System.arraycopy(propListOld, 0, propListNew, 0, propListOld.length);
		propListNew[propListOld.length] = prop;
		exemplar.setPropertyList(propListNew);
		return true;
	}

	/**
	 * Removes a property from the exemplar properties.<br>
	 * If the property not exists, it will not remove anything!
	 * 
	 * @param exemplar
	 *            The exemplar from which to remove the property
	 * @param prop
	 *            The property to remove
	 * @return TRUE, if removed; FALSE, if not exists
	 */
	public static boolean removeProperty(DBPFExemplar exemplar,
			DBPFProperty prop) {
		// search, if prop exists
		DBPFProperty propFound = exemplar.getPropertyByID(prop.getID());
		if (propFound == null) {
			return false;
		}
		// create new property list from existing with one lesser property
		DBPFProperty[] propListOld = exemplar.getPropertyList();
		DBPFProperty[] propListNew = new DBPFProperty[propListOld.length - 1];
		int nr = 0;
		for (int i = 0; i < propListOld.length; i++) {
			if (propListOld[i] != propFound) {
				propListNew[nr] = propListOld[i];
				nr++;
			}
		}
		exemplar.setPropertyList(propListNew);
		return true;
	}

	/**
	 * Updates a property of the exemplar properties.<br>
	 * If the property does not exists, it will add the property!
	 * 
	 * @param exemplar
	 *            The exemplar to add the property
	 * @param prop
	 *            The property to update
	 * @return TRUE, if updated or added; FALSE, otherwise
	 */
	public static boolean updateProperty(DBPFExemplar exemplar,
			DBPFProperty prop) {
		// search, if prop exists
		DBPFProperty propFound = exemplar.getPropertyByID(prop.getID());
		if (propFound == null) {
			// prop does not exists, add it
			DBPFProperty[] propListOld = exemplar.getPropertyList();
			DBPFProperty[] propListNew = new DBPFProperty[propListOld.length + 1];
			System
					.arraycopy(propListOld, 0, propListNew, 0,
							propListOld.length);
			propListNew[propListOld.length] = prop;
			exemplar.setPropertyList(propListNew);
		} else {
			// Update the property with new data
			propFound.setType(prop.getType());
			propFound.setHasCount(prop.hasCount());
			if (propFound instanceof DBPFStringProperty) {
				DBPFStringProperty propFound2 = (DBPFStringProperty) propFound;
				DBPFStringProperty prop2 = (DBPFStringProperty) prop;
				propFound2.setString(prop2.getString());
			} else if (propFound instanceof DBPFLongProperty) {
				DBPFLongProperty propFound2 = (DBPFLongProperty) propFound;
				DBPFLongProperty prop2 = (DBPFLongProperty) prop;
				propFound2.setCount(prop2.getCount());
				for (int i = 0; i < prop2.getCount(); i++) {
					propFound2.setLong(prop2.getLong(i), i);
				}
			} else if (propFound instanceof DBPFFloatProperty) {
				DBPFFloatProperty propFound2 = (DBPFFloatProperty) propFound;
				DBPFFloatProperty prop2 = (DBPFFloatProperty) prop;
				propFound2.setCount(prop2.getCount());
				for (int i = 0; i < prop2.getCount(); i++) {
					propFound2.setFloat(prop2.getFloat(i), i);
				}
			}
		}
		return true;
	}
}
