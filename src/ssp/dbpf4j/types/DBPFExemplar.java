package ssp.dbpf4j.types;

import ssp.dbpf4j.properties.DBPFProperty;
import ssp.dbpf4j.util.DBPFCoder;
import ssp.dbpf4j.util.DBPFUtil;

/**
 * Defines an exemplar of the DBPF.<br>
 * 
 * The exemplar stores basic information about the lot.<br>
 * The decompressedSize will be updated with setPropertyList.
 * 
 * @author Stefan Wertich
 * @version 0.9.1.27, 27.01.2009
 * 
 */
public class DBPFExemplar implements DBPFType {

	private long[] tgi;

	private long cohortT = 0;
	private long cohortG = 0;
	private long cohortI = 0;
	
	private short format;
	
	private boolean compressed;
	private long decompressedSize;

	private DBPFProperty[] propertyList;

	/**
	 * Constructor.<br>
	 * 
	 */
	public DBPFExemplar() {
		propertyList = new DBPFProperty[0];
		tgi = new long[] { 0x0, 0x0, 0x0 };
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TGI: ");
		sb.append(DBPFUtil.toHex(tgi[0], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[1], 8));
		sb.append(",");
		sb.append(DBPFUtil.toHex(tgi[2], 8));
		sb.append(",");
		sb.append("Compressed: ");
		sb.append(compressed);
		sb.append(", ");
		sb.append("Property-Size: " + propertyList.length);
		sb.append("\n");
		sb.append("DecompressedSize: "+decompressedSize);		
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
	
	public String toDetailString() {
		StringBuffer sb = new StringBuffer(toString());
		if (propertyList.length > 0) {
			sb.append("\n");
			for (int i = 0; i < propertyList.length; i++) {
				sb.append(propertyList[i].toString());
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

	@Override
	public long[] getTGI() {
		return tgi;
	}

	@Override
	public int getType() {
		return DBPFTypes.EXEMPLAR;
	}

	@Override
	public void setTGI(long[] tgi) {
		this.tgi = tgi;
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
	 * @param format the format to set
	 */
	public void setFormat(short format) {
		this.format = format;
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	@Override
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;		
	}
	
	@Override
	public long getDecompressedSize() {
		return decompressedSize;
	}

	@Override
	public void setDecompressedSize(long decompressedSize) {
		this.decompressedSize = decompressedSize;
	}
}
