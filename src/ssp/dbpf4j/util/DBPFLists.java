package ssp.dbpf4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Stores some DBPF lists for various things.<br>
 * 
 * Before using any list, call loadLists to initialize and fill them.
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 03.01.2011
 * 
 */
public class DBPFLists {

	/**
	 * The Occupant Groups, e.g. 0x1400 = Power (Menu)
	 */
	public static LinkedHashMap<Long, String> occupantGroups = new LinkedHashMap<Long, String>();

	/**
	 * The Occupant Types, e.g. 0x1010 = R$
	 */
	public static LinkedHashMap<Long, String> occupantTypes = new LinkedHashMap<Long, String>();

	/**
	 * The Purposes, e.g. 0x1 = R
	 */
	public static LinkedHashMap<Long, String> purposes = new LinkedHashMap<Long, String>();

	/**
	 * The LotConfigData, e.g. 0x00000001 = Prop
	 */
	public static LinkedHashMap<Long, String> lotconfigData = new LinkedHashMap<Long, String>();

	/**
	 * The LotConfigZones, e.g. 0x02 = Medium Res
	 */
	public static LinkedHashMap<Long, String> lotconfigZones = new LinkedHashMap<Long, String>();

	/**
	 * Loads the lists.<br>
	 * 
	 * @return TRUE, if loaded successful; FALSE, otherwise
	 */
	public static boolean loadLists() {
		occupantGroups = load("occupantGroups");
		occupantTypes = load("occupantTypes");
		purposes = load("purposes");
		lotconfigData = load("lotconfigdata");
		lotconfigZones = load("lotconfigzones");

		return true;
	}

	/**
	 * Reads the array for the given xmlTag from the file.<br>
	 * 
	 * The file will be: /resources/dbpflists.xml
	 * 
	 * @param xmlTag
	 *            The xmlTag inside the XML file
	 */
	private static LinkedHashMap<Long, String> load(String xmlTag) {
		LinkedHashMap<Long, String> list = new LinkedHashMap<Long, String>();
		String resourceName = "/resources/dbpflists.xml";
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream is = DBPFLists.class.getResourceAsStream(resourceName);
			org.w3c.dom.Document properties = builder.parse(is);

			NodeList nodeList = properties.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nExemplar = nodeList.item(i);
				if (nExemplar.getNodeName().equalsIgnoreCase("exemplars")) {
					NodeList typeList = nExemplar.getChildNodes();
					for (int j = 0; j < typeList.getLength(); j++) {
						Node nType = typeList.item(j);
						if (nType.getNodeName().equalsIgnoreCase(xmlTag)) {
							NodeList groupList = nType.getChildNodes();
							for (int k = 0; k < groupList.getLength(); k++) {
								setGroup(groupList.item(k), list);
							}
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			System.err.println("<ERROR> ParserConfigurationException: "
					+ e.getMessage());
		} catch (SAXException e) {
			System.err.println("<ERROR> SAXException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("<ERROR> IOException: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Sets the group to the list.<br>
	 * 
	 * @param list
	 *            The list
	 * @param group
	 *            The group
	 */
	private static void setGroup(Node group, LinkedHashMap<Long, String> list) {
		if (group.getNodeName().equalsIgnoreCase("group")) {
			NodeList props = group.getChildNodes();
			for (int i = 0; i < props.getLength(); i++) {
				Node prop = props.item(i);
				if (prop.getNodeName().equalsIgnoreCase("group")) {
					setGroup(prop, list);
				} else {
					setProp(prop, list);
				}
			}
		}
	}

	/**
	 * Sets the props to the list.<br>
	 * 
	 * @param list
	 *            The list
	 * @param prop
	 *            The property
	 */
	private static void setProp(Node prop, LinkedHashMap<Long, String> list) {
		if (prop.getNodeName().equalsIgnoreCase("property")) {
			NamedNodeMap map = prop.getAttributes();
			Node nNum = map.getNamedItem("num");
			Node nName = map.getNamedItem("name");
			long key = Long.decode(nNum.getNodeValue());
			String name = nName.getNodeValue();
			// System.out.println(DBPFUtil.toHex(key,8) + "," + name);
			list.put(key, name);
		}
	}

	/**
	 * Returns the value for the given key.<br>
	 * 
	 * @param list
	 *            The list to search for the key
	 * @param key
	 *            The key
	 * @return The value or NULL, if not found
	 */
	public static String get(LinkedHashMap<Long, String> list, long key) {
		return list.get(key);
	}

	/**
	 * Returns the first key, which has the given value.<br>
	 * 
	 * @param list
	 *            The list to search for the value
	 * @param value
	 *            The value
	 * @return The key or -1, if not found
	 */
	public static long getKey(LinkedHashMap<Long, String> list, String value) {
		long key = -1;
		if (list.containsValue(value)) {
			Iterator<Long> itera = list.keySet().iterator();
			while (key == -1 && itera.hasNext()) {
				long keyTemp = itera.next();
				String elem = list.get(keyTemp);
				if (elem.equals(value)) {
					key = keyTemp;
				}
			}
		}
		return key;
	}

	/**
	 * Converts the list with the values to one string.<br>
	 * 
	 * Uses the list to get the string for the keys.
	 * 
	 * @param list
	 *            The list to search for the keys
	 * @param keyList
	 *            The list with the keys
	 * @return A string separated by comma
	 */
	public static String getString(LinkedHashMap<Long, String> list,
			Vector<Long> keyList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyList.size(); i++) {
			Long key = keyList.get(i);
			String s = list.get(key);
			if (s != null) {
				sb.append(s);
			} else {
				sb.append(DBPFUtil.toHex(key, 4));
			}
			if (i < keyList.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
