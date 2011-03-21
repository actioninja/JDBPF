package ssp.dbpf4j.types;

import ssp.dbpf4j.format.DBPFCoder;
import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.tgi.TGIKey;
import ssp.dbpf4j.tgi.TGIKeys;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines an exemplar of the DBPF.<br>
 * 
 * The exemplar stores basic information about the lot.<br>
 * The decompressedSize will be updated with setPropertyList.
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 28.12.2010
 * 
 */
public class DBPFCohort extends AbstractDBPFType {

	private TGIKey tgiCohort;

	private short format;

	private DBPFProperty[] propertyList;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFCohort() {
		tgiCohort = new TGIKey();
		propertyList = new DBPFProperty[0];
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param exemplar
	 *            The exemplar
	 */
	public DBPFCohort(DBPFExemplar exemplar) {
		setCompressed(exemplar.isCompressed());
		setDecompressedSize(exemplar.getDecompressedSize());
		setTGIKey(exemplar.getTGIKey());
		setTGICohort(exemplar.getTGICohort());
		setFormat(exemplar.getFormat());
		setPropertyList(exemplar.getPropertyList());
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
		setDecompressedSize(DBPFCoder.calcCohortDataLength(this));
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
		return TGIKeys.COHORT.getFormatID();
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
