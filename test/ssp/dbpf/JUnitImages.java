package ssp.dbpf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.converter.DBPFPackager;
import ssp.dbpf.converter.types.PNGConverter;
import ssp.dbpf.event.DBPFException;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.io.DBPFWriter;
import ssp.dbpf.tgi.TGIKeys;
import ssp.dbpf.types.DBPFPNG;
import ssp.dbpf.types.DBPFType;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF images.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitImages extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator + "images");
	private File dstFolder = new File("testfiles" + File.separator + "images"
			+ File.separator + "out");

	@Test
	public void testImages() {
		if (!dstFolder.exists()) {
			assertEquals(true, dstFolder.mkdir());
		}

		TestUtil.printLine("TEST: Images");
		TestUtil.printLine();
		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("png");
		FileOp.listFiles(srcFolder, filelist, xff, false);

		for (File file : filelist) {
			try {
				check(file);
			} catch (DBPFException e) {
				fail(e.getMessage());
			}
		}

		assertEquals(3 * filelist.size(), dstFolder.listFiles().length);
	}

	/**
	 * Checks the file.<br>
	 * 
	 * @param file
	 *            The file
	 * @throws DBPFException Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		TestUtil.printLine("Compress check of image: " + file.toString()
				+ " ...");
		short[] rawData = DBPFReader.readRawData(file);

		short[] cData = DBPFPackager.compress(rawData);
		File fileout1 = new File(dstFolder, "new-compressed.cpng");
		TestUtil.printLine("Write as compressed raw data ...");
		DBPFWriter.writeRawData(fileout1, cData);

		short[] dData = DBPFPackager.decompress(cData);
		File fileout3 = new File(dstFolder,
				"new-decompressed_from_compressed.png");
		TestUtil.printLine("Write as decompressed raw data ...");
		DBPFWriter.writeRawData(fileout3, dData);

		DBPFPNG png = PNGConverter.createType(cData);
		Random random = new Random();
		long iid = random.nextInt(0x7fffffff);
		png.setTGIKey(TGIKeys.PNG_ICON.getTGIKey().clone(-1, -1, iid));
		png.setCompressed(true);
		Vector<DBPFType> writeList = new Vector<DBPFType>();
		writeList.addElement(png);
		File fileout2 = new File(dstFolder, "new-compressed.dat");
		TestUtil.printLine("Write as DBPFType ...");
		DBPFWriter.write(fileout2, writeList);

		DBPFCollection collection = DBPFReader.readCollection(fileout2);
		for (DBPFType type : collection.getTypeList()) {
			TestUtil.printLine(type.getClass() + ": " + type.toString());
			if (type instanceof DBPFPNG) {
				BufferedImage bim = ((DBPFPNG) type).getImage();
				JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(
						bim)));
			}
		}
	}
}
