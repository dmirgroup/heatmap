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
package de.uniwue.dmir.heatmap.filters.apic;

import java.awt.geom.Path2D;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.types.geo.ApicGeoPoint;

@AllArgsConstructor
public class PointToCityMapper 
implements IMapper<ApicGeoPoint, String> {

	private Map<String, Path2D> cityToPath2DMap;

	@Override
	public String map(ApicGeoPoint object) {
		
		GeoCoordinates geoCoordinates = object.getGeoCoordinates();
		if (geoCoordinates == null) {
			return null;
		}
		
		double x = geoCoordinates.getLongitude();
		double y = geoCoordinates.getLatitude();

		for (Entry<String, Path2D> e : this.cityToPath2DMap.entrySet()) {
			if (e.getValue().contains(x, y)) {
				return e.getKey();
			}
		}
		
		return null;
	}
}
