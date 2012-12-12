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
package ssp.dbpf.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.properties.DBPFPropertyTypes;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between data and property.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class PropertyConverter {

	private static final String LOGNAME = PropertyConverter.class
			.getSimpleName();

	/**
	 * Private Constructor prevents instance.<br>
	 */
	private PropertyConverter() {
	}

	/**
	 * Decodes the property from the rawData at the given offset.<br>
	 * 
	 * @param dData
	 *            The rawData
	 * @param offset
	 *            The offset
	 * @return The property
	 * @throws DBPFException
	 *             Thrown, if property cannot be decoded
	 */
	public static DBPFProperty createProperty(short[] dData, int offset)
			throws DBPFException {
		long id = DBPFUtil.getUint32(dData, offset, 4);
		offset += 4;
		short typeID = (short) DBPFUtil.getUint32(dData, offset, 2);
		DBPFPropertyTypes type = DBPFPropertyTypes.forID.get(typeID);

		offset += 2;
		long hasCountLong = DBPFUtil.getUint32(dData, offset, 1);
		offset += 1;
		@SuppressWarnings("unused")
		long unknown = DBPFUtil.getUint32(dData, offset, 2);
		offset += 2;

		boolean hasCount = false;
		int count = 1;
		if (hasCountLong == 0x80 || type == DBPFPropertyTypes.STRING) {
			// explicit check of PropertyType.STRING for some strange found
			// files
			hasCount = true;
			count = (int) DBPFUtil.getUint32(dData, offset, 4);
			offset += 4;
		}
		// System.out.println("Name: " + DBPFUtil.toHex(id, 8) + ", Type: " +
		// type
		// + " = " + DBPFUtil.toHex(type.id, 4) + ", HasCount: "
		// + DBPFUtil.toHex(hasCountLong, 2) + ", Count: " + count);

		DBPFProperty prop = null;
		try {
			prop = type.propertyRawConstructor.newInstance(id, count, type,
					hasCount, dData, offset);
		} catch (InstantiationException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (IllegalAccessException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (IllegalArgumentException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (InvocationTargetException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		}
		if (prop == null) {
			String message = "Property can not be decoded! ID=0x"
					+ DBPFUtil.toHex(id, 8) + ", TypeID="
					+ DBPFUtil.toHex(typeID, 4) + ", HasCount=" + hasCount
					+ ", Count=" + count;
			// DBPFLogger.toLog(LOGNAME, Level.SEVERE, message);
			throw new DBPFException(LOGNAME, message);
		}
		return prop;
	}

	/**
	 * Decodes the property from the given string.<br>
	 * 
	 * @param propString
	 *            The property string
	 * @return The DBPFProperty
	 * @throws DBPFException
	 *             Thrown, if property is unknown
	 */
	public static DBPFProperty createProperty(String propString)
			throws DBPFException {
		String[] tokens = propString.split("=");

		// now analyze the nameValue
		String[] nameValues = tokens[0].split(":");
		long id = Long.decode(nameValues[0]);

		// System.out.println(DBPFUtil.toHex(nameValue, 8));

		// now analyze the value
		String[] propValues = tokens[1].split(":");
		DBPFPropertyTypes type = DBPFPropertyTypes.valueOf(propValues[0]
				.toUpperCase());

		int count = Integer.parseInt(propValues[1]);
		boolean hasCount = true;
		if (count == 0) {
			hasCount = false;
			count = 1;
		}
		String s = propValues[2];
		s = s.replace('{', ' ');
		s = s.replace('}', ' ');
		s = s.replace('\"', ' ');
		String[] data = s.trim().split(",");

		DBPFProperty prop = null;
		try {
			prop = type.propertyTextConstructor.newInstance(id, count, type,
					hasCount, data);
		} catch (InstantiationException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (IllegalAccessException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (IllegalArgumentException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		} catch (InvocationTargetException e) {
			DBPFLogger.toLog(LOGNAME, Level.SEVERE, e.getMessage());
		}
		if (prop == null) {
			String message = "Property can not be decoded! ID=0x"
					+ DBPFUtil.toHex(id, 8) + ", TypeID="
					+ DBPFUtil.toHex(type.id, 4) + ", HasCount=" + hasCount
					+ ", Count=" + count;
			// DBPFLogger.toLog(LOGNAME, Level.SEVERE, message);
			throw new DBPFException(LOGNAME, message);
		}
		return prop;
	}

	/**
	 * Create the data for the given property.<br>
	 * 
	 * @param prop
	 *            The property to create the data from
	 * @param format
	 *            DBPFUtil.MAGICNUMBER_B_FORMAT or T_FORMAT
	 * @return The array
	 * @throws DBPFException
	 *             Thrown, if data cannot be created
	 */
	public static short[] createData(DBPFProperty prop, short format)
			throws DBPFException {
		if (format == DBPFConstant.FORMAT_BINARY) {
			return prop.toRaw();
		} else if (format == DBPFConstant.FORMAT_TEXT) {
			String s = "";
			s = prop.toText();
			short[] retData = new short[s.length()];
			for (int i = 0; i < retData.length; i++) {
				retData[i] = (short) s.charAt(i);
			}
			return retData;
		}
		return null;
	}
}
