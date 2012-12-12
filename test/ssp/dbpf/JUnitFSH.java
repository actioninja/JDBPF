package ssp.dbpf;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import junit.framework.TestCase;

import org.junit.Test;

import ssp.dbpf.event.DBPFException;
import ssp.dbpf.fsh.FSHContainer;
import ssp.dbpf.fsh.FSHHandler;
import ssp.dbpf.fsh.FSHEntry;
import ssp.dbpf.fsh.dxt.DXTCoder;
import ssp.dbpf.fsh.dxt.DXTImage;
import ssp.dbpf.io.DBPFReader;
import ssp.dbpf.io.DBPFWriter;
import ssp.xtools.io.XFileFilter;
import ssp.xtools.util.FileOp;

/**
 * Tests the DBPF FSH.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class JUnitFSH extends TestCase {

	private File srcFolder = new File("testfiles" + File.separator + "fsh");
	private File dstFolder = new File("testfiles" + File.separator + "fsh"
			+ File.separator + "out");

	@Test
	public void testFSH() {
		if (!dstFolder.exists()) {
			assertEquals(true, dstFolder.mkdir());
		}

		TestUtil.printLine("TEST: FSH");
		TestUtil.printLine();
		Vector<File> filelist = new Vector<File>();
		XFileFilter xff = new XFileFilter("fsh");
		FileOp.listFiles(srcFolder, filelist, xff, false);

		for (File file : filelist) {
			try {
				check(file);
			} catch (DBPFException e) {
				fail(e.getMessage());
			}
		}

		assertEquals(4 * filelist.size(), dstFolder.listFiles().length);
	}

	/**
	 * Checks the file.<br>
	 * 
	 * @param file
	 *            The file
	 * @throws DBPFException
	 *             Thrown, if any error occur
	 */
	public void check(File file) throws DBPFException {
		short[] dData = DBPFReader.readRawData(file);
		FSHContainer container = FSHHandler.createContainer(dData);
		FSHEntry entry = container.getEntryList().get(0);
		DXTImage dxtImage = entry.getDxtImage();
		viewDXTImage(dxtImage);

		BufferedImage bim = DXTCoder.createColorWithAlpha(dxtImage.getColor(),
				dxtImage.getAlpha());
		JOptionPane.showMessageDialog(null, new ImageIcon(bim));

		short[] data = FSHHandler.createData(container);
		File fout = new File(dstFolder, file.getName());
		DBPFWriter.writeRawData(fout, data);

		short[] dData3 = DBPFReader.readRawData(fout);
		container = FSHHandler.createContainer(dData3);
		viewDXTImage(container.getEntryList().get(0).getDxtImage());
	}

	/**
	 * Views the DXTImage.<br>
	 * 
	 * @param image
	 *            The DXTImage
	 */
	private void viewDXTImage(DXTImage image) {
		if (image.getColor() != null) {
			JPanel jp = new JPanel(new GridLayout(1, 2));
			jp.add(new JLabel(new ImageIcon(image.getColor())));
			if (image.getAlpha() == null) {
				jp.add(new JLabel("not available", JLabel.CENTER));
			} else {
				jp.add(new JLabel(new ImageIcon(image.getAlpha())));
			}
			JOptionPane.showMessageDialog(null, jp);
		}
	}

}
