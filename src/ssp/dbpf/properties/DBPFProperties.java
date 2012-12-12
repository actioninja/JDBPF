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

import ssp.dbpf.event.DBPFException;

/**
 * Represents an exemplar property type.<br>
 * Instances of this represent known exemplar types.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 */
public class DBPFProperties {

	private static final String LOGNAME = DBPFProperties.class.getSimpleName();

	/** A constant representing an unknown exemplar property. */
	public static final DBPFProperties UNKNOWN = new DBPFProperties();

	/*
	 * Some common ExemplarProperties. Only the ID will be set!
	 */
	public static final DBPFProperties EXEMPLAR_NAME = new DBPFProperties(
			0x00000020L);
	public static final DBPFProperties EXEMPLAR_TYPE = new DBPFProperties(
			0x00000010L);
	public static final DBPFProperties BULLDOZE_COST = new DBPFProperties(
			0x099afacdL);
	public static final DBPFProperties OCCUPANT_SIZE = new DBPFProperties(
			0x27812810L);
	public static final DBPFProperties WEALTH = new DBPFProperties(0x27812832L);
	public static final DBPFProperties PURPOSE = new DBPFProperties(0x27812833L);
	public static final DBPFProperties DEMAND_SATISFIED = new DBPFProperties(
			0x27812840L);
	public static final DBPFProperties LANDMARK_EFFEKT = new DBPFProperties(
			0x2781284fL);
	public static final DBPFProperties PARK_EFFEKT = new DBPFProperties(
			0x27812850L);
	public static final DBPFProperties POLLUTION_AT_CENTER = new DBPFProperties(
			0x27812851L);
	public static final DBPFProperties POWER_CONSUMED = new DBPFProperties(
			0x27812854L);
	public static final DBPFProperties FLAMMABILITY = new DBPFProperties(
			0x29244db5L);
	public static final DBPFProperties QUERY_EXEMPLAR_GUID = new DBPFProperties(
			0x2a499f85L);
	public static final DBPFProperties MAX_FIRE_STAGE = new DBPFProperties(
			0x49beda31L);
	public static final DBPFProperties PLOP_COST = new DBPFProperties(
			0x49cac341L);
	public static final DBPFProperties CATALOG_CAPACITY = new DBPFProperties(
			0x4aa60ebcL);
	public static final DBPFProperties POLLUTION_RADII = new DBPFProperties(
			0x68ee9764L);
	public static final DBPFProperties LOT_CONFIG_PROPERTY_VERSION = new DBPFProperties(
			0x88edc789L);
	public static final DBPFProperties LOT_CONFIG_PROPERTY_SIZE = new DBPFProperties(
			0x88edc790L);
	public static final DBPFProperties LOT_CONFIG_PROPERTY_LOT_OBJECT = new DBPFProperties(
			0x88edc900L);
	public static final DBPFProperties ITEM_NAME = new DBPFProperties(
			0x899afbadL);
	public static final DBPFProperties ITEM_DESCRIPTION = new DBPFProperties(
			0x8a2602a9L);
	public static final DBPFProperties ITEM_ICON = new DBPFProperties(
			0x8a2602b8L);
	public static final DBPFProperties ITEM_ORDER = new DBPFProperties(
			0x8a2602b9L);
	public static final DBPFProperties USER_VISIBLE_NAME_KEY = new DBPFProperties(
			0x8a416a99L);
	public static final DBPFProperties OCCUPANT_GROUPS = new DBPFProperties(
			0xaa1dd396L);
	public static final DBPFProperties ITEM_DESCRIPTION_KEY = new DBPFProperties(
			0xca416ab5L);
	public static final DBPFProperties WATER_CONSUMED = new DBPFProperties(
			0xc8ed2d84L);
	public static final DBPFProperties LOT_RESOURCE_KEY = new DBPFProperties(
			0xea260589L);
	public static final DBPFProperties CONDITIONAL_BUILDING = new DBPFProperties(
			0xea3209f8L);
	public static final DBPFProperties MONTHLY_COST = new DBPFProperties(
			0xea54d286L);
	public static final DBPFProperties ITEM_BUTTON_ID = new DBPFProperties(
			0x8a2602bbL);

	private final long id;
	private final String name;
	private final DBPFPropertyTypes type;

	// public final Map<String, String> attributes;

	/**
	 * Constructor.<br>
	 * Creates the UNKNOWN ExemplarProperty
	 */
	private DBPFProperties() {
		this(0, "", DBPFPropertyTypes.STRING);
		// attributes = Collections.emptyMap();
	}

	private DBPFProperties(long id) {
		this(id, "", DBPFPropertyTypes.STRING);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param type
	 *            The type
	 */
	public DBPFProperties(long id, String name, DBPFPropertyTypes type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	/**
	 * Constructor.<br>
	 * Creates an ExemplarProperty from the given Node.
	 * 
	 * @param node
	 *            A node
	 */
	// REMARK:
	// Do not use a specific constructor, the XML file might
	// be different

	// public ExemplarProperty(Node node) {
	// xmlElement = (Element) node;
	// HashMap<String, String> modAttr = new HashMap<String, String>();
	// attributes = Collections.unmodifiableMap(modAttr);
	//
	// NamedNodeMap nodeAttrib = node.getAttributes();
	// for (int i = 0; i < nodeAttrib.getLength(); i++) {
	// modAttr.put(nodeAttrib.item(i).getNodeName(), nodeAttrib.item(i)
	// .getNodeValue());
	// }
	//
	// name = attributes.get("Name");
	//
	// String idStr = attributes.get("ID");
	// if (!idStr.startsWith("0x") || idStr.length() != 10) {
	// Logger.getLogger(DBPFUtil.LOGGER_NAME).log(
	// Level.SEVERE,
	// "NumberFormatException for ID of Exemplar property: "
	// + idStr);
	// }
	// id = Long.parseLong(attributes.get("ID").substring("0x".length()), 16);
	//
	// type = PropertyType.valueOf(attributes.get("Type").toUpperCase());
	// }

	/**
	 * Creates a DBPFProperty from the given values.<br>
	 * 
	 * @param values
	 *            The values
	 * @return The DBPFProperty or NULL, if not createable
	 */
	public DBPFProperty createProperty(Object values) throws DBPFException {
		// REMARK:
		// Do not use attributes from the Exemplar Property, they might
		// be different between various XML files

		// int count;
		// String defaultVal;
		// boolean hasCount;
		// String countStr = attributes.get("Count");
		// hasCount = (countStr == null);
		// count = hasCount ? Integer.parseInt(countStr) : 1;
		// if (values == null) {
		// defaultVal = attributes.get("Default");
		// if (type.propertyClass.equals(DBPFLongProperty.class)) {
		// values = new long[count];
		// Arrays.fill((long[]) values, Long.decode(defaultVal));
		// } else if (type.propertyClass.equals(DBPFFloatProperty.class)) {
		// values = new float[count];
		// Arrays.fill((float[]) values, Float.parseFloat(defaultVal));
		// } else if (type.propertyClass.equals(DBPFStringProperty.class)) {
		// values = defaultVal;
		// }
		// }

		DBPFProperty prop = null;
		if (type.propertyClass.equals(DBPFLongProperty.class)) {
			prop = new DBPFLongProperty(id, type, (long[]) values);
		} else if (type.propertyClass.equals(DBPFFloatProperty.class)) {
			prop = new DBPFFloatProperty(id, type, (float[]) values);
		} else if (type.propertyClass.equals(DBPFStringProperty.class)) {
			prop = new DBPFStringProperty(id, type, (String) values);
		} else {
			throw new DBPFException(LOGNAME,
					"UnsupportedOperationException: Can not create Property for: "
							+ values);
		}
		return prop;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public DBPFPropertyTypes getType() {
		return type;
	}
}
