package ssp.dbpf4j.types;

/**
 * The types.<br>
 * 
 * @author Stefan Wertich
 * @version 1.2.0, 20.08.2009
 * 
 */
public interface DBPFTypes {

	/**
	 * Unknown data as rawData
	 */
	public static final int RAW = 0;
	
	/**
	 * Directory file
	 */
	public static final int DIRECTORY = 1;

	/**
	 * LD file
	 */
	public static final int LD = 2;

	/**
	 * Exemplar file: LotInfo, LotConfig
	 */
	public static final int EXEMPLAR = 3;

	/**
	 * PNG file: PNG image, PNG icon
	 */
	public static final int PNG = 11;
	
	/**
	 * PNG file: PNG icon
	 */
	public static final int PNG_ICON = 12;

	/**
	 * FSH
	 */
	public static final int FSH = 13;
	
	/**
	 * S3D
	 */
	public static final int S3D = 14;
	
	/**
	 * LUA file
	 */
	public static final int LUA = 21;

	/**
	 * RUL
	 */
	public static final int RUL = 22;
	
	/**
	 * LTEXT
	 */
	public static final int LTEXT = 23;
	
	/**
	 * WAV
	 */
	public static final int WAV = 31;
	
}
