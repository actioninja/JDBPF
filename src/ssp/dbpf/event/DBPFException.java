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
package ssp.dbpf.event;

/**
 * Defines the DBPFException.<br>
 * Will be thrown, if any error inside DBPF occur.
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 */
public class DBPFException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -825448989877189971L;

	private Object source;

	/**
	 * Constructor.<br>
	 * 
	 * @param message
	 *            The message for this exception
	 */
	public DBPFException(Object source, String message) {
		super(message);
		this.source = source;
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param message
	 *            The message for this exception
	 * @param cause
	 *            The cause of this exception
	 */
	public DBPFException(Object source, String message, Throwable cause) {
		super(message, cause);
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Object source) {
		this.source = source;
	}

}
