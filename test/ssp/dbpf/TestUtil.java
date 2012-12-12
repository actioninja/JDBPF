package ssp.dbpf;

import ssp.dbpf.util.DBPFUtil;

/**
 * Some help things for JUnit tests.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 0.0.1, 10.11.2012
 * 
 */
public class TestUtil {

	/**
	 * Prints the given string without linefeed.<br>
	 * 
	 * @param s
	 *            The string
	 */
	public static void print(String s) {
		System.out.print(s);
	}

	/**
	 * Prints the given string.<br>
	 * 
	 * @param s
	 *            The string
	 */
	public static void printLine(String s) {
		System.out.println(s);
	}

	/**
	 * Prints an line with minus '-'.
	 */
	public static void printLine() {
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Prints the data in a special HEX format with 16 hex values.<br>
	 * 
	 * @param data
	 *            The data
	 */
	public static void printData(short[] data) {
		System.out.println("## ## ## ## ## ## ## ## | ## ## ## ## ## ## ## ##");
		int count = 0;
		for (Short s : data) {
			System.out.print(DBPFUtil.toHex(s, 2) + " ");
			count++;
			if (count == 8) {
				System.out.print("| ");
			} else if (count == 16) {
				count = 0;
				System.out.println("");
			}
		}
		System.out.println("");
	}
}
