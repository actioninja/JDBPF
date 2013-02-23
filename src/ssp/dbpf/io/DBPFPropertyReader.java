package ssp.dbpf.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.properties.DBPFProperties;
import ssp.dbpf.properties.DBPFPropertyTypes;
import ssp.dbpf.util.DBPFLogger;

/**
 * Reads the properties from the SC4PIM Extended XML file.<br>
 * 
 * Format:<br>
 * &lt;ExemplarProperties&gt;<br>
 * &lt;PROPERTIES&gt;<br>
 * &lt;PROPERTY ID="0x00000000" Name="MiscType1" Type="Uint32" ShowAsHex="N"&gt;<br>
 * &lt;HELP&gt;Function, values, &amp; DataType: varies from Exemplar file to
 * Exemplar file&lt;/HELP&gt;<br>
 * &lt;OPTION Value="0x00000000" Name="Other/Unknown"/&gt;
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 23.02.2013
 * 
 */
public class DBPFPropertyReader {

	private static final String LOGNAME = DBPFPropertyReader.class
			.getSimpleName();

	private static final File PROP_FILE = new File(
			"resources/properties/properties.xml");

	/**
	 * Reads the properties from default xml file.<br>
	 * 
	 * @return The list with DBPFProperties
	 * @throws DBPFException
	 *             Thrown, if error occur
	 */
	public static List<DBPFProperties> readProperties() throws DBPFException {
		// Try internal property file
		InputStream is = DBPFPropertyReader.class.getResourceAsStream("/"
				+ PROP_FILE);

		// Try external property file
		if (is == null) {
			try {
				is = new FileInputStream(PROP_FILE);
			} catch (FileNotFoundException e) {
				is = null;
				// ignore this
			}
		}

		// Load from inputstream
		if (is != null) {
			return readProperties(is);
		}
		throw new DBPFException(LOGNAME, "Can not read properties from: "
				+ PROP_FILE);
	}

	/**
	 * Reads the properties from the given inputstream.<br>
	 * 
	 * @param is
	 *            The inputstream
	 * @return The list with DBPFProperties
	 * @throws DBPFException
	 *             Thrown, if error occur
	 */
	public static List<DBPFProperties> readProperties(InputStream is)
			throws DBPFException {
		List<DBPFProperties> props = new ArrayList<DBPFProperties>();
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document propDoc = builder.parse(is);
			readXMLFormatB(props, propDoc);

		} catch (ParserConfigurationException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (SAXException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		} catch (IOException e) {
			throw new DBPFException(LOGNAME, e.getMessage());
		}
		return props;
	}

	/**
	 * Reads the properties from a XML file used by SC4PIM (XML_FORMAT_PIM).<br>
	 * File: new_properties.xml
	 * 
	 * Format:<br>
	 * &lt;ExemplarProperties&gt;<br>
	 * &lt;PROPERTIES&gt;<br>
	 * &lt;PROPERTY ID="0x00000000" Name="MiscType1" Type="Uint32"
	 * ShowAsHex="N"&gt;<br>
	 * &lt;HELP&gt;Function, values, &amp; DataType: varies from Exemplar file
	 * to Exemplar file&lt;/HELP&gt;<br>
	 * &lt;OPTION Value="0x00000000" Name="Other/Unknown"/&gt;
	 * 
	 */
	private static void readXMLFormatB(List<DBPFProperties> props,
			Document propDoc) {
		NodeList nodeList = propDoc.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nExemplar = nodeList.item(i);
			if (nExemplar.getNodeName().equalsIgnoreCase("ExemplarProperties")) {
				NodeList typeList = nExemplar.getChildNodes();
				for (int j = 0; j < typeList.getLength(); j++) {
					Node nType = typeList.item(j);
					if (nType.getNodeName().equalsIgnoreCase("PROPERTIES")) {
						NodeList propList = nType.getChildNodes();
						for (int k = 0; k < propList.getLength(); k++) {
							setPropB(props, propList.item(k));
						}
					}
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
	private static void setPropB(List<DBPFProperties> props, Node prop) {
		if (prop.getNodeName().equalsIgnoreCase("PROPERTY")) {
			NamedNodeMap map = prop.getAttributes();
			Node nID = map.getNamedItem("ID");
			Node nName = map.getNamedItem("Name");
			Node nType = map.getNamedItem("Type");
			if (nID != null && nName != null && nType != null) {
				String idStr = nID.getNodeValue();
				if (!idStr.startsWith("0x") || idStr.length() != 10) {
					DBPFLogger.toLog(LOGNAME, Level.WARNING,
							"NumberFormatException for ID of Exemplar property: "
									+ idStr + ". Ignore these property!");
				} else {
					long id = Long.decode(idStr);
					String name = nName.getNodeValue().trim();
					String sType = nType.getNodeValue().trim().toUpperCase();
					// System.out.println(DBPFUtil.toHex(id,8) + "," + name);
					DBPFPropertyTypes type = DBPFPropertyTypes.valueOf(sType);
					DBPFProperties exemProp = new DBPFProperties(id, name, type);
					props.add(exemProp);
				}
			} else {
				DBPFLogger.toLog(
						LOGNAME,
						Level.WARNING,
						"Can not analyze Exemplar property: "
								+ prop.getNodeName());
			}
		}
	}

}
