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
package ssp.dbpf.fsh.dxt;

import java.awt.image.BufferedImage;

/**
 * Handles the DXT compression.<br>
 * 
 * Supported formats are: DXT1 (RGB), DXT3 (ARGB).
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public class DXTCoder {

	/**
	 * Stores the ALPHA and COLOR masks
	 */
	private final DXTMask[] ALPHA = new DXTMask[] { DXTMask.HIGHBITS4,
			DXTMask.LOWBITS4 };
	private final DXTMask[] COLOR = new DXTMask[] { DXTMask.ALPHA_1BYTE,
			DXTMask.RED_1BYTE, DXTMask.GREEN_1BYTE, DXTMask.BLUE_1BYTE };

	/**
	 * Constructor.<br>
	 */
	public DXTCoder() {
	}

	@Override
	public String toString() {
		return "DXTCoder";
	}

	/**
	 * Creates an image for Alpha from the given Color image.<br>
	 * The Color image has to be a BufferedImage.TYPE_4BYTE_ABGR or NULL will be
	 * returned.
	 * 
	 * @param color
	 *            The Color image
	 * @return The Alpha image or NULL, if wrong type
	 */
	public static BufferedImage createAlphaFromColor(BufferedImage color) {
		if (color.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
			return null;
		}
		int width = color.getWidth();
		int height = color.getHeight();
		BufferedImage alpha = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int rgb = color.getRGB(j, i) >> 24;
				int value = rgb | rgb << 8 | rgb << 16;
				alpha.setRGB(j, i, value);
			}
		}
		return alpha;
	}

	/**
	 * Creates an image from the Color image and Alpha image.<br>
	 * The alpha value will be taken from the blue channel of Alpha image. The
	 * resulting image will be BufferedImage.TYPE_4BYTE_ABGR.
	 * 
	 * @param color
	 *            The color image
	 * @param alpha
	 *            The alpha image
	 * @return A new image
	 */
	public static BufferedImage createColorWithAlpha(BufferedImage color,
			BufferedImage alpha) {
		int width = color.getWidth();
		int height = color.getHeight();
		BufferedImage coloralpha = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int value = color.getRGB(j, i)
						| ((alpha.getRGB(j, i) & 0xFF) << 24);
				coloralpha.setRGB(j, i, value);
			}
		}
		return coloralpha;
	}

	// **************************************************************
	// DECODING, from short[] to DXTImage
	// **************************************************************

	/**
	 * Decode the data to the image container.<br>
	 * 
	 * Existing images in the container will be overwritten!
	 * 
	 * @param dxtImage
	 *            A container to store the decoded images
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset where to start
	 */
	public void decode(DXTImage dxtImage, short[] data, int offset)
			throws DXTException {
		if (!dxtImage.isColor()) {
			throw new DXTException(
					toString()
							+ ": No width and size for image given! Can not decode data.");
		}

		int width = dxtImage.getWidth();
		int height = dxtImage.getHeight();
		// for all 4x4 blocks:
		for (int block = 0; block < (width * height / 16); block++) {

			// offset position for the image
			int xoffset = block * 4 % width;
			int yoffset = (block * 4 / width) * 4;

			if (dxtImage.isAlpha()) {
				offset = decodeAlpha(dxtImage, xoffset, yoffset, data, offset);
			}
			offset = decodeColor(dxtImage, xoffset, yoffset, data, offset);
		}
	}

	/**
	 * Decodes the alpha canal from the data.<br>
	 * 
	 * @param xoffset
	 *            The x-offset in the image
	 * @param yoffset
	 *            The y-offset in the image
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset in the data
	 * 
	 * @return The new offset position
	 */
	private int decodeAlpha(DXTImage dxtImage, int xoffset, int yoffset,
			short[] data, int offset) {
		// Temporary store of the images
		BufferedImage imageColor = dxtImage.getColor();
		BufferedImage imageGray = dxtImage.getAlpha();

		// reverse byte-order instead of {0,1,2,3}
		int px[] = new int[] { 1, 0, 3, 2 };
		// Loop through the alpha block
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int index = col % 2;
				int alpha = ((data[offset] & 0xFF) & ALPHA[index].getMask()) >> ALPHA[index]
						.getShift();
				// uses the highest 4 bit gray component
				alpha = (alpha & DXTMask.LOWBITS4.getMask()) << 4;

				int x = xoffset + px[col];
				int y = yoffset + row;
				// Set the alpha to the image
				imageColor.setRGB(x, y, alpha << 24);
				// Set the alpha as grey value to greymap for RGB
				imageGray.setRGB(x, y, alpha | alpha << 8 | alpha << 16);
				offset += col % 2;
			}
		}
		return offset;
	}

	/**
	 * Decodes the color canals from the data.<br>
	 * 
	 * @param xoffset
	 *            The x-offset in the image
	 * @param yoffset
	 *            The y-offset in the image
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset in the data
	 * 
	 * @return The new offset position
	 */
	private int decodeColor(DXTImage dxtImage, int xoffset, int yoffset,
			short[] data, int offset) {
		BufferedImage imageColor = dxtImage.getColor();
		// 4 colors
		DXTPixel c[] = new DXTPixel[4];
		c[0] = DXTPixel.getInstance(data[offset], data[offset + 1]);
		c[1] = DXTPixel.getInstance(data[offset + 2], data[offset + 3]);
		offset += 4;
		c[2] = new DXTPixel();
		c[3] = new DXTPixel();
		setColors(c);

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int x = xoffset + (3 - col);
				int y = yoffset + row;

				int index = col;
				int color = (data[offset] & COLOR[index].getMask()) >> COLOR[index]
						.getShift();
				// get alpha, if already defined
				int a = imageColor.getRGB(x, y);
				// add alpha to the color
				int rgb = a | (c[color].to24Bit());
				imageColor.setRGB(x, y, rgb);
			}
			offset += 1;
		}
		return offset;
	}

	// **************************************************************
	// ENCODING, from DXTImage to short[]
	// **************************************************************

	/**
	 * Encodes the image container to data.<br>
	 * 
	 * @param dxtImage
	 *            The image container
	 * @param data
	 *            The data
	 * @param offset
	 *            The offset of the data
	 * @return The offset after encoding
	 */
	public int encode(DXTImage dxtImage, short[] data, int offset)
			throws DXTException {
		if (!dxtImage.isColor()) {
			throw new DXTException(toString()
					+ ": No Color image given! Can not decode data.");
		}

		BufferedImage imageColor = dxtImage.getColor();
		int width = imageColor.getWidth();
		int height = imageColor.getHeight();

		// all 4x4 blocks...
		for (int block = 0; block < width * height / 16; block++) {
			int xoffset = block * 4 % width;
			int yoffset = (block * 4 / width) * 4;

			// alpha information: DXT3
			if (dxtImage.isAlpha()) {
				offset = encodeAlpha(dxtImage, xoffset, yoffset, data, offset);
			}
			offset = encodeColor(dxtImage, xoffset, yoffset, data, offset);
		}
		return offset;
	}

	/**
	 * Encodes the Alpha image.<br>
	 * 
	 * @param dxtImage
	 *            The DXTImage with alpha image
	 * @param xoffset
	 *            The xoffset in the alpha image
	 * @param yoffset
	 *            The yoffset in the alpha image
	 * @param data
	 *            The data to encode to
	 * @param offset
	 *            The offset in the data to start
	 * @return The new offset position
	 */
	private int encodeAlpha(DXTImage dxtImage, int xoffset, int yoffset,
			short[] data, int offset) {
		BufferedImage imageAlpha = dxtImage.getAlpha();
		int alpha[][] = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				alpha[i][j] = imageAlpha.getRGB(xoffset + i, yoffset + j);
				// uses highest 4 bit of blue component, hopefully gray
				// component
				alpha[i][j] = (alpha[i][j] & DXTMask.HIGHBITS4.getMask()) >> 4;
			}
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col += 2) {
				int value = (alpha[col][row] | (alpha[col + 1][row] << 4));
				// DBPFUtil.setInt32(value, data, offset, 1);
				data[offset] = (short) value;
				offset += 1;
			}
		}
		return offset;
	}

	/**
	 * Encodes the Color image.<br>
	 * 
	 * @param dxtImage
	 *            The DXTImage with color image
	 * @param xoffset
	 *            The xoffset in the color image
	 * @param yoffset
	 *            The yoffset in the color image
	 * @param data
	 *            The data to encode to
	 * @param offset
	 *            The offset in the data to start
	 * @return The new offset position
	 */
	private int encodeColor(DXTImage dxtImage, int xoffset, int yoffset,
			short[] data, int offset) {
		BufferedImage imageColor = dxtImage.getColor();

		// get colors
		DXTPixel rgb[][] = new DXTPixel[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				rgb[i][j] = DXTPixel.getInstance(imageColor.getRGB(xoffset + i,
						yoffset + j));
			}
		}

		// find extremes
		DXTPixel c[] = new DXTPixel[4];
		c[0] = rgb[0][0];
		c[1] = rgb[0][0];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (rgb[i][j].score() > c[0].score()) {
					c[0] = rgb[i][j];
				}
				if (rgb[i][j].score() < c[1].score()) {
					c[1] = rgb[i][j];
				}
			}
		}

		// make sure c0 > c1
		if (c[0].to16Bit() < c[1].to16Bit()) {
			DXTPixel t = c[0];
			c[0] = c[1];
			c[1] = t;
		}
		c[2] = new DXTPixel();
		c[3] = new DXTPixel();
		setColors(c);

		int colorIndex[][] = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				colorIndex[i][j] = bestColorIndex(c, rgb[i][j]); // 0 to 3
			}
		}

		// add colors
		// DBPFUtil.setInt32(c[0].to16Bit() & 0xFFFF, data, offset, 2);
		// DBPFUtil.setInt32(c[1].to16Bit() & 0xFFFF, data, offset+2, 2);
		data[0 + offset] = (short) (((c[0].to16Bit() & 0x0FFFF) << 8) >> 8);
		data[1 + offset] = (short) (((c[0].to16Bit() & 0x0FFFF) << 0) >> 8);
		data[2 + offset] = (short) (((c[1].to16Bit() & 0x0FFFF) << 8) >> 8);
		data[3 + offset] = (short) (((c[1].to16Bit() & 0x0FFFF) << 0) >> 8);
		offset += 4;

		// add pixel bits
		for (int i = 0; i < 4; i++) {
			int color = (colorIndex[0][i]) | (colorIndex[1][i] << 2)
					| (colorIndex[2][i] << 4) | (colorIndex[3][i] << 6);
			// DBPFUtil.setInt32(color, data, offset, 1);
			data[offset] = (short) color;
			offset += 1;
		}
		return offset;
	}

	// **************************************************************
	// MISC
	// **************************************************************

	/**
	 * Sets the RGB for the pixel.<br>
	 * Use the first [0] and second [1] pixel to set the third [2] and fourth
	 * [3] pixel.
	 * 
	 * @param c
	 *            The array with the pixel
	 */
	private void setColors(DXTPixel c[]) {
		if (c[0].to16Bit() > c[1].to16Bit()) {
			c[2].r = (c[0].r * 2 + c[1].r) / 3;
			c[2].g = (c[0].g * 2 + c[1].g) / 3;
			c[2].b = (c[0].b * 2 + c[1].b) / 3;

			c[3].r = (c[1].r * 2 + c[0].r) / 3;
			c[3].g = (c[1].g * 2 + c[0].g) / 3;
			c[3].b = (c[1].b * 2 + c[0].b) / 3;
		} else {
			c[2].r = (c[0].r + c[1].r) * 1 / 2;
			c[2].g = (c[0].g + c[1].g) * 1 / 2;
			c[2].b = (c[0].b + c[1].b) * 1 / 2;

			c[3].r = 0;
			c[3].g = 0;
			c[3].b = 0;
		}
	}

	/**
	 * Returns the index of the color array with the smallest distance to the
	 * compare color.<br>
	 * 
	 * @param c
	 *            The color array
	 * @param color
	 *            The compare color
	 * @return The index
	 */
	private int bestColorIndex(DXTPixel c[], DXTPixel color) {
		int index = 0;
		int di, dmin = color.distance(c[0]);
		for (int i = 1; i < c.length; i++) {
			di = color.distance(c[i]);
			if (di < dmin) {
				dmin = di;
				index = i;
			}
		}
		return index;
	}
}
