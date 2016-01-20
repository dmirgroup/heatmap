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
package de.uniwue.dmir.heatmap.heatmaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonFileReader<I> implements IFileReader<I> {

	private ObjectMapper objectMapper;
	
	private Class<I> clazz;
	private boolean gzip;
	private String extension;
	
	public JsonFileReader(Class<I> clazz, boolean gzip) {
		this.clazz = clazz;
		this.gzip = gzip;
		if (gzip) {
			this.extension = "json.gz";
		} else {
			this.extension = "json";
		}
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public String getExtension() {
		return this.extension;
	}

	@Override
	public I readFile(File file) throws IOException {
		InputStream in = null;
		if (this.gzip) {
			in = new GZIPInputStream(new FileInputStream(file));
		} else {
			in = new FileInputStream(file);
		}
		return this.objectMapper.readValue(in, this.clazz);
	}

}
