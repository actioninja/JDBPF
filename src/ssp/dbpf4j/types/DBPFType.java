package ssp.dbpf4j.types;

/**
 * Defines an interface for all DBPFTypes.<br>
 * 
 * @author Stefan Wertich
 * @version 1.5.0, 24.08.2010
 * 
 */
public interface DBPFType {

	/**
	 * @param decompressedSize the uncompressedSize to set
	 */
	public void setDecompressedSize(long decompressedSize);

	/**
	 * @return the uncompressedSize
	 */
	public long getDecompressedSize();
	
	/**
	 * This flag signalize, if the file should be compressed.<br>
	 * 
	 * @param compressed
	 *            set the compressed
	 */
	public void setCompressed(boolean compressed);

	/**
	 * 
	 * @return TRUE, if compressed; FALSE, otherwise
	 */
	public boolean isCompressed();

	/**
	 * Gives detail information about type.<br>
	 * Consists of toString() and more information.
	 * 
	 * @return A detail string
	 */
	public String toDetailString();

	/**
	 * Returns the specific type of this type.<br>
	 * 
	 * @return The type
	 */
	public int getType();

	/**
	 * Sets the TGI of this type.<br>
	 * Normally from entry.
	 * 
	 * @param tgi
	 *            The TGI
	 */
	public void setTGI(long[] tgi);

	/**
	 * Returns the TGI.<br>
	 * 
	 * @return The TGI
	 */
	public long[] getTGI();
	
	/**
	 * Returns the TypeID.<br>
	 * 
	 * @return The typeID
	 */
	public long getTID();

	/**
	 * Returns the GroupID.<br>
	 * 
	 * @return The groupID
	 */
	public long getGID();

	/**
	 * Returns the InstanceID.<br>
	 * 
	 * @return The instanceID
	 */
	public long getIID();

}
