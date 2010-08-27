package ssp.dbpf4j.types;

import ssp.dbpf4j.format.DBPFCoder;
import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines an exemplar of the DBPF.<br>
 * 
 * The exemplar stores basic information about the lot.<br>
 * The decompressedSize will be updated with setPropertyList.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFExemplar extends AbstractDBPFType {

	private long cohortT = 0;
	private long cohortG = 0;
	private long cohortI = 0;

	private short format;

	private DBPFProperty[] propertyList;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFExemplar() {
		propertyList = new DBPFProperty[0];
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Property-Size: " + propertyList.length);
		sb.append("\n");
		sb.append("Format: ");
		sb.append(DBPFUtil.getExemplarFormat(format));
		sb.append(", ");
		sb.append("Cohort TGI: ");
		sb.append(DBPFUtil.toHex(cohortT, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(cohortG, 8));
		sb.append(" ");
		sb.append(DBPFUtil.toHex(cohortI, 8));
		return sb.toString();
	}

	@Override
	public String toDetailString() {
		StringBuffer sb = new StringBuffer(toString());
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
		setDecompressedSize(DBPFCoder.calcExemplarDataLength(this));
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
		return DBPFTypes.EXEMPLAR;
	}

	/**
	 * @return the cohortT
	 */
	public long getCohortT() {
		return cohortT;
	}

	/**
	 * @param cohortT
	 *            the cohortT to set
	 */
	public void setCohortT(long cohortT) {
		this.cohortT = cohortT;
	}

	/**
	 * @return the cohortG
	 */
	public long getCohortG() {
		return cohortG;
	}

	/**
	 * @param cohortG
	 *            the cohortG to set
	 */
	public void setCohortG(long cohortG) {
		this.cohortG = cohortG;
	}

	/**
	 * @return the cohortI
	 */
	public long getCohortI() {
		return cohortI;
	}

	/**
	 * @param cohortI
	 *            the cohortI to set
	 */
	public void setCohortI(long cohortI) {
		this.cohortI = cohortI;
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
