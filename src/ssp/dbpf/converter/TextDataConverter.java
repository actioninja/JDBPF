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

import java.util.List;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between text data and exemplar type.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 *
 */
public class TextDataConverter {
	
	/**
	 * Private Constructor prevents instance.<br>
	 */
	private TextDataConverter() {
	}

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * The data is in the Text-format (0x54).
	 * 
	 * @param dData
	 *            The decompressed data
	 * @throws DBPFException Thrown, if exemplar cannot be created
	 */
	public static DBPFExemplar createExemplar(short[] dData) throws DBPFException {
		DBPFExemplar exemplar = new DBPFExemplar();
		// Read all lines
		List<String> lines = DBPFUtil.getLines(dData, 0x08);
		// for (String string : lines) {
		// System.out.println(string);
		// }
		int start = 0;
		while (!lines.get(start).contains("=")) {
			start++;
		}
		// Get cohort from first line
		String[] nameValue = lines.get(start).split("=");
		String[] value = nameValue[1].split(":");
		String[] tgi = value[1].split(",");
		exemplar.setCohortT(Long.decode(tgi[0].substring(1)));
		exemplar.setCohortG(Long.decode(tgi[1]));
		exemplar.setCohortI(Long.decode(tgi[2].substring(0, 10)));

		// Get propCount from second line
		nameValue = lines.get(start + 1).split("=");
		int propCount = Integer.decode(nameValue[1]);
		DBPFProperty[] propertyList = new DBPFProperty[propCount];
		for (int i = 0; i < propertyList.length; i++) {
			DBPFProperty prop = PropertyConverter.createProperty(lines
					.get(start + 2 + i));
			propertyList[i] = prop;
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFConstant.FORMAT_TEXT);
		return exemplar;
	}

	/**
	 * Create the data for the given exemplar.<br>
	 * 
	 * The data is in the Text-format (0x54).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The data
	 * @throws DBPFException Thrown, if data cannot be created
	 */
	public static short[] createData(DBPFExemplar exemplar) throws DBPFException {
		StringBuilder sb = new StringBuilder();
		// Header
		sb.append(DBPFConstant.MAGICNUMBER_EQZ);
		sb.append("T1###");
		sb.append(DBPFConstant.CRLF);
		// Parent Cohort Key
		sb.append("ParentCohort=Key:{0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortT(), 8));
		sb.append(",0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortG(), 8));
		sb.append(",0x");
		sb.append(DBPFUtil.toHex(exemplar.getCohortI(), 8));
		sb.append("}");
		sb.append(DBPFConstant.CRLF);
		// PropCount
		DBPFProperty[] propList = exemplar.getPropertyList();
		sb.append("PropCount=0x");
		sb.append(DBPFUtil.toHex(propList.length, 8));
		sb.append(DBPFConstant.CRLF);
		// Propertys
		for (int i = 0; i < propList.length; i++) {
			sb.append(propList[i].toText());
			sb.append(DBPFConstant.CRLF);
		}
		short[] data = new short[sb.length()];
		DBPFUtil.setChars(sb.toString(), data, 0);
		return data;
	}
}
