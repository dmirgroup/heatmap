package de.uniwue.dmir.heatmap.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.Data;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;

@Data
public class GeoPolygon {

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
		
		this.minLatitude = Double.POSITIVE_INFINITY;
		this.minLongitude = Double.POSITIVE_INFINITY;
		
		this.maxLatitude = Double.NEGATIVE_INFINITY;
		this.maxLongitude = Double.NEGATIVE_INFINITY;
		
		for (int i = 0; i < p.length; i++) {
			
			double value = this.p[i];
			
			if ((i + 1) % 2 == 1) {
				this.minLatitude = Math.min(value, this.minLatitude);
				this.maxLatitude = Math.max(value, this.maxLatitude);
			} else {
				this.minLongitude = Math.min(value, this.minLongitude);
				this.maxLongitude = Math.max(value, this.maxLongitude);
			}
		}
		
		this.geoBoundingBox = new GeoBoundingBox(
				new GeoCoordinates(this.minLongitude, this.maxLatitude), 
				new GeoCoordinates(this.maxLongitude, this.minLatitude));
		
	}
	
	public static GeoPolygon load(String file) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		return load(new FileInputStream(file));
	}

	public static GeoPolygon load(InputStream inputStream) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		GeoPolygon geoPolygon = 
				objectMapper.readValue(
						inputStream, 
						GeoPolygon.class);
		geoPolygon.calculateExtrema();
		return geoPolygon;
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		GeoPolygon geoPolygon = load("src/test/resources/polygon-london.json");
		System.out.println(geoPolygon);
	}
}
