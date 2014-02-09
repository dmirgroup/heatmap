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
package de.uniwue.dmir.heatmap.util;

import java.awt.Polygon;

import org.springframework.core.io.ClassPathResource;

import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.projections.EquidistantProjection;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.distances.GreatCircleDistance;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.beans.EquidistantProjectionTileSizeFactoryBean;
import de.uniwue.dmir.heatmap.util.beans.GeoPolygonFactoryBean;
import de.uniwue.dmir.heatmap.util.beans.PolygonFromGeoPolygonFacoryBean;

public class Test {
	public static void main(String[] args) throws Exception {
		
		GeoPolygonFactoryBean geoPolygonFactoryBean = 
				new GeoPolygonFactoryBean(
						new ClassPathResource(
								"spring/example/points/areas/polygon-turin-p3.json"));
		
		GeoPolygon geoPolygon = geoPolygonFactoryBean.getObject();
		
		IDistanceFunction<GeoCoordinates> distanceFunction = 
				new GreatCircleDistance.Haversine();
				
		EquidistantProjectionTileSizeFactoryBean equidistantProjectionTileSizeFactoryBean =
				new EquidistantProjectionTileSizeFactoryBean(
						10., 
						10., 
						false, 
						geoPolygon.getGeoBoundingBox(), 
						distanceFunction);
		
		TileSize tileSize = equidistantProjectionTileSizeFactoryBean.getObject();
		
		EquidistantProjection equidistantProjection =
				new EquidistantProjection(
						geoPolygon.getGeoBoundingBox(), 
						tileSize);
		
		PolygonFromGeoPolygonFacoryBean polygonFromGeoPolygonFacoryBean =
				new PolygonFromGeoPolygonFacoryBean(
						geoPolygon, 
						new TileCoordinates(0, 0, 0), 
						equidistantProjection);
		
		Polygon polygon = polygonFromGeoPolygonFacoryBean.getObject();
		
		int pixelsInsidePolygon = 0;
		for (int x = 0; x < tileSize.getWidth(); x ++) {
			for (int y = 0; y < tileSize.getHeight(); y ++) {
				if (polygon.contains(x, y)) {
					pixelsInsidePolygon ++;
				}
			}
		}
		
		System.out.println(
				pixelsInsidePolygon 
				+ " / " + tileSize.getWidth() * tileSize.getHeight());

		System.out.println(
				pixelsInsidePolygon * 10. * 10 / 1000 / 1000
				+ " / " + tileSize.getWidth() * tileSize.getHeight() * 10. / 1000 / 1000);
	}
}
