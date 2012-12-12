/*
 * Copyright (c) 2012 by Stefan Wertich.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this code.  If not, see <http://www.gnu.org/licenses/>.
 */
package ssp.dbpf.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import ssp.dbpf.util.DBPFConstant;
import ssp.dbpf.util.DBPFLogger;
import ssp.dbpf.util.DBPFUtil;

/**
 * Handle compressed data in DBPF format.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 09.12.2012
 * 
 */
public class DBPFPackager {

	private long compressedSize = 0;
	private long decompressedSize = 0;
	private boolean compressed = false;

	/**
	 * Constructor.<br>
	 */
	public DBPFPackager() {
	}

	/**
	 * @return the compressedSize
	 */
	public long getCompressedSize() {
		return compressedSize;
	}

	/**
	 * @return the decompressedSize
	 */
	public long getDecompressedSize() {
		return decompressedSize;
	}

	/**
	 * @return the compressed
	 */
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * Check, if data is compressed.<br>
	 * 
	 * @param data
	 *            The data to check
	 * @return TRUE, if compressed; FALSE, otherwise
	 */
	public static boolean isCompressed(short[] data) {
		if (data.length > 6) {
			int signature = (int) DBPFUtil.getUint32(data, 0x04, 2);
			if (signature == DBPFConstant.MAGICNUMBER_QFS) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Copies data from source to destination array.<br>
	 * The copy is byte by byte from srcPos to destPos and given length.
	 * 
	 * @param src
	 *            The source array
	 * @param srcPos
	 *            The source position
	 * @param dest
	 *            The destination array
	 * @param destPos
	 *            The destination position
	 * @param length
	 *            The length
	 */
	private void arrayCopy2(short[] src, int srcPos, short[] dest, int destPos,
			long length) {
		// This shouldn't occur, but to prevent errors
		if (dest.length < destPos + length) {
			String message = "ATTENTION!"
					+ "\n[arrayCopy2] The destination array is not big enough!"
					+ "\nWill make it bigger and make a System.arraycopy.";
			DBPFLogger.toLog("DBPFPackager", Level.WARNING, message);

			short[] destExt = new short[(int) (destPos + length)];
			System.arraycopy(dest, 0, destExt, 0, dest.length);
			dest = destExt;
		}

		for (int i = 0; i < length; i++) {
			dest[destPos + i] = src[srcPos + i];
		}
	}

	/**
	 * Copies data from array at destPos-srcPos to array at destPos.<br>
	 * 
	 * @param array
	 *            The array
	 * @param srcPos
	 *            The position to copy from (reverse from end of array!)
	 * @param destPos
	 *            The position to copy to
	 * @param length
	 *            The length of data to copy
	 */
	private void offsetCopy(short[] array, int srcPos, int destPos, long length) {
		srcPos = destPos - srcPos;
		// This shouldn't occur, but to prevent errors
		if (array.length < destPos + length) {
			String message = "ATTENTION!"
					+ "\n[offsetCopy] The destination array is not big enough!"
					+ "\nWill make it bigger and make a System.arraycopy.";
			DBPFLogger.toLog("DBPFPackager", Level.WARNING, message);

			short[] arrayNew = new short[(int) (destPos + length)];
			System.arraycopy(array, 0, arrayNew, 0, array.length);
			array = arrayNew;
		}

		for (int i = 0; i < length; i++) {
			array[destPos + i] = array[srcPos + i];
		}
	}

	/**
	 * Compress the decompressed data.<br>
	 * 
	 * @param dData
	 *            The decompressed data
	 * @return The compressed data
	 */
	public short[] compress(short[] dData) {
		// if data is big enough for compress
		if (dData.length > 6) {
			// check, if data already compressed
			int signature = (int) DBPFUtil.getUint32(dData, 0x04, 2);
			// System.out.println("Signature: " + DBPFUtil.toHex(signature, 4)
			// + "," + dData.length);
			if (signature != DBPFConstant.MAGICNUMBER_QFS) {

				// some Compression Data
				final int MAX_OFFSET = 0x20000;
				final int MAX_COPY_COUNT = 0x404;
				// used to finetune the lookup (small values increase the
				// compression for Big Files)
				final int QFS_MAXITER = 0x80;

				// contains the latest offset for a combination of two
				// characters
				HashMap<Integer, ArrayList<Integer>> cmpmap2 = new HashMap<Integer, ArrayList<Integer>>();

				// will contain the compressed data (maximal size =
				// uncompressedSize+MAX_COPY_COUNT)
				short[] cData = new short[dData.length + MAX_COPY_COUNT];

				// init some vars
				int writeIndex = 9; // leave 9 bytes for the header
				int lastReadIndex = 0;
				ArrayList<Integer> indexList = null;
				int copyOffset = 0;
				int copyCount = 0;
				int index = -1;
				boolean end = false;

				// begin main compression loop
				while (index < dData.length - 3) {
					// get all Compression Candidates (list of offsets for all
					// occurances of the current 3 bytes)
					do {
						index++;
						if (index >= dData.length - 2) {
							end = true;
							break;
						}
						int mapindex = dData[index] + (dData[index + 1] << 8)
								+ (dData[index + 2] << 16);

						indexList = cmpmap2.get(mapindex);
						if (indexList == null) {
							indexList = new ArrayList<Integer>();
							cmpmap2.put(mapindex, indexList);
						}
						indexList.add(index);
					} while (index < lastReadIndex);
					if (end) {
						break;
					}

					// find the longest repeating byte sequence in the index
					// List (for offset copy)
					int offsetCopyCount = 0;
					int loopcount = 1;
					while ((loopcount < indexList.size())
							&& (loopcount < QFS_MAXITER)) {
						int foundindex = (int) indexList
								.get((indexList.size() - 1) - loopcount);
						if ((index - foundindex) >= MAX_OFFSET) {
							break;
						}
						loopcount++;
						copyCount = 3;
						while ((dData.length > index + copyCount)
								&& (dData[index + copyCount] == dData[foundindex
										+ copyCount])
								&& (copyCount < MAX_COPY_COUNT)) {
							copyCount++;
						}
						if (copyCount > offsetCopyCount) {
							offsetCopyCount = copyCount;
							copyOffset = index - foundindex;
						}
					}

					// check if we can compress this
					// In FSH Tool stand additionally this:
					if (offsetCopyCount > dData.length - index) {
						offsetCopyCount = index - dData.length;
					}
					if (offsetCopyCount <= 2) {
						offsetCopyCount = 0;
					} else if ((offsetCopyCount == 3) && (copyOffset > 0x400)) { // 1024
						offsetCopyCount = 0;
					} else if ((offsetCopyCount == 4) && (copyOffset > 0x4000)) { // 16384
						offsetCopyCount = 0;
					}

					// this is offset-compressable? so do the compression
					if (offsetCopyCount > 0) {
						// plaincopy

						// In FSH Tool stand this (A):
						while (index - lastReadIndex >= 4) {
							copyCount = (index - lastReadIndex) / 4 - 1;
							if (copyCount > 0x1B) {
								copyCount = 0x1B;
							}
							cData[writeIndex++] = (short) (0xE0 + copyCount);
							copyCount = 4 * copyCount + 4;

							arrayCopy2(dData, lastReadIndex, cData, writeIndex,
									copyCount);
							lastReadIndex += copyCount;
							writeIndex += copyCount;
						}
						// while ((index - lastReadIndex) > 3) {
						// copyCount = (index - lastReadIndex);
						// while (copyCount > 0x71) {
						// copyCount -= 0x71;
						// }
						// copyCount = copyCount & 0xfc;
						// int realCopyCount = (copyCount >> 2);
						// cData[writeIndex++] = (short) (0xdf + realCopyCount);
						// arrayCopy2(dData, lastReadIndex, cData, writeIndex,
						// copyCount);
						// writeIndex += copyCount;
						// lastReadIndex += copyCount;
						// }

						// offsetcopy
						copyCount = index - lastReadIndex;
						copyOffset--;
						if ((offsetCopyCount <= 0x0A) && (copyOffset < 0x400)) {
							cData[writeIndex++] = (short) (((copyOffset >> 8) << 5)
									+ ((offsetCopyCount - 3) << 2) + copyCount);
							cData[writeIndex++] = (short) (copyOffset & 0xff);
						} else if ((offsetCopyCount <= 0x43)
								&& (copyOffset < 0x4000)) {
							cData[writeIndex++] = (short) (0x80 + (offsetCopyCount - 4));
							cData[writeIndex++] = (short) ((copyCount << 6) + (copyOffset >> 8));
							cData[writeIndex++] = (short) (copyOffset & 0xff);
						} else if ((offsetCopyCount <= MAX_COPY_COUNT)
								&& (copyOffset < MAX_OFFSET)) {
							cData[writeIndex++] = (short) (0xc0
									+ ((copyOffset >> 16) << 4)
									+ (((offsetCopyCount - 5) >> 8) << 2) + copyCount);
							cData[writeIndex++] = (short) ((copyOffset >> 8) & 0xff);
							cData[writeIndex++] = (short) (copyOffset & 0xff);
							cData[writeIndex++] = (short) ((offsetCopyCount - 5) & 0xff);
						}
						// else {
						// copyCount = 0;
						// offsetCopyCount = 0;
						// }

						// do the offset copy
						arrayCopy2(dData, lastReadIndex, cData, writeIndex,
								copyCount);
						writeIndex += copyCount;
						lastReadIndex += copyCount;
						lastReadIndex += offsetCopyCount;

					}
				}

				// add the End Record
				index = dData.length;
				// in FSH Tool stand the same as above (A)
				while (index - lastReadIndex >= 4) {
					copyCount = (index - lastReadIndex) / 4 - 1;
					if (copyCount > 0x1B) {
						copyCount = 0x1B;
					}
					cData[writeIndex++] = (short) (0xE0 + copyCount);
					copyCount = 4 * copyCount + 4;

					arrayCopy2(dData, lastReadIndex, cData, writeIndex,
							copyCount);
					lastReadIndex += copyCount;
					writeIndex += copyCount;
				}

				// lastReadIndex = Math.min(index, lastReadIndex);
				// while ((index - lastReadIndex) > 3) {
				// copyCount = (index - lastReadIndex);
				// while (copyCount > 0x71) {
				// copyCount -= 0x71;
				// }
				// copyCount = copyCount & 0xfc;
				// int realCopyCount = (copyCount >> 2);
				// cData[writeIndex++] = (short) (0xdf + realCopyCount);
				// arrayCopy2(dData, lastReadIndex, cData, writeIndex,
				// copyCount);
				// writeIndex += copyCount;
				// lastReadIndex += copyCount;
				// }
				copyCount = index - lastReadIndex;
				cData[writeIndex++] = (short) (0xfc + copyCount);
				arrayCopy2(dData, lastReadIndex, cData, writeIndex, copyCount);
				writeIndex += copyCount;
				lastReadIndex += copyCount;

				// write the header for the compressed data
				// set the compressed size
				DBPFUtil.setUint32(writeIndex, cData, 0x00, 4);
				this.compressedSize = writeIndex;
				// set the MAGICNUMBER
				DBPFUtil.setUint32(DBPFConstant.MAGICNUMBER_QFS, cData, 0x04, 2);
				// set the decompressed size
				short[] revData = new short[3];
				DBPFUtil.setUint32(dData.length, revData, 0x00, 3);
				for (int j = 0; j < revData.length; j++) {
					cData[j + 6] = revData[2 - j];
				}
				this.decompressedSize = dData.length;
				compressed = false;
				if (compressedSize < decompressedSize) {
					compressed = true;
				}
				// get the compressed data
				short[] retData = new short[writeIndex];
				System.arraycopy(cData, 0, retData, 0, writeIndex);
				return retData;
			}
		}
		return dData;
	}

	/**
	 * Decompress the compressed data.<br>
	 * 
	 * If the data are not compressed, this will return the same data.
	 * 
	 * @param cData
	 *            The compressed data
	 * @return The decompressed data
	 */
	public short[] decompress(short[] cData) {
		compressed = false;
		if (cData.length > 6) {
			// HEADER
			compressedSize = DBPFUtil.getUint32(cData, 0x00, 4);
			int signature = (int) DBPFUtil.getUint32(cData, 0x04, 2);
			// if not compressed
			decompressedSize = compressedSize;

			if (signature == DBPFConstant.MAGICNUMBER_QFS) {
				short a = (short) DBPFUtil.getUint32(cData, 0x06, 1);
				short b = (short) DBPFUtil.getUint32(cData, 0x07, 1);
				short c = (short) DBPFUtil.getUint32(cData, 0x08, 1);
				decompressedSize = a * 65536 + b * 256 + c;

				// There seems sometimes that given compressedSize is
				// not exactly the read data size.
				// Don't know why but take real data size for decompress
				if (Math.abs(compressedSize - cData.length) > 4) {
					String message = "[decompress] Different sizes! RawData-Size: "
							+ cData.length
							+ " CompressedSize: "
							+ compressedSize
							+ " DecompressedSize: "
							+ decompressedSize;
					DBPFLogger.toLog("DBPFPackager", Level.WARNING, message);
				}

				short[] dData = new short[(int) decompressedSize];
				int dpos = 0;
				// COMPRESSED DATA
				compressed = true;
				int pos = 9;
				long control1 = 0;
				while (control1 != 0xFC && pos < cData.length) {
					control1 = cData[pos];
					// System.out.println(">>> Position: " + pos +
					// " ## Control: "
					// + Integer.toHexString((int)
					// control1)+" ## Rest: "+(compressedSize-pos));
					pos++;

					if (control1 >= 0 && control1 <= 127) {
						// 0x00 - 0x7F
						long control2 = cData[pos];
						pos++;
						long numberOfPlainText = (control1 & 0x03);
						arrayCopy2(cData, pos, dData, dpos, numberOfPlainText);
						dpos += numberOfPlainText;
						pos += numberOfPlainText;

						int offset = (int) (((control1 & 0x60) << 3)
								+ (control2) + 1);
						long numberToCopyFromOffset = ((control1 & 0x1C) >> 2) + 3;
						offsetCopy(dData, offset, dpos, numberToCopyFromOffset);
						dpos += numberToCopyFromOffset;

					} else if (control1 >= 128 && control1 <= 191) {
						// 0x80 - 0xBF
						long control2 = cData[pos];
						pos++;
						long control3 = cData[pos];
						pos++;

						long numberOfPlainText = (control2 >> 6) & 0x03;
						arrayCopy2(cData, pos, dData, dpos, numberOfPlainText);
						dpos += numberOfPlainText;
						pos += numberOfPlainText;

						int offset = (int) (((control2 & 0x3F) << 8)
								+ (control3) + 1);
						long numberToCopyFromOffset = (control1 & 0x3F) + 4;
						offsetCopy(dData, offset, dpos, numberToCopyFromOffset);
						dpos += numberToCopyFromOffset;
					} else if (control1 >= 192 && control1 <= 223) {
						// 0xC0 - 0xDF
						long numberOfPlainText = (control1 & 0x03);
						long control2 = cData[pos];
						pos++;
						long control3 = cData[pos];
						pos++;
						long control4 = cData[pos];
						pos++;
						arrayCopy2(cData, pos, dData, dpos, numberOfPlainText);
						dpos += numberOfPlainText;
						pos += numberOfPlainText;

						int offset = (int) (((control1 & 0x10) << 12)
								+ (control2 << 8) + (control3) + 1);
						long numberToCopyFromOffset = ((control1 & 0x0C) << 6)
								+ (control4) + 5;
						offsetCopy(dData, offset, dpos, numberToCopyFromOffset);
						dpos += numberToCopyFromOffset;
					} else if (control1 >= 224 && control1 <= 251) {
						// 0xE0 - 0xFB
						long numberOfPlainText = ((control1 & 0x1F) << 2) + 4;
						arrayCopy2(cData, pos, dData, dpos, numberOfPlainText);
						dpos += numberOfPlainText;
						pos += numberOfPlainText;
					} else {
						long numberOfPlainText = (control1 & 0x03);
						arrayCopy2(cData, pos, dData, dpos, numberOfPlainText);
						dpos += numberOfPlainText;
						pos += numberOfPlainText;
					}
				}
				return dData;
			}
		}
		// no data to decompress
		compressed = false;
		return cData;
	}
}
