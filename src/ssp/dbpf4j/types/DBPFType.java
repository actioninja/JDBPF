package ssp.dbpf4j.types;

import ssp.dbpf4j.tgi.TGIKey;

/**
 * Defines an interface for all DBPFTypes.<br>
 * <br>
 * The following types are currently supported:<br>
 * <ol>
 * <li>EQZB1 = Binary format</li>
 * <li>EQZT1 = Text format</li>
 * </ol>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 29.12.2010
 * 
 */
public interface DBPFType {

	/**
	 * @param decompressedSize
	 *            the uncompressedSize to set
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
	 * Returns the specific type of this type.<br>
	 * 
	 * @return The type
	 */
	public int getType();

	/**
	 * Sets the TGI of this type.<br>
	 * 
	 * @param tgiKey
	 *            The TGIKey
	 */
	public void setTGIKey(TGIKey tgiKey);

	/**
	 * Returns the TGI of this type.<br>
	 * 
	 * @return The TGI
	 */
	public TGIKey getTGIKey();
	
	/**
	 * Returns the TID.<br>
	 * 
	 * @return The TID
	 */
	public long getTID();

	/**
	 * Returns the GID.<br>
	 * 
	 * @return The GID
	 */
	public long getGID();

	/**
	 * Returns the IID.<br>
	 * 
	 * @return The IID
	 */
	public long getIID();

}
