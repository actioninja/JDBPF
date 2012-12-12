/*
 * Copyright (c) 2012 by Stefan Wertich.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 */
package ssp.dbpf.properties;

import java.io.IOException;

import ssp.dbpf.event.DBPFException;

/**
 * Defines a property of a DBPFType.<br>
 * <br>
 * The property consists of the following byte data:<br>
 * <ol>
 * <li>byte{4} NameValue/ID</li>
 * <li>byte{2} DataType</li>
 * <li>byte{1} RepIs? (80=Yes, 00=No)</li>
 * <li>byte{2} Unknown (00 00)</li>
 * <li>byte{4} RepSize, if RepIs=Yes</li>
 * <li>byte{x} Values</li>
 * </ol>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public interface DBPFProperty {

	/**
	 * Copies multiple values from this property to the destination.
	 * 
	 * @param srcPos
	 *            the position in this property to start copying from
	 * @param dest
	 *            the destination to copy this property's values to
	 * @param destPos
	 *            the position in the destination to copy to
	 * @param length
	 *            the number of values to copy
	 * 
	 * @see System#arraycopy(Object, int, Object, int, int)
	 */
	// public void getValues(int srcPos, Object dest, int destPos, int length);

	/**
	 * Retrieves a copy of the current values this property represents.
	 * 
	 * @return A copy of this property's values.
	 */
	// public Object getValues();

	/**
	 * Retrieves the value at the given index.
	 * 
	 * @param index
	 *            the index of value
	 * 
	 * @return the value
	 */
	// public Object getValue(int index);

	/**
	 * Retrieves the reference to the values of this property.</p>
	 * 
	 * For performance reasons this returns the actual data structure in which
	 * the values are stored internally. As such, the object returned by this
	 * method should NOT be modified. Implementations of this method should
	 * return a non-null object so that no null-checking is required.
	 * 
	 * @return the reference to values of this property
	 */
	// public Object getValuesQuick();

	/**
	 * Copies the values of the given property into this property
	 * 
	 * @param dest
	 *            the property to copy
	 */
	// public void setValues(DBPFProperty dest);

	/**
	 * Copies the given values into this property and updates the count.
	 * 
	 * @param values
	 *            the new values of this property.
	 */
	// public void setValues(Object values);

	/**
	 * Sets multiple values in this property to match the given values.
	 * 
	 * @param values
	 *            the source from which to copy the new values
	 * @param srcPos
	 *            the position in this property to start copying from
	 * @param destPos
	 *            the position in the destination to copy to
	 * @param length
	 *            the number of values to copy
	 * 
	 * @see System#arraycopy(java.lang.Object, int, java.lang.Object, int, int),
	 *      where this property is the source.
	 */
	// public void setValues(Object values, int srcPos, int destPos, int
	// length);

	/**
	 * Sets a single value.
	 * 
	 * @param value
	 *            the new value
	 * @param index
	 *            the index of the value
	 */
	// public void setValue(Object value, int index);

	/**
	 * The number of bytes required to hold one count of this property's value
	 * 
	 * @return the size of one count of this property's value
	 */
	// public int getDatumLength();

	/**
	 * Returns a short array for this property.<br>
	 * 
	 * The data is in the Binary-Format (0x42) and includes alle data for the
	 * property, e.g.:<br>
	 * 20 00 00 00 00 0C 80 00 | 00 10 00 00 00 4C 4D 39<br>
	 * 78 36 5F 53 61 63 72 65 | 43 6F 75 65 72<br>
	 * for:<br>
	 * ID:Exemplar-Name, Type:STRING, HasCount:True, Length:16, String:
	 * LM9x6_SacreCouer
	 * 
	 * Updates the hasCount from the length of values.
	 * 
	 * @return An array
	 */
	public short[] toRaw();

	/**
	 * Returns the binary-format (0x42) data length for this property.
	 * 
	 * @return binary-format length, in bytes
	 */
	public int getBinaryLength();

	/**
	 * Returns a string for this property.<br>
	 * 
	 * The data is in the text-format (0x54), e.g.:<br>
	 * 0x00000010:{"Exemplar Type"}=Uint32:0:{0x00000002}
	 * 
	 * @throws IOException
	 *             Thrown, if error creating the text
	 */
	public String toText() throws DBPFException;

	/**
	 * Sets the 32-bit value that uniquely identifies this property.
	 * 
	 * @param id
	 *            property's new ID
	 */
	public void setID(long id);

	/**
	 * Retrieves the 32-bit value that uniquely identifies this property.
	 * 
	 * @return property's ID
	 */
	public long getID();

	/**
	 * Gets the type of data that this property's values represent.
	 * 
	 * @return the PropertyType constant indicating what kind of data this
	 *         property stores.
	 */
	public DBPFPropertyTypes getType();

	/**
	 * Sets the type of data stored in this property.
	 * 
	 * @param type
	 *            the type of data stored in this property
	 */
	public void setType(DBPFPropertyTypes type);

	/**
	 * Retrieves how many items are in this property's value. Normally, this
	 * managed automatically.
	 * 
	 * @return the new number of items in this property's value
	 */
	public int getCount();

	/**
	 * Sets how many items are in this property's value. Normally, this managed
	 * automatically.<br>
	 * The array with the values will be padded or truncated to the new count.
	 * 
	 * @param count
	 *            the new number of items in this property's value
	 */
	public void setCount(int count);

	/**
	 * Specifies if this property's value is an array or not. This value is not
	 * managed automatically and must be set by the user of this property. This
	 * is done so an array of length 1 can be distinguished from a single value,
	 * as these are treated differently when written to a DBPF file.
	 * 
	 * @param hasCount
	 *            true if this property's value is an array of values, false if
	 *            it not.
	 */
	public void setHasCount(boolean hasCount);

	/**
	 * Specifies if this property's value is an array or not. This value is not
	 * managed automatically and must be set by the user of this property.
	 * 
	 * @return true if this property's value is an array of values, false
	 *         otherwise.
	 */
	public boolean hasCount();
}
