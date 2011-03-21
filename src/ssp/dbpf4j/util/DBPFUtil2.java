package ssp.dbpf4j.util;

import java.util.ArrayList;
import java.util.List;

import ssp.dbpf4j.entries.DBPFEntry;
import ssp.dbpf4j.properties.DBPFFloatProperty;
import ssp.dbpf4j.properties.DBPFLongProperty;
import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.properties.DBPFStringProperty;
import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.types.DBPFExemplar;
import ssp.dbpf4j.types.DBPFType;

/**
 * Extended tools for operations on DBPF.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 05.01.2011
 * 
 */
public class DBPFUtil2 {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// TGI
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the count of the given TGI in the entryList.<br>
	 * 
	 * @param entryList
	 *            The entryList
	 * @param tgiKey
	 *            The TGIKey
	 * @return The count
	 */
	public static int countTGI(List<DBPFEntry> entryList, TGIKey tgiKey) {
		int count = 0;
		for (DBPFEntry entry : entryList) {
			if (entry.getTGIKey().equals(tgiKey)) {
				count++;
			}
		}
		return count;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Type
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * Search a typeList for a type with the given TGI.<br>
	 * 
	 * @param typeList
	 *            The typeList
	 * @param tgiKey
	 *            The TGIKey
	 * 
	 * @return The founded types; if nothing found: length is zero
	 */
	public static DBPFType[] searchType(List<DBPFType> typeList, TGIKey tgiKey) {
		List<DBPFType> foundEntries = new ArrayList<DBPFType>();
		for (int i = 0; i < typeList.size(); i++) {
			DBPFType type = typeList.get(i);
			if (type.getTGIKey().equals(tgiKey)) {
				foundEntries.add(type);
			}
		}
		DBPFType[] ret = new DBPFType[foundEntries.size()];
		return foundEntries.toArray(ret);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Entry
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Search an entryList for an entry with the given TGI.<br>
	 * 
	 * @param entryList
	 *            The entryList
	 * @param tgiKey
	 *            The TGIKey
	 * @return The founded entries, might be nothing then size is zero
	 */
	public static DBPFEntry[] searchEntry(List<DBPFEntry> entryList,
			TGIKey tgiKey) {
		List<DBPFEntry> foundEntries = new ArrayList<DBPFEntry>();
		for (int i = 0; i < entryList.size(); i++) {
			DBPFEntry entry = entryList.get(i);
			if (entry.getTGIKey().equals(tgiKey)) {
				foundEntries.add(entry);
			}
		}
		DBPFEntry[] ret = new DBPFEntry[foundEntries.size()];
		return foundEntries.toArray(ret);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Property
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Returns the TGI key from the property.<br>
	 * The property have to have three or more long values to extract the TGI,
	 * which will then be taken from the first three.
	 * 
	 * @param property
	 *            The property with the TGI values
	 * @return The TGIKey or NULL
	 */
	public static TGIKey extractTGI(DBPFProperty property) {
		if (property != null && property instanceof DBPFLongProperty) {
			DBPFLongProperty prop = (DBPFLongProperty) property;
			// Accept more than three values but only takes the first three
			if (prop.getCount() >= 3) {
				return new TGIKey(prop.getLong(0), prop.getLong(1),
						prop.getLong(2));
			}
		}
		return null;
	}

	/**
	 * Search for a property with the given nameValue and check for the type.<br>
	 * 
	 * @param exemplar
	 *            The DBPFExemplar
	 * @param id
	 *            The ID of the property
	 * @param property
	 *            The property to check the type against with
	 * @return The property if found, or NULL, if not found
	 */
	public static DBPFProperty searchProperty(DBPFExemplar exemplar, long id,
			DBPFProperty property) {
		if (exemplar != null) {
			DBPFProperty prop = exemplar.getPropertyByID(id);
			if (prop != null) {
				if (property == null) {
					return prop;
				} else if (property != null
						&& prop.getClass().equals(property.getClass())) {
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
	 * @param id
	 *            The ID of the property
	 * @param index
	 *            The index
	 * @return The long value or -1, if not found
	 */
	public static long getLongValue(DBPFExemplar exemplar, long id, int index) {
		return getLongValue(exemplar.getPropertyByID(id), index);
	}

	/**
	 * Returns the long value of a given property.<br>
	 * 
	 * @param property
	 *            The property
	 * @param index
	 *            The index
	 * @return The long value or -1, if not DBPFLongProperty
	 */
	public static long getLongValue(DBPFProperty property, int index) {
		if (property != null && property instanceof DBPFLongProperty) {
			return ((DBPFLongProperty) property).getLong(index);
		}
		return -1;
	}

	/**
	 * Sets a long value in a property of an exemplar.<br>
	 * If the repSize is smaller than the index, false will be returned!
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param id
	 *            The ID of the property
	 * @param value
	 *            The value to set
	 * @param index
	 *            The index of the value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setLongValue(DBPFExemplar exemplar, long id,
			long value, int index) {
		DBPFProperty prop = exemplar.getPropertyByID(id);
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
	 * @param id
	 *            The ID of the property
	 * @param index
	 *            The index
	 * @return The float value or -1, if not found
	 */
	public static float getFloatValue(DBPFExemplar exemplar, long id, int index) {
		return getFloatValue(exemplar.getPropertyByID(id), index);
	}

	/**
	 * Returns the float value of a given property.<br>
	 * 
	 * @param property
	 *            The property
	 * @param index
	 *            The index
	 * @return The float value or -1, if not DBPFFloatProperty
	 */
	public static float getFloatValue(DBPFProperty property, int index) {
		if (property != null && property instanceof DBPFFloatProperty) {
			return ((DBPFFloatProperty) property).getFloat(index);
		}
		return -1;
	}

	/**
	 * Sets a float value in a property of an exemplar.<br>
	 * If the repSize is smaller than the index, false will be returned!
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param id
	 *            The ID of the property
	 * @param value
	 *            The value to set
	 * @param index
	 *            The index of the value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setFloatValue(DBPFExemplar exemplar, long id,
			float value, int index) {
		DBPFProperty prop = exemplar.getPropertyByID(id);
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
	 * @param id
	 *            The ID of the property
	 * @return The string or null, if not found
	 */
	public static String getStringValue(DBPFExemplar exemplar, long id) {
		return getStringValue(exemplar.getPropertyByID(id));
	}

	/**
	 * Returns the string of a given property.<br>
	 * 
	 * @param property
	 *            The property
	 * @return The string or null, if not found
	 */
	public static String getStringValue(DBPFProperty property) {
		if (property != null && property instanceof DBPFStringProperty) {
			return ((DBPFStringProperty) property).getString();
		}
		return null;
	}

	/**
	 * Sets a string in a property of an exemplar.<br>
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @param id
	 *            The ID of the property
	 * @param value
	 *            The value to set
	 * @return TRUE, if set; FALSE, otherwise
	 */
	public static boolean setStringValue(DBPFExemplar exemplar, long id,
			String value) {
		DBPFProperty prop = exemplar.getPropertyByID(id);
		if (prop != null && prop instanceof DBPFStringProperty) {
			((DBPFStringProperty) prop).setString(value);
			return true;
		}
		return false;
	}

	/**
	 * Adds a property to the exemplar properties.<br>
	 * If the property already exists, it will not add again!
	 * 
	 * @param exemplar
	 *            The exemplar to add the property
	 * @param property
	 *            The property to add
	 * @return TRUE, if added; FALSE, if already exists and not added
	 */
	public static boolean addProperty(DBPFExemplar exemplar,
			DBPFProperty property) {
		// search, if prop already exists
		DBPFProperty propFound = exemplar.getPropertyByID(property.getID());
		if (propFound != null) {
			return false;
		}
		// create new property list from existing with one more property
		DBPFProperty[] propListOld = exemplar.getPropertyList();
		DBPFProperty[] propListNew = new DBPFProperty[propListOld.length + 1];
		System.arraycopy(propListOld, 0, propListNew, 0, propListOld.length);
		propListNew[propListOld.length] = property;
		exemplar.setPropertyList(propListNew);
		return true;
	}

	/**
	 * Removes a property from the exemplar properties.<br>
	 * If the property not exists, it will not remove anything!
	 * 
	 * @param exemplar
	 *            The exemplar from which to remove the property
	 * @param id
	 *            The ID of the property to remove
	 * @return TRUE, if removed; FALSE, if not exists
	 */
	public static boolean removeProperty(DBPFExemplar exemplar, long id) {
		// search, if prop exists
		DBPFProperty propFound = exemplar.getPropertyByID(id);
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
	 * @param property
	 *            The property to update
	 * @return TRUE, if updated or added; FALSE, otherwise
	 */
	public static boolean updateProperty(DBPFExemplar exemplar,
			DBPFProperty property) {
		// search, if prop exists
		DBPFProperty propFound = exemplar.getPropertyByID(property.getID());
		if (propFound == null) {
			// prop does not exists, add it
			DBPFProperty[] propListOld = exemplar.getPropertyList();
			DBPFProperty[] propListNew = new DBPFProperty[propListOld.length + 1];
			System.arraycopy(propListOld, 0, propListNew, 0, propListOld.length);
			propListNew[propListOld.length] = property;
			exemplar.setPropertyList(propListNew);
		} else {
			// Update the property with new data
			propFound.setType(property.getType());
			propFound.setHasCount(property.hasCount());
			if (propFound instanceof DBPFStringProperty) {
				DBPFStringProperty propFound2 = (DBPFStringProperty) propFound;
				DBPFStringProperty prop2 = (DBPFStringProperty) property;
				propFound2.setString(prop2.getString());
			} else if (propFound instanceof DBPFLongProperty) {
				DBPFLongProperty propFound2 = (DBPFLongProperty) propFound;
				DBPFLongProperty prop2 = (DBPFLongProperty) property;
				propFound2.setCount(prop2.getCount());
				for (int i = 0; i < prop2.getCount(); i++) {
					propFound2.setLong(prop2.getLong(i), i);
				}
			} else if (propFound instanceof DBPFFloatProperty) {
				DBPFFloatProperty propFound2 = (DBPFFloatProperty) propFound;
				DBPFFloatProperty prop2 = (DBPFFloatProperty) property;
				propFound2.setCount(prop2.getCount());
				for (int i = 0; i < prop2.getCount(); i++) {
					propFound2.setFloat(prop2.getFloat(i), i);
				}
			}
		}
		return true;
	}
}
