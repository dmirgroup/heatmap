package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.mapper.GeoPointMapper;

public class DatabaseGeoDataSource implements IGeoDataSource<GeoPoint> {

	private RequestGeo request;
	
	@Autowired
	private GeoPointMapper mapper;
	
	public DatabaseGeoDataSource(RequestGeo request) {
		
		if (request.getTable() == null 
				&& request.getLongitudeAttribute() == null 
				&& request.getLatitudeAttribute() == null) {
			
			throw new IllegalArgumentException("");
		}
		
		this.request = request;
	}
	
	@Override
	public List<GeoPoint> getData(
			double westLon, double northLat,
			double eastLon, double southLat) {
		
		this.request.setLatNorth(northLat);
		this.request.setLatSouth(southLat);
		this.request.setLonEast(eastLon);
		this.request.setLonWest(westLon);
		
		return this.mapper.getData(this.request);
	}

}
