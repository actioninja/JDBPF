package ssp.dbpf4j.types;

import ssp.dbpf4j.tgi.TGIKeys;

/**
 * Defines a directory of the DBPF.<br>
 * 
 * This class stores the information about the compressed files.<br>
 * The decompressedSize is updated with setData. The directory should never be
 * compressed!
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFDirectory extends AbstractDBPFType {

	private short[] rawData;

	/**
	 * Constructor.<br>
	 */
	public DBPFDirectory() {
		rawData = new short[0];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n");
		sb.append("RawData-Size: " + rawData.length);
		return sb.toString();
	}

	/**
	 * Sets the data of the raw type.<br>
	 * This data are normally the rawData.
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(short[] data) {
		this.rawData = data;
		this.decompressedSize = rawData.length;
	}

	/**
	 * Returns the data of the raw type.<br>
	 * This data is equivalent to the rawData.
	 * 
	 * @return The data
	 */
	public short[] getData() {
		return rawData;
	}

	@Override
	public int getType() {
		return TGIKeys.DIRECTORY.getFormatID();
	}

}
