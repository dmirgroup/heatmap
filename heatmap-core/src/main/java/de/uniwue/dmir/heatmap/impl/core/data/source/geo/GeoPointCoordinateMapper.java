package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.IToGeoCoordinatesMapper;

public class GeoPointCoordinateMapper 
implements IToGeoCoordinatesMapper<GeoPoint> {

	public GeoCoordinates map(GeoPoint object) {
		return object.getGeoCoordinates();
	}
	
}