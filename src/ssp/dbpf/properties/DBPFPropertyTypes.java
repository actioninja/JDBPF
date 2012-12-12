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

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import ssp.dbpf.util.DBPFLogger;

/**
 * Represents the type of data stored in a {@link DBPFProperty}.<br>
 * <br>
 * The following property types are currently supported:<br>
 * <ol>
 * <li>0x0100 Uint8 byte{1}</li>
 * <li>0x0200 Uint16 byte{2} (byte{4})</li>
 * <li>0x0300 Uint32 byte{4}</li>
 * <li>0x0700 Sint32 byte{4}</li>
 * <li>0x0800 Sint64 byte{8}</li>
 * <li>0x0900 Float32 byte{4} (6 decimals)</li>
 * <li>0x0B00 Bool byte{1}</li>
 * <li>0x0C00 String byte{1} (chars)</li>
 * </ol>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 */
public enum DBPFPropertyTypes {

	UINT8((short) 0x0100, "Uint8", 1, DBPFLongProperty.class), UINT16(
			(short) 0x0200, "Uint16", 2, DBPFLongProperty.class), UINT32(
			(short) 0x0300, "Uint32", 4, DBPFLongProperty.class), SINT32(
			(short) 0x0700, "Sint32", 4, DBPFLongProperty.class), SINT64(
			(short) 0x0800, "Sint64", 8, DBPFLongProperty.class), FLOAT32(
			(short) 0x0900, "Float32", 4, DBPFFloatProperty.class), BOOL(
			(short) 0x0B00, "Bool", 1, DBPFLongProperty.class), STRING(
			(short) 0x0C00, "String", 1, DBPFStringProperty.class);
	
	private final String LOGNAME = DBPFPropertyTypes.class.getSimpleName();

	/** A map of data type IDs ({@link #id}) to PropertyType constants. */
	public static final Map<Short, DBPFPropertyTypes> forID;

	static {
		HashMap<Short, DBPFPropertyTypes> modifiable = new HashMap<Short, DBPFPropertyTypes>(
				DBPFPropertyTypes.values().length);

		for (DBPFPropertyTypes type : DBPFPropertyTypes.values())
			modifiable.put(type.id, type);

		forID = Collections.unmodifiableMap(modifiable);
	}

	/**
	 * The ID of this data type. This is the value used in a DBPF file to
	 * identify properties of this data type.
	 */
	public final short id;

	/**
	 * The name of the property type.<b> Used normally for Text-Format.
	 */
	public final String name;

	/** The length (in bytes) of one property of this type */
	public final int length;

	/** The DBPFProperty that stores this data type */
	public final Class<? extends DBPFProperty> propertyClass;

	/**
	 * Instantiates a DBPFProperty that stores this data type by decoding raw
	 * data. It should point to an implementation of
	 * {@link AbstractDBPFProperty#AbstractDBPFProperty(long, int, DBPFPropertyTypes, boolean, short[], int)}
	 */
	public final Constructor<? extends DBPFProperty> propertyRawConstructor;

	/**
	 * Instantiates a DBPFProperty that stores this data type by decoding text
	 * data. It should point to an implementation of
	 * {@link AbstractDBPFProperty#AbstractDBPFProperty(long, int, DBPFPropertyTypes, boolean, String[])}
	 */
	public final Constructor<? extends DBPFProperty> propertyTextConstructor;

	/**
	 * Constructor.<br>
	 * 
	 * @param id
	 *            The ID of the property
	 * @param length
	 *            The length of the property
	 * @param propertyClass
	 *            The class of the property
	 */
	DBPFPropertyTypes(short id, String name, int length,
			Class<? extends DBPFProperty> propertyClass) {
		this.id = id;
		this.name = name;
		this.length = length;
		this.propertyClass = propertyClass;

		Constructor<? extends DBPFProperty> tempPRC = null;
		try {
			tempPRC = propertyClass.getConstructor(long.class, int.class,
					DBPFPropertyTypes.class, boolean.class,
					new short[0].getClass(), int.class);
		} catch (NoSuchMethodException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (SecurityException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		}
		propertyRawConstructor = tempPRC;

		Constructor<? extends DBPFProperty> tempPTC = null;
		try {
			tempPTC = propertyClass.getConstructor(long.class, int.class,
					DBPFPropertyTypes.class, boolean.class,
					new String[0].getClass());
		} catch (NoSuchMethodException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (SecurityException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		}
		propertyTextConstructor = tempPTC;
	}

	@Override
	public String toString() {
		return name;
	}
}
