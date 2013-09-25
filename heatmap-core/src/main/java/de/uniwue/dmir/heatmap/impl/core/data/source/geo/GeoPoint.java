package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import lombok.Data;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;

@Data
public class GeoPoint {
	
	private GeoCoordinates geoCoordinates;
	
	public GeoPoint() {
		this(0, 0);
	}
	
	public GeoPoint(double longitude, double latitude) {
		this.geoCoordinates = new GeoCoordinates(longitude, latitude);
	}
}