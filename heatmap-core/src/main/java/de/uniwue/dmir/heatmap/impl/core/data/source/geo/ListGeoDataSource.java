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
