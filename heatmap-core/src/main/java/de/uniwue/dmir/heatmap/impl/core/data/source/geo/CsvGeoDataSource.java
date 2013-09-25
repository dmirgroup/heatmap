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
