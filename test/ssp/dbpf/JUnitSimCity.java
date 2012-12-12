package ssp.dbpf;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the SimCity lots.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitSimCity extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator
			+ "simcity");
	
	@Test
	public void testLots() {
		TestUtil.printLine("TEST: SimCity");
		TestUtil.printLine();

		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("dat");
		FileOp.listFiles(srcFolder, filelist, xff, true);
//		File file = new File(srcFolder,"SimCity_1.dat");
		for (File file : filelist) {
			try {
				check(file);
			} catch (DBPFException e) {
				fail(e.getMessage());
			}
		}
	}

	/**
	 * Checks a value.<br>
	 * 
	 * @param value
	 *            The value
	 * @throws DBPFException Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("Check file: " + file.toString() + " ...");

		DBPFContainer dbpfFile = DBPFReader.read(file);

		TestUtil.printLine(" > Entries: " + dbpfFile.getIndexEntryCount());
//		int col = 0;
		for (DBPFEntry entry : dbpfFile.getEntryList()) {
//			col++;
//			if (col == 80) {
//				TestUtil.printLine("*");
//				col=0;
//			} else {
//				TestUtil.print("+");
//			}
			
			// SILENT CHECK
			DBPFConverter.createType(entry);

//			DBPFExemplar exem = DBPFConverter.createExemplar(entry);
//			if (exem != null) {
//				DBPFProperty[] props = exem.getPropertyList();
//				System.out.print(" > Properties: " + props.length);
//				for (int i = 0; i < props.length; i++) {
//					try {
//						// SILENT CHECK
//						props[i].toRaw();
//						props[i].toText();
//					} catch (IOException e) {
//						fail(e.getLocalizedMessage());
//						e.printStackTrace();
//					}
//				}
//			}
		}
		TestUtil.printLine("");
		TestUtil.printLine("Check file finished.");
		TestUtil.printLine();
	}

}
