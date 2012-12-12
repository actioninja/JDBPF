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

import java.util.ArrayList;
import java.util.List;


/**
 * Defines a FSHContainer which stores the entries in a FSH file.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 04.12.2012
 * 
 */
public class FSHContainer {

	private FSHDirectoryID directoryID;

	private List<FSHEntry> entryList = new ArrayList<FSHEntry>();

	/**
	 * Constructor.<br>
	 */
	public FSHContainer(FSHDirectoryID directoryID) {
		this.directoryID = directoryID;
	}

	/**
	 * Adds the entry to this container.<br>
	 * 
	 * @param entry
	 *            The entry
	 */
	public void addEntry(FSHEntry entry) {
		entryList.add(entry);
	}

	/**
	 * @return the entryList
	 */
	public List<FSHEntry> getEntryList() {
		return entryList;
	}

	/**
	 * @return the directoryID
	 */
	public FSHDirectoryID getDirectoryID() {
		return directoryID;
	}
}
