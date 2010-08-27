package ssp.dbpf4j.types;

/**
 * Defines a PNG of DBPF.<br>
 * 
 * The PNG is a open image format and the data stored as imageData.<br>
 * The decompressedSize is updated with setImageData. Although the imageData
 * might be compressed, it could be compressed with QFS.
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public class DBPFPNG extends AbstractDBPFType {

	private short[] imageData;

	/**
	 * Constructor.<br>
	 */
	public DBPFPNG() {
		imageData = new short[0];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(", ");
		sb.append("ImageData-Size: " + imageData.length);
		return sb.toString();
	}

	/**
	 * Sets the imageData of the PNG.<br>
	 * 
	 * @param imageData
	 *            The data
	 */
	public void setImageData(short[] imageData) {
		this.imageData = imageData;
		setDecompressedSize(imageData.length);
	}

	/**
	 * Returns the imageData of the PNG.<br>
	 * 
	 * @return The data
	 */
	public short[] getImageData() {
		return imageData;
	}

	@Override
	public int getType() {
		return DBPFTypes.PNG;
	}
}
