package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;

public class ListGeoDataSource implements IGeoDataSource<GeoPoint> {

	@Getter
	private List<GeoPoint> list;
	
	public ListGeoDataSource() {
		this.list = new ArrayList<GeoPoint>();
	}
	
	public ListGeoDataSource(List<GeoPoint> list) {
		this.list = list;
	}
	
	public List<GeoPoint> getData(
			double westLon,
			double northLat, 
			double eastLon, 
			double southLat) {
		
		List<GeoPoint> result = new ArrayList<GeoPoint>();
		for (GeoPoint geoPoint : this.list) {
			if (geoPoint.getGeoCoordinates().getLongitude() > westLon
					&& geoPoint.getGeoCoordinates().getLongitude() < eastLon
					&& geoPoint.getGeoCoordinates().getLatitude() > southLat
					&& geoPoint.getGeoCoordinates().getLatitude() < northLat) {
				result.add(geoPoint);
			}
		}
		
		return result;
	}
}
