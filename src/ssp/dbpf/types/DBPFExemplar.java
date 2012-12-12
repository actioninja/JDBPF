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

import ssp.dbpf.converter.types.ExemplarConverter;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.tgi.TGIKey;
import ssp.dbpf.tgi.TGIKeys;
import ssp.dbpf.util.DBPFUtil;

/**
 * Defines an exemplar of the DBPF.<br>
 * 
 * The exemplar stores basic information about the lot.<br>
 * The decompressedSize will be updated with setPropertyList.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 * 
 */
public class DBPFExemplar extends AbstractDBPFType {

	private TGIKey tgiCohort;

	private short format;

	private DBPFProperty[] propertyList;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFExemplar() {
		tgiCohort = new TGIKey();
		propertyList = new DBPFProperty[0];
	}
	
	/**
	 * Constructor.<br>
	 * @param cohort The cohort
	 */
	public DBPFExemplar(DBPFCohort cohort) {
		setCompressed(cohort.isCompressed());
		setDecompressedSize(cohort.getDecompressedSize());
		setTGIKey(cohort.getTGIKey());
		setTGICohort(cohort.getTGICohort());
		setFormat(cohort.getFormat());
		setPropertyList(cohort.getPropertyList());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("Format: ");
		sb.append(DBPFUtil.getExemplarFormat(format));
		sb.append(", ");
		sb.append("Cohort TGI: ");
		sb.append(tgiCohort.toString());
		sb.append(", ");
		sb.append("Property-Size: " + propertyList.length);
		sb.append("\n");
		if (propertyList.length > 0) {
			sb.append("\n");
			for (int i = 0; i < propertyList.length; i++) {
				sb.append(propertyList[i].toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}


	/**
	 * @return the propertyList
	 */
	public DBPFProperty[] getPropertyList() {
		return propertyList;
	}

	/**
	 * @param propertyList
	 *            the propertyList to set
	 */
	public void setPropertyList(DBPFProperty[] propertyList) {
		this.propertyList = propertyList;
		setDecompressedSize(ExemplarConverter.calcDataLength(this));
	}

	/**
	 * Returns the property for the given propertyID.<br>
	 * 
	 * @param propertyID
	 *            The propertyID/nameValue
	 * @return The property or NULL, if not found
	 */
	public DBPFProperty getPropertyByID(long propertyID) {
		DBPFProperty prop;
		for (int i = 0; i < propertyList.length; i++) {
			prop = propertyList[i];
			if (prop.getID() == propertyID) {
				return prop;
			}
		}
		return null;
	}

	@Override
	public int getType() {
		return TGIKeys.EXEMPLAR.getFormatID();
	}

	/**
	 * Returns the TGICohort Key.<br>
	 * 
	 * @return The TGICohort Key
	 */
	public TGIKey getTGICohort() {
		return tgiCohort;
	}

	/**
	 * Sets the TGI Cohort key.<br>
	 * 
	 * @param tgiCohort
	 *            The TGI cohort
	 */
	public void setTGICohort(TGIKey tgiCohort) {
		this.tgiCohort = tgiCohort;
	}

	/**
	 * @return the cohortT
	 */
	public long getCohortT() {
		return tgiCohort.getTID();
	}

	/**
	 * @param cohortT
	 *            the cohortT to set
	 */
	public void setCohortT(long cohortT) {
		tgiCohort.setTID(cohortT);
	}

	/**
	 * @return the cohortG
	 */
	public long getCohortG() {
		return tgiCohort.getGID();
	}

	/**
	 * @param cohortG
	 *            the cohortG to set
	 */
	public void setCohortG(long cohortG) {
		this.tgiCohort.setGID(cohortG);
	}

	/**
	 * @return the cohortI
	 */
	public long getCohortI() {
		return tgiCohort.getIID();
	}

	/**
	 * @param cohortI
	 *            the cohortI to set
	 */
	public void setCohortI(long cohortI) {
		tgiCohort.setIID(cohortI);
	}

	/**
	 * @return the format
	 */
	public short getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(short format) {
		this.format = format;
	}

}
