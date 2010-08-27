package ssp.dbpf4j.types;

/**
 * Defines a S3D of DBPF.<br>
 * 
 * Only stores the rawData like DBPFRaw.
 * The decompressedSize will be updated
 * with setData.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFS3D extends AbstractDBPFType {

	private short[] rawData;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(", ");
		sb.append("Data-Size: " + rawData.length);
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
		setDecompressedSize(rawData.length);
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
		return DBPFTypes.RAW;
	}
}
