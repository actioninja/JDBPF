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
package ssp.dbpf.fsh;

/**
 * Defines the legal DirectoryID for a FSHContainer.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public enum FSHDirectoryID {

	/**
	 * G354 - Building Textures
	 */
	G354("G354"),
	/**
	 * G264 - Network textures, Sim Textures, Sim heads, Sim animations, Trees,
	 * props, Base textures, Misc colours
	 */
	G264("G264"),
	/**
	 * G266 - 3D Animation textures (ie the green rotating diamon in
	 * loteditor.dat
	 */
	G266("G266"),
	/**
	 * G290 - Dispatch marker textures
	 */
	G290("G290"),
	/**
	 * G315 - Small Sim texture, Network Transport Model Textures (trains etc.)
	 */
	G315("G315"),
	/**
	 * GIMX - UI Editor textures
	 */
	GIMX("GIMX"),
	/**
	 * G344 - BAT gen texture maps
	 */
	G344("G344");

	private String id;

	/**
	 * Constructor.<br>
	 * 
	 * @param id
	 *            The ID of this directoryID
	 */
	FSHDirectoryID(String id) {
		this.id = id;
	}

	/**
	 * Returns the DirectoryID for the given ID.<br>
	 * 
	 * @param id
	 *            The ID
	 * @return The DirectoryID
	 */
	public static FSHDirectoryID forID(String id) {
		for (FSHDirectoryID directoryID : values()) {
			if (directoryID.getID().equals(id)) {
				return directoryID;
			}
		}
		throw new UnsupportedOperationException(
				"[FSHDirectoryID] ID not supported: " + id);
	}

	/**
	 * Returns the ID.<br>
	 * 
	 * @return The ID
	 */
	public String getID() {
		return id;
	}
}
