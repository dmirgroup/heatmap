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
package de.uniwue.dmir.heatmap.core.processors;

import java.awt.Polygon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.processors.ProxyFilteredKeyValueIteratorFactory.IObjectFilter;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;

@AllArgsConstructor
public class PolygonRelativeCoordinatesFilter 
implements IObjectFilter<RelativeCoordinates> {

	@Getter
	private Polygon polygon;
	
	@Override
	public boolean isToBeDisregarded(RelativeCoordinates object) {

		boolean disregard = !this.polygon.contains(
				object.getX(), 
				object.getY());
		
//		System.out.println("FILTER");
//		System.out.println(object);
//		System.out.println(disregard);
		
		return disregard;
	}
	
	public static final Polygon fromGeoPolygon(
		GeoPolygon geoPolygon,
		TileCoordinates tileCoordinates,
		IMapProjection mapProjection) {

		int length = geoPolygon.getP().length / 2;
		
		int[] x = new int[length];
		int[] y = new int[length];
		for (int i = 0; i < length; i++) {
			RelativeCoordinates relativeCoordinates =
					mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(
									geoPolygon.getP()[2 * i + 1], 
									geoPolygon.getP()[2 * i + 0]), 
							null);
			
			x[i] = relativeCoordinates.getX();
			y[i] = relativeCoordinates.getY();
		}
		
		return new Polygon(x, y, length);
	}
}
