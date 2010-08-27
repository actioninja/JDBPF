package ssp.dbpf4j.properties;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Represents the type of data stored in a {@link DBPFProperty}.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 25.08.2010
 */
public enum PropertyType {

	UINT8((short) 0x0100, "Uint8", 1, DBPFLongProperty.class), UINT16(
			(short) 0x0200, "Uint16", 2, DBPFLongProperty.class), UINT32(
			(short) 0x0300, "Uint32", 4, DBPFLongProperty.class), SINT32(
			(short) 0x0700, "Sint32", 4, DBPFLongProperty.class), SINT64(
			(short) 0x0800, "Sint64", 8, DBPFLongProperty.class), FLOAT32(
			(short) 0x0900, "Float32", 4, DBPFFloatProperty.class), BOOL(
			(short) 0x0B00, "Bool", 1, DBPFLongProperty.class), STRING(
			(short) 0x0C00, "String", 1, DBPFStringProperty.class);

	/** A map of data type IDs ({@link #id}) to PropertyType constants. */
	public static final Map<Short, PropertyType> forID;

	static {
		HashMap<Short, PropertyType> modifiable = new HashMap<Short, PropertyType>(
				PropertyType.values().length);

		for (PropertyType type : PropertyType.values())
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
	 * {@link AbstractDBPFProperty#AbstractDBPFProperty(long, int, PropertyType, boolean, short[], int)}
	 */
	public final Constructor<? extends DBPFProperty> propertyRawConstructor;

	/**
	 * Instantiates a DBPFProperty that stores this data type by decoding text
	 * data. It should point to an implementation of
	 * {@link AbstractDBPFProperty#AbstractDBPFProperty(long, int, PropertyType, boolean, String[])}
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
	PropertyType(short id, String name, int length,
			Class<? extends DBPFProperty> propertyClass) {
		this.id = id;
		this.name = name;
		this.length = length;
		this.propertyClass = propertyClass;

		Constructor<? extends DBPFProperty> tempPRC = null;
		try {
			tempPRC = propertyClass.getConstructor(long.class, int.class,
					PropertyType.class, boolean.class, new short[0].getClass(),
					int.class);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ex.getMessage(), ex);
		} catch (SecurityException ex) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		propertyRawConstructor = tempPRC;

		Constructor<? extends DBPFProperty> tempPTC = null;
		try {
			tempPTC = propertyClass
					.getConstructor(long.class, int.class, PropertyType.class,
							boolean.class, new String[0].getClass());
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ex.getMessage(), ex);
		} catch (SecurityException ex) {
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		propertyTextConstructor = tempPTC;
	}

	@Override
	public String toString() {
		return name;
	}
}