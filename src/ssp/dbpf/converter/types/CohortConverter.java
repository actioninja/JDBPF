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
package ssp.dbpf.converter.types;

import ssp.dbpf.converter.BinaryDataConverter;
import ssp.dbpf.converter.TextDataConverter;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFCohort;
import ssp.dbpf.types.DBPFExemplar;
import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFUtil;

/**
 * Converter between Cohort data and Cohort type.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class CohortConverter {

	/**
	 * Private Constructor prevents instance.<br>
	 */
	private CohortConverter() {
	}

	/**
	 * Creates an exemplar from the given data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The exemplar or NULL, if data is no cohort
	 * @throws DBPFException
	 *             Thrown, if cannot create exemplar
	 */
	public static DBPFCohort createType(short[] dData) throws DBPFException {
		String fileType = DBPFUtil.getChars(dData, 0x00, 3);
		if (fileType.equals(DBPFConstant.MAGICNUMBER_CQZ)) {
			long format = DBPFUtil.getUint32(dData, 0x03, 1);
			@SuppressWarnings("unused")
			long unknown1 = DBPFUtil.getUint32(dData, 0x04, 1);
			@SuppressWarnings("unused")
			long unknown2 = DBPFUtil.getUint32(dData, 0x05, 3);

			DBPFExemplar exemplar = null;
			// B-Format
			if (format == DBPFConstant.FORMAT_BINARY) {
				exemplar = BinaryDataConverter.createExemplar(dData);
			}
			// T-Format
			else if (format == DBPFConstant.FORMAT_TEXT) {
				exemplar = TextDataConverter.createExemplar(dData);
			}
			return new DBPFCohort(exemplar);
		}
		return null;
	}

	/**
	 * Create the data for the given cohort.<br>
	 * 
	 * @param cohort
	 *            The cohort
	 * @param format
	 *            DBPFUtil.MAGICNUMBER_B_FORMAT or T_FORMAT
	 * @return The data or NULL, if data is whether BINARY nor TEXT format
	 * @throws DBPFException
	 *             Thrown, if data cannot be created
	 */
	public static short[] createData(DBPFCohort cohort, short format)
			throws DBPFException {
		if (format == DBPFConstant.FORMAT_BINARY) {
			short[] data = BinaryDataConverter.createData(new DBPFExemplar(
					cohort));
			DBPFUtil.setChars(DBPFConstant.MAGICNUMBER_CQZ, data, 0x00);
			return data;
		} else if (format == DBPFConstant.FORMAT_TEXT) {
			short[] data = TextDataConverter
					.createData(new DBPFExemplar(cohort));
			DBPFUtil.setChars(DBPFConstant.MAGICNUMBER_CQZ, data, 0x00);
			return data;
		}
		return null;
	}

	/**
	 * Returns the data length for the given exemplar after calculating.<br>
	 * 
	 * The length is for the Binary-format (0x42).
	 * 
	 * @param cohort
	 *            The Cohort
	 * @return The length in bytes
	 */
	public static int calcDataLength(DBPFCohort cohort) {
		int dataLength = 0x18;
		DBPFProperty[] propList = cohort.getPropertyList();
		for (DBPFProperty prop : propList) {
			// Updates the hasCount
			if (prop.getCount() > 1) {
				// if more than one value
				prop.setHasCount(true);
			} else if (!prop.hasCount()) {
				// if hasCount not already set
				prop.setHasCount(false);
			}
			dataLength += prop.getBinaryLength();
		}
		return dataLength;
	}
}
