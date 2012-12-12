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
package ssp.dbpf.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logging of DBPF.<br>
 * 
 * @author Stefan Wertich, mapsonswen@web.de
 * @version 2.0.0, 13.08.2012
 *
 */
public class DBPFLogger {

	/**
	 * The logger name for logging events
	 */
	public static final String LOGNAME = "JDBPF";
	
	static {
		Logger.getLogger(DBPFLogger.LOGNAME).setUseParentHandlers(false);
	}

	/**
	 * Logs the message to the standard DBPF logger.<br>
	 * 
	 * @param level
	 *            The level
	 * @param message
	 *            The message
	 */
	public static void toLog(String source, Level level, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(source);
		Logger.getLogger(DBPFLogger.LOGNAME).log(record);
	}

}
