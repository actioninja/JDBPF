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
package ssp.dbpf.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ssp.dbpf.properties.DBPFProperties;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.util.DBPFUtil2;

/**
 * Defines the Exemplar types.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public enum DBPFExemplarTypes {
	OTHER(0x00), TUNING(0x01),
	/**
	 * Buildings
	 */
	BUILDING(0x02),
	/**
	 * Zones
	 */
	RCI(0x03), DEVELOPER(0x04), SIMULATOR(0x05),
	/**
	 * Roads
	 */
	ROAD(0x06),
	/**
	 * Bridges
	 */
	BRIDGE(0x07), MISC_NETWORK(0x08), NETWORK_INTERSECTION(0x09),
	/**
	 * Railroads
	 */
	RAIL(0x0A),
	/**
	 * Highways
	 */
	HIGHWAY(0x0B),
	/**
	 * Power lines
	 */
	POWER_LINE(0x0C), TERRAIN(0x0D), ORDINANCE(0x0E),
	/**
	 * Flora
	 */
	FLORA(0x0F),
	/**
	 * LotConfigurations
	 */
	LOT_CONFIGURATION(0x10), FOUNDATION(0x11), ADVICE(0x12), LIGHTING(0x13), CURSOR(
			0x14), LOT_RETAINING_WALL(0x15), VEHICLE(0x16),
	/**
	 * Pedestrians
	 */
	PEDESTRIAN(0x17), AIRCRAFT(0x18), WATERCRAFT(0x19), UNUSED26(0x1A), UNUSED27(
			0x1B), UNUSED28(0x1C), UNUSED29(0x1D),
	/**
	 * Prop
	 */
	PROP(0x1E), CONSTRUCTION(0x1F), AUTOMATA_TUNING(0x20), NETWORK(0x21), DISASTER(
			0x22), DATA_VIEW(0x23), CRIME(0x24), AUDIO(0x25), MY_SIM_TEMPLATE(
			0x26),
	/**
	 * God Mode Tools
	 */
	TERRAIN_BRUSH(0x27),
	/**
	 * Mayor Mode Tools
	 */
	MISC_CATALOG(0x28), UNUSED41(0x29), TREND_BAR(0x2A), GRAPH_CONTROL(0x2B);

	// Stores the strings for the exemplarTypes
	public static final Map<String, DBPFExemplarTypes> strings;

	static {
		HashMap<String, DBPFExemplarTypes> modifiableStrings = new HashMap<String, DBPFExemplarTypes>();

		for (DBPFExemplarTypes type : DBPFExemplarTypes.values())
			for (String key : type.keys)
				modifiableStrings.put(key, type);

		strings = Collections.unmodifiableMap(modifiableStrings);
	}

	private final String[] keys;
	private final long value;

	/**
	 * Constructor.<br>
	 * 
	 * @param value
	 *            The value
	 */
	DBPFExemplarTypes(long value) {
		this.value = value;
		keys = new String[0];
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param value
	 *            The value
	 * @param keys
	 *            The keys
	 */
	DBPFExemplarTypes(long value, String... keys) {
		this.value = value;
		this.keys = keys;
	}

	/**
	 * Returns the value of the exemplar type (0x00000010) property as a long.
	 * 
	 * @return The value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Returns the keys
	 * 
	 * @return The keys
	 */
	public String[] getKeys() {
		return keys;
	}

	/**
	 * Returns the ExemplarType for the given propertyID.<br>
	 * 
	 * @param propertyID
	 *            The propertyID
	 * @return The ExemplarType
	 */
	public static DBPFExemplarTypes forPropertyID(long propertyID) {
		for (DBPFExemplarTypes type : values()) {
			if (type.getValue() == propertyID) {
				return type;
			}
		}
		return OTHER;
	}

	/**
	 * Returns the type of the exemplar.<br>
	 * At first it searches for property EXEMPLAR_TYPE. If not found, it tries
	 * to find the property LOT_CONFIG_PROPERTY_LOT_OBJECT or
	 * LOT_CONFIG_PROPERTY_VERSION which indicates for LOT_CONFIGURATION.
	 * Finally it returns OTHER.
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The type
	 */
	public static DBPFExemplarTypes forExemplar(DBPFExemplar exemplar) {
		// Finally DBPFExemplarType: OTHER
		DBPFExemplarTypes type = OTHER;

		// Search for the EXEMPLAR_TYPE
		long exemplarTypeValue = DBPFUtil2.getLongValue(
				exemplar.getPropertyByID(DBPFProperties.EXEMPLAR_TYPE.getId()),
				0);
		if (exemplarTypeValue != -1) {
			// if found, return the right DBPFExemplarTypes
			type = DBPFExemplarTypes.forPropertyID(exemplarTypeValue);
		} else {
			// try to find props which indicates for LOT_CONFIGURATION
			if (isLotConfiguration(exemplar)) {
				type = LOT_CONFIGURATION;
			}
		}
		return type;
	}

	/**
	 * Checks, if the exemplar is LOT_CONFIGURATION.<br>
	 * At first it searches for property LOT_CONFIG_PROPERTY_LOT_OBJECT. If not
	 * found it searches for LOT_CONFIG_PROPERTY_VERSION. If no property found,
	 * it returns false.
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return TRUE, if the exemplar is LOT_CONFIGURATION; FALSE, otherwise
	 */
	public static boolean isLotConfiguration(DBPFExemplar exemplar) {
		DBPFProperty prop;
		prop = exemplar
				.getPropertyByID(DBPFProperties.LOT_CONFIG_PROPERTY_LOT_OBJECT
						.getId());
		if (prop == null) {
			// try another property, for LOT_CONFIGURATION
			prop = exemplar
					.getPropertyByID(DBPFProperties.LOT_CONFIG_PROPERTY_VERSION
							.getId());
		}
		if (prop != null) {
			return true;
		}
		return false;
	}
}
