/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.incremental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessManager implements IProcessManager {

	public static final String NULL = "NULL";
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String filename;
	private SimpleDateFormat dateFormat;
	private String split;
	
	public FileProcessManager(String filename) {
		this.filename = filename;
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.split = ",";
	}
	
	@Override
	public ProcessManagerEntry getEntry() {
		
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(this.filename));
			String line = r.readLine();
			return parseEntry(line, this.dateFormat, split);
			
		} catch (FileNotFoundException e) {
			this.logger.debug("File not found.", e);
			return null;
		} catch (IOException e) {
			this.logger.debug("Reading file failed.", e);
			return null;
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					this.logger.debug("Closing file reader failed.", e);
				}
			}
		}
	}

	@Override
	public void start(Date minTime) {
		
		File f = new File(this.filename);
		if (f.exists()) f.delete();
		
		ProcessManagerEntry entry = new ProcessManagerEntry();
		entry.setStartTime(new Date());
		entry.setMinTime(minTime);
		
		try {
			FileWriter fw = new FileWriter(this.filename);
			fw.append(entryToString(entry, this.dateFormat, this.split));
			fw.close();
		} catch (IOException e) {
			this.logger.debug("Writing start failed.", e);
		}
		
	}

	@Override
	public void finish(Date maxTime) {
		
		ProcessManagerEntry entry = this.getEntry();
		entry.setEndTime(new Date());
		entry.setMaxTime(maxTime);
		
		try {
			FileWriter fw = new FileWriter(this.filename, false);
			fw.append(entryToString(entry, dateFormat, split));
			fw.close();
		} catch (IOException e) {
			this.logger.debug("Writing finish failed.", e);
		}
		
	}
	
	public static String entryToString(
			ProcessManagerEntry e, DateFormat dateFormat, String split) {
		
		StringBuffer b = new StringBuffer();
		
		if (e.getStartTime() != null)
			b.append(dateFormat.format(e.getStartTime()));
		else
			b.append(NULL);
		b.append(split);
		
		if (e.getMinTime() != null)
			b.append(dateFormat.format(e.getMinTime()));
		else
			b.append(NULL);
		b.append(split);
		
		if (e.getEndTime() != null)
			b.append(dateFormat.format(e.getEndTime()));
		else
			b.append(NULL);
		b.append(split);
		
		if (e.getMaxTime() != null)
			b.append(dateFormat.format(e.getMaxTime()));
		else
			b.append(NULL);
		
		return b.toString();
	}
	
	public static ProcessManagerEntry parseEntry(
			String line, DateFormat dateFormat, String split) {
		
		if (line == null) {
			
			return null;
			
		} else {
			
			String[] strings = line.split(split);
			ProcessManagerEntry e = new ProcessManagerEntry();
			if (!strings[0].trim().equals(NULL)) {
				try {
					e.setStartTime(dateFormat.parse(strings[0]));
				} catch (ParseException e1) {
				}
			}
			if (!strings[1].trim().equals(NULL)) {
				try {
					e.setMinTime(dateFormat.parse(strings[1]));
				} catch (ParseException e1) {
				}
			}
			if (!strings[2].trim().equals(NULL)) {
				try {
					e.setEndTime(dateFormat.parse(strings[2]));
				} catch (ParseException e1) {
				}
			}
			if (!strings[3].trim().equals(NULL)) {
				try {
					e.setMaxTime(dateFormat.parse(strings[3]));
				} catch (ParseException e1) {
				}
			}
			
			return e;
		}
	}

}
