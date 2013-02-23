package ssp.dbpf;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.DBPFConverter;
import ssp.dbpf.entries.DBPFEntry;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.properties.DBPFProperty;
import ssp.dbpf.types.DBPFExemplar;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF lots.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitLots extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator + "lots");

	@Test
	public void testLots() {
		TestUtil.printLine("TEST: Lots");
		TestUtil.printLine();

		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("sc4lot,sc4desc,dat");
		FileOp.listFiles(srcFolder, filelist, xff, true);
		long t1 = System.currentTimeMillis();
		for (File file : filelist) {
			try {
				check(file);
				// Scanner sc = new Scanner(System.in) ;
				// sc.nextLine();
			} catch (DBPFException e) {
				fail(e.getMessage());
			}
		}
		long t2 = System.currentTimeMillis();
		TestUtil.printLine("Time to check " + filelist.size() + " files: "
				+ (t2 - t1) / 1000 + " sec");
	}

	/**
	 * Checks a value.<br>
	 * 
	 * @param value
	 *            The value
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("Check file: " + file.toString() + " ...");

		DBPFContainer dbpfFile = DBPFReader.read(file);

		TestUtil.printLine(" > Entries: " + dbpfFile.getIndexEntryCount());
		for (DBPFEntry entry : dbpfFile.getEntryList()) {
			// TestUtil.printLine(" > Entry: "+entry.getTGIKey().toString());

			// SILENT CHECK
			DBPFConverter.createType(entry);

			DBPFExemplar exem = DBPFConverter.createExemplar(entry);
			if (exem != null) {
				DBPFProperty[] props = exem.getPropertyList();
//				TestUtil.printLine(" > Exemplar Entry: "
//						+ entry.getTGIKey().toString());
//				TestUtil.printLine(" > Exemplar Properties: " + props.length);
				for (int i = 0; i < props.length; i++) {
					// SILENT CHECK
					props[i].toRaw();
//					TestUtil.printLine(props[i].toText());
				}
			}
		}
		TestUtil.printLine();
	}

}
