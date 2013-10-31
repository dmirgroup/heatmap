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
package de.uniwue.dmir.heatmap;

import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import de.uniwue.dmir.heatmap.core.Heatmap;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.MapTileFactory;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoTileDataSource;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.CsvGeoDataSource;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.EquidistantProjection;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToGeoCoordinateMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToGroupValuePixelMapper;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.GroupValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.PointSize;
import de.uniwue.dmir.heatmap.impl.core.filter.MapPixelAccess;
import de.uniwue.dmir.heatmap.impl.core.filter.PointFilter;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.GreatCircleDistance;

public class PointTest {

	@Test
	public void testHeatmap() throws IOException {
		
		HeatmapSettings settings = new HeatmapSettings();
		settings.getZoomLevelRange().setMax(0);
		
		GeoBoundingBox gbb = new GeoBoundingBox(
				new GeoCoordinates(-30,  30), 
				new GeoCoordinates( 30, -30));
		
		TileSize tileSize = EquidistantProjection.getTileSize(
				10000, 
				10000, 
				false, 
				gbb, 
				new GreatCircleDistance.Haversine());
		System.out.println(tileSize);
		
		GeoTileDataSource<GeoPoint, GroupValuePixel> dataSouce =
				new GeoTileDataSource<GeoPoint, GroupValuePixel>(
						new CsvGeoDataSource(
								new File("src/test/resources/test.txt"),
								",",
								false), 
						new EquidistantProjection(
								gbb,
								tileSize), 
						new GeoPointToGeoCoordinateMapper(), 
						new GeoPointToGroupValuePixelMapper());
		
		IFilter<GroupValuePixel, Map<RelativeCoordinates, PointSize>> filter = 
				new PointFilter<Map<RelativeCoordinates,PointSize>>(
						new MapPixelAccess<PointSize>());
		
		IHeatmap<Map<RelativeCoordinates, PointSize>> heatmap =
				new Heatmap<GroupValuePixel, Map<RelativeCoordinates, PointSize>>(
						new MapTileFactory<PointSize>(),
						dataSouce, 
						filter, 
						settings);
				
		Map<RelativeCoordinates, PointSize> tile = heatmap.getTile(new TileCoordinates(0, 0, 0));
		System.out.println(tile);
		
	}
}
