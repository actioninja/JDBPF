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

import ssp.dbpf.converter.types.ExemplarConverter;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between binary data and exemplar type.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class BinaryDataConverter {
	
	/**
	 * Private Constructor prevents instance.<br>
	 */
	private BinaryDataConverter() {
	}

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * The data is in the Binary-format (0x42).<br>
	 * The header is EQZB1###.
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The exemplar
	 * @throws DBPFException Thrown, if exemplar cannot be created 
	 */
	public static DBPFExemplar createExemplar(short[] dData) throws DBPFException {
		DBPFExemplar exemplar = new DBPFExemplar();
		// Reading the exemplars TGI
		exemplar.setCohortT(DBPFUtil.getUint32(dData, 0x08, 4));
		exemplar.setCohortG(DBPFUtil.getUint32(dData, 0x0C, 4));
		exemplar.setCohortI(DBPFUtil.getUint32(dData, 0x10, 4));

		// Reading the properties
		int propCount = (int) DBPFUtil.getUint32(dData, 0x14, 4);
		DBPFProperty[] propertyList = new DBPFProperty[propCount];
		// Size of header
		int pos = 0x18;
		for (int i = 0; i < propCount; i++) {
			DBPFProperty prop = PropertyConverter.createProperty(dData, pos);
			propertyList[i] = prop;
			pos += prop.getBinaryLength();
		}
		exemplar.setPropertyList(propertyList);
		exemplar.setFormat(DBPFConstant.FORMAT_BINARY);
		return exemplar;
	}

	/**
	 * Create the data for the given exemplar.<br>
	 * 
	 * The data is in the Binary-format (0x42).
	 * 
	 * @param exemplar
	 *            The exemplar
	 * @return The data
	 */
	public static short[] createData(DBPFExemplar exemplar) {
		DBPFProperty[] propList = exemplar.getPropertyList();
		int length = ExemplarConverter.calcDataLength(exemplar);
		short[] data = new short[length];
		DBPFUtil.setChars(DBPFConstant.MAGICNUMBER_EQZ, data, 0x00);
		DBPFUtil.setUint32(DBPFConstant.FORMAT_BINARY, data, 0x03, 1);
		long unknown1 = 0x31;
		DBPFUtil.setUint32(unknown1, data, 0x04, 1);
		long unknown2 = 0x232323;
		DBPFUtil.setUint32(unknown2, data, 0x05, 3);
		DBPFUtil.setUint32(exemplar.getCohortT(), data, 0x08, 4);
		DBPFUtil.setUint32(exemplar.getCohortG(), data, 0x0c, 4);
		DBPFUtil.setUint32(exemplar.getCohortI(), data, 0x10, 4);
		DBPFUtil.setUint32(propList.length, data, 0x14, 4);
		int pos = 0x18;
		for (DBPFProperty prop : propList) {
			short[] pdata = prop.toRaw();
			for (int i = 0; i < pdata.length; i++) {
				data[pos + i] = pdata[i];
			}
			pos += pdata.length;
		}
		return data;
	}
}
