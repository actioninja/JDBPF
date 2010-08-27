package ssp.dbpf4j.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import ssp.dbpf4j.util.DBPFUtil;

/**
 * Represents an exemplar property type.<br>
 * Instances of this represent known exemplar types.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 26.08.2010
 */
public class ExemplarProperty {

	/** A constant representing an unknown exemplar property. */
	public static final ExemplarProperty UNKNOWN = new ExemplarProperty();

	private final long id;
	private final String name;
	private final PropertyType type;

	// public final Map<String, String> attributes;

	/**
	 * Constructor.<br>
	 * Creates the UNKNOWN ExemplarProperty
	 */
	private ExemplarProperty() {
		id = 0;
		name = "";
		type = PropertyType.STRING;
		// attributes = Collections.emptyMap();
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
	public ExemplarProperty(long id, String name, PropertyType type) {
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
	public DBPFProperty createProperty(Object values) {
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
			Logger.getLogger(DBPFUtil.LOGGER_NAME).log(Level.SEVERE,
					"UnsupportedOperationException: Can not create Property.");
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
	public PropertyType getType() {
		return type;
	}
}
