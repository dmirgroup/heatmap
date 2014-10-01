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

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.point.types.geo.SimpleGeoPoint;

public class ListGeoDatasource<TGroupDescription> 
implements IGeoDatasource<SimpleGeoPoint<TGroupDescription>, Object> {

	@Getter
	private List<SimpleGeoPoint<TGroupDescription>> list;
	
	public ListGeoDatasource() {
		this.list = new ArrayList<SimpleGeoPoint<TGroupDescription>>();
	}
	
	public ListGeoDatasource(List<SimpleGeoPoint<TGroupDescription>> list) {
		this.list = list;
	}
	
	public List<SimpleGeoPoint<TGroupDescription>> getData(
			GeoBoundingBox geoBoundingBox,
			Object parameters) {
		
		// return everything if no bounding box is given
		if (geoBoundingBox == null) {
			return new ArrayList<SimpleGeoPoint<TGroupDescription>>(this.list);
		}
		
		// calculate result
		List<SimpleGeoPoint<TGroupDescription>> result = new ArrayList<SimpleGeoPoint<TGroupDescription>>();
		for (SimpleGeoPoint<TGroupDescription> geoPoint : this.list) {
			if (
					geoPoint.getGeoCoordinates().getLongitude() 
						>= geoBoundingBox.getMin().getLongitude()
					&& geoPoint.getGeoCoordinates().getLongitude() 
						<= geoBoundingBox.getMax().getLongitude()
					&& geoPoint.getGeoCoordinates().getLatitude() 
						>= geoBoundingBox.getMin().getLatitude()
					&& geoPoint.getGeoCoordinates().getLatitude() 
						<= geoBoundingBox.getMax().getLatitude()) {
				result.add(geoPoint);
			}
		}
		
		return result;
	}
}
