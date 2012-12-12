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
 * This class holds the two images and information for the DXTCoder.<br>
 * If image has alpha, the following images will be created:<br>
 * <ul>
 * <li>color = BufferedImage.TYPE_4BYTE_ABGR</li>
 * <li>alpha = BufferedImage.TYPE_3BYTE_BGR</li>
 * </ul>
 * If image has <b>no</b> alpha the following images will be created:<br>
 * <ul>
 * <li>color = BufferedImage.TYPE_3BYTE_BGR</li>
 * <li>alpha = NULL</li>
 * </ul>
 * <br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 14.08.2012
 * 
 */
public class DXTImage {

	private BufferedImage color;
	private BufferedImage alpha;

	/**
	 * Constructor.<br>
	 * 
	 * @param width
	 *            The width of the images
	 * @param height
	 *            The height of the images
	 * @param hasAlpha
	 *            TRUE, if Alpha image exists; FALSE, otherwise
	 */
	public DXTImage(int width, int height, boolean hasAlpha) {
		createEmptyImages(width, height, hasAlpha);
	}

	/**
	 * Creates empty images with the given width and height.<br>
	 * If image has alpha, the following images will be created:<br>
	 * <ul>
	 * <li>color = BufferedImage.TYPE_4BYTE_ABGR</li>
	 * <li>alpha = BufferedImage.TYPE_3BYTE_BGR</li>
	 * </ul>
	 * If image has <b>no</b> alpha the following images will be created:<br>
	 * <ul>
	 * <li>color = BufferedImage.TYPE_3BYTE_BGR</li>
	 * <li>alpha = NULL</li>
	 * </ul>
	 * <br>
	 * 
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 * @param hasAlpha
	 *            TRUE, if alpha canal exists; FALSE, otherwise
	 */
	private void createEmptyImages(int width, int height, boolean hasAlpha) {
		if (hasAlpha) {
			color = new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
			alpha = new BufferedImage(width, height,
					BufferedImage.TYPE_3BYTE_BGR);
		} else {
			color = new BufferedImage(width, height,
					BufferedImage.TYPE_3BYTE_BGR);
			alpha = null;
		}
	}

	/**
	 * Returns the width of the color image.<br>
	 * 
	 * @return The width
	 */
	public int getWidth() {
		return color.getWidth();
	}

	/**
	 * Returns the height of the color image.<br>
	 * 
	 * @return The height
	 */
	public int getHeight() {
		return color.getHeight();
	}

	/**
	 * Returns the state of Color image.<br>
	 * 
	 * @return TRUE, if color image exists; FALSE, otherwise
	 */
	public boolean isColor() {
		return (color != null);
	}

	/**
	 * Returns the state of Alpha image.<br>
	 * 
	 * @return TRUE, if alpha image exists; FALSE, otherwise
	 */
	public boolean isAlpha() {
		return (alpha != null);
	}

	/**
	 * @return the color
	 */
	public BufferedImage getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(BufferedImage color) {
		this.color = color;
	}

	/**
	 * @return the alpha
	 */
	public BufferedImage getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(BufferedImage alpha) {
		this.alpha = alpha;
	}
}
