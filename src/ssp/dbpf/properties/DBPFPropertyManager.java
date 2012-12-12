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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFPropertyReader;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Reads the properties for the exemplar and stores them.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 05.12.2012
 * 
 */
public class DBPFPropertyManager {

	// for Logging
	private static final String LOGNAME = DBPFPropertyManager.class.getName();

	// the INSTANCE
	private static volatile DBPFPropertyManager INSTANCE = null;

	/**
	 * Mapping from PropertyID to Property.
	 */
	public final Map<Long, DBPFProperties> forID;

	/**
	 * Mapping from PropertyName to Property. The strings are always UPPER_CASE
	 * for easier handling.
	 */
	public final Map<String, DBPFProperties> forName;

	// private modifiable maps
	private final Map<Long, DBPFProperties> modifiableForID = new HashMap<Long, DBPFProperties>();
	private final Map<String, DBPFProperties> modifiableForName = new HashMap<String, DBPFProperties>();

	/**
	 * Creates an instance of this manager from a given list of properties.<br>
	 * 
	 * @param props
	 *            The list with properties
	 */
	public static void createInstance(List<DBPFProperties> props) {
		INSTANCE = new DBPFPropertyManager(props);
	}

	/**
	 * Returns the instance of this manager.<br>
	 * The instance have to be created before with
	 * 
	 * @return The instance
	 * @throws DBPFException
	 *             Throws, if no instance was created before
	 */
	public static DBPFPropertyManager getInstance() throws DBPFException {
		if (INSTANCE == null) {
			DBPFLogger.toLog(LOGNAME, Level.WARNING,
					"No instance for property manager created before! "
							+ "Try default property reader.");
			createInstance(DBPFPropertyReader.readProperties());
		}
		return INSTANCE;
	}

	/**
	 * Private Constructor.<br>
	 * 
	 * @param props
	 *            A list with properties
	 */
	private DBPFPropertyManager(List<DBPFProperties> props) {
		for (DBPFProperties p : props) {
			modifiableForID.put(p.getId(), p);
			modifiableForName.put(p.getName().toUpperCase(), p);
		}

		forID = Collections.unmodifiableMap(modifiableForID);
		forName = Collections.unmodifiableMap(modifiableForName);
	}

	/**
	 * Returns the first value for the given key.<br>
	 * If the key can not be found, it returns 'UNKNOWN'.
	 * 
	 * @param key
	 *            The key
	 * @return The value or UNKNOWN, if not found
	 * @throws DBPFException
	 *             Thrown, if no instance exists
	 */
	public static String getString(long key) throws DBPFException {
		DBPFPropertyManager props = getInstance();
		if (props != null) {
			Map<Long, DBPFProperties> map = props.forID;
			if (map != null) {
				DBPFProperties prop = map.get(key);
				if (prop != null) {
					return prop.getName();
				}
			}
		}
		DBPFLogger.toLog(LOGNAME, Level.WARNING,
				"Can not find the prop/name for: " + DBPFUtil.toHex(key, 8));
		return "UNKNOWN";
	}

	/**
	 * Returns the first key, which has the given value.<br>
	 * 
	 * @param value
	 *            The value
	 * @return The key or -1, if not found
	 * @throws DBPFException
	 *             Thrown, if no instance exists
	 */
	public static long getKey(String value) throws DBPFException {
		DBPFPropertyManager props = getInstance();
		if (props != null) {
			Map<String, DBPFProperties> map = props.forName;
			if (map != null) {
				DBPFProperties prop = map.get(value);
				if (prop != null) {
					return prop.getId();
				}
			}
		}
		DBPFLogger.toLog(LOGNAME, Level.WARNING,
				"Can not find the prop/key for: " + value);
		return -1;
	}
}
