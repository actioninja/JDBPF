package ssp.dbpf4j.util;

/**
 * Defines DBPF constants.<br>
 * 
 * @author Stefan Wertich
 * @version 1.6.0, 07.01.2011
 * 
 */
public interface DBPFConstant {

	/**
	 * Magic number for all DBPF files
	 */
	public static final String MAGICNUMBER_DBPF = "DBPF";

	/**
	 * Magic number for Exemplar files
	 */
	public static final String MAGICNUMBER_EQZ = "EQZ";

	/**
	 * Magic number for Cohort files
	 */
	public static final String MAGICNUMBER_CQZ = "CQZ";

	/**
	 * Magic Number for FSH files
	 */
	public static final String MAGICNUMBER_FSH = "SHPI";

	/**
	 * Magic number for Compressed data
	 */
	public static final int MAGICNUMBER_QFS = 0xFB10;

	/**
	 * Magic number for the B-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_BINARY = 0x42;

	/**
	 * Magic number for the T-Format of an exemplar
	 * 
	 */
	public static final short FORMAT_TEXT = 0x54;

	/**
	 * Header size of a DBPF file
	 */
	public static final int HEADERSIZE_DBPF = 0x60; // =96dec

	/**
	 * Header size of a FSH file
	 */
	public static final int HEADERSIZE_FSH = 0x10; // =16dec;

	/**
	 * Header size of a directory inside a FSH file
	 */
	public static final int HEADERSIZE_FSH_DIR = 0x08;

	/**
	 * Header size of a entry inside a FSH file
	 */
	public static final int HEADERSIZE_FSH_ENTRY = 0x10; // =16dec;

}
