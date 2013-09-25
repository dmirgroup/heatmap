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
package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvGeoDataSource 
extends ListGeoDataSource {

	public CsvGeoDataSource(File file, String separator, boolean skipFirstLine) 
	throws IOException {
		
		BufferedReader bufferedReader = 
				new BufferedReader(new FileReader(file));
		
		if (skipFirstLine) {
			bufferedReader.readLine();
		}
		
		String line = bufferedReader.readLine();
		while (line != null) {
			
			String[] split = line.split(separator);
			double longitude = Double.parseDouble(split[0]);
			double latitude = Double.parseDouble(split[1]);
			
			GeoPoint geoPoint = new GeoPoint(longitude, latitude);
			this.getList().add(geoPoint);
			
			line = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		
	}

}
