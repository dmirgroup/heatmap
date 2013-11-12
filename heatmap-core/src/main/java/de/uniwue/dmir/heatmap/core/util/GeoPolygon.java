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
package de.uniwue.dmir.heatmap.core.util;

import java.awt.geom.Path2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.Data;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;

@Data
public class GeoPolygon {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@JsonProperty("cLa")
	private double cLa;
	
	@JsonProperty("cLo")
	private double cLo;
	
	private double z;

	@JsonProperty("pLa")
	private double pLa;
	
	@JsonProperty("pLo")
	private double pLo;
	
	private double[] p;
	
	private double minLatitude;
	private double maxLatitude;
	
	private double minLongitude;
	private double maxLongitude;
	
	private GeoBoundingBox geoBoundingBox;
	
	public void calculateExtrema() {
		
		this.logger.debug("Calculating extrema.");
		
		this.minLatitude = Double.POSITIVE_INFINITY;
		this.minLongitude = Double.POSITIVE_INFINITY;
		
		this.maxLatitude = Double.NEGATIVE_INFINITY;
		this.maxLongitude = Double.NEGATIVE_INFINITY;
		
		for (int i = 0; i < this.p.length; i++) {
			
			double value = this.p[i];
			
			if ((i + 1) % 2 == 1) { // lat, lon pairs
				this.minLatitude = Math.min(value, this.minLatitude);
				this.maxLatitude = Math.max(value, this.maxLatitude);
			} else {
				this.minLongitude = Math.min(value, this.minLongitude);
				this.maxLongitude = Math.max(value, this.maxLongitude);
			}
		}
		
		this.geoBoundingBox = new GeoBoundingBox(
				new GeoCoordinates(this.minLongitude, this.minLatitude), 
				new GeoCoordinates(this.maxLongitude, this.maxLatitude));
		
		this.logger.debug("Extrema: {}", this.geoBoundingBox);
	}
	

	public Path2D getPath2D(boolean lonlat) {
		
		Path2D path = new Path2D.Double();
		
		if (this.p.length > 2) {

			double latitude = this.p[0];
			double longitude = this.p[1];
			
			double firstX;
			double firstY;
			
			if (lonlat) {
				firstX = longitude;
				firstY = latitude;
			} else {
				firstX = latitude;
				firstY = longitude;
			}
			
			path.moveTo(firstX, firstY);
			
			for (int i = 1; i < this.p.length / 2; i++) {
				
				latitude = this.p[2 * i];
				longitude = this.p[2 * i + 1];
				
				double x;
				double y;
				if (lonlat) {
					x = longitude;
					y = latitude;
				} else {
					x = latitude;
					y = longitude;
				}

				path.lineTo(x, y);
			}
			
			path.lineTo(firstX, firstY);
		}
		
		return path;
	}
	
	public static GeoPolygon load(String file) 
	throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		return load(new FileInputStream(file));
	}

	public static GeoPolygon load(InputStream inputStream) 
	throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		GeoPolygon geoPolygon = 
				objectMapper.readValue(
						inputStream, 
						GeoPolygon.class);
		geoPolygon.calculateExtrema();
		return geoPolygon;
	}
	
	public static void main(String[] args) 
	throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {

		GeoPolygon geoPolygon = load("src/main/resources/spring/example/points/polygon-london.json");
		System.out.println(geoPolygon);
		
	}
}
