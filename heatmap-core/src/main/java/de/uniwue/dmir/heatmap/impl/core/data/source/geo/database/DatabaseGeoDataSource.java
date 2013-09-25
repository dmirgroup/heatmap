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
