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
package de.uniwue.dmir.heatmap.point.sources.geo.datasources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniwue.dmir.heatmap.point.types.geo.SimpleGeoPoint;
import lombok.Getter;
import lombok.Setter;

public class CsvGeoDatasource
extends ListGeoDatasource<String> {

//	public static enum FIELD_TYPE {
//		DOUBLE,
//		INT,
//		STRING,
//		DATE
//	};

	
	@Getter
	@Setter
	private SimpleDateFormat dateFormat = 
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public CsvGeoDatasource(
			File file, 
			String separator, 
			boolean skipFirstLine) 
	throws IOException {
		
		BufferedReader bufferedReader = 
				new BufferedReader(new FileReader(file));
		
		if (skipFirstLine) {
			bufferedReader.readLine();
		}
		
		String line = bufferedReader.readLine();
		while (line != null) {

			String[] split = line.split(separator);
			
			SimpleGeoPoint<String> geoPoint = new SimpleGeoPoint<String>();
			
			double longitude = Double.parseDouble(split[0]);
			geoPoint.getGeoCoordinates().setLongitude(longitude);
			
			double latitude = Double.parseDouble(split[1]);
			geoPoint.getGeoCoordinates().setLatitude(latitude);
			
			if (split.length > 2) {
				double value = Double.parseDouble(split[2]);
				geoPoint.setValue(value);
			}
			
			if (split.length > 3) {
				Date timestamp;
				try {
					timestamp = this.dateFormat.parse(split[3]);
				} catch (ParseException e) {
					bufferedReader.close();
					throw new IOException(e);
				}
				geoPoint.setTimestamp(timestamp);
			}
			
			if (split.length > 4) {
				String groupId = split[4];
				geoPoint.setGroupDescription(groupId);
			}
			
			this.getList().add(geoPoint);
			
			line = bufferedReader.readLine();
//			System.out.println(geoPoint);
		}
		
		bufferedReader.close();
		
	}

}
