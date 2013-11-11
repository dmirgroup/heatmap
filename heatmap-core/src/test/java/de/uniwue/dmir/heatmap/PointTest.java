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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.uniwue.dmir.heatmap.core.HeatmapSettings;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.ITileFactory;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.GeoHeatmapDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.sources.CsvGeoDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.GeoPoint;
import de.uniwue.dmir.heatmap.core.data.sources.geo.mappers.GeoPointToGeoCoordinateMapper;
import de.uniwue.dmir.heatmap.core.data.sources.geo.projections.EquidistantProjection;
import de.uniwue.dmir.heatmap.core.filters.PointFilter;
import de.uniwue.dmir.heatmap.core.filters.ProxyGroupFilter;
import de.uniwue.dmir.heatmap.core.filters.access.MapGroupAccess;
import de.uniwue.dmir.heatmap.core.filters.access.MapPixelAccess;
import de.uniwue.dmir.heatmap.core.heatmaps.DefaultHeatmap;
import de.uniwue.dmir.heatmap.core.processors.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.PointProcessor;
import de.uniwue.dmir.heatmap.core.processors.PolygonRelativeCoordinatesFilter;
import de.uniwue.dmir.heatmap.core.processors.ProxyFilteredKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.processors.ProxyFilteredKeyValueIteratorFactory.CombinedKeyValueFilter;
import de.uniwue.dmir.heatmap.core.processors.ProxyGroupFileWriter;
import de.uniwue.dmir.heatmap.core.processors.VisualizationFileWriterProcessor;
import de.uniwue.dmir.heatmap.core.processors.mappers.IdentityMapper;
import de.uniwue.dmir.heatmap.core.processors.mappers.StringReplaceMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.ImageColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.MapKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.processors.visualizers.SimpleVisualizer;
import de.uniwue.dmir.heatmap.core.processors.visualizers.StaticPolygonProxyVisualizer;
import de.uniwue.dmir.heatmap.core.processors.visualizers.colors.CombinedColorPipe;
import de.uniwue.dmir.heatmap.core.processors.visualizers.colors.SimpleColorPipe;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.GreatCircleDistance;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.factories.GenericMapTileFactory;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;

public class PointTest {

	public static final GeoBoundingBox TEST = new GeoBoundingBox(
			new GeoCoordinates(-30, -30), 
			new GeoCoordinates(+30, +30));
	
	@Test
	public void testHeatmap() throws IOException {
		
		HeatmapSettings settings = new HeatmapSettings();
		settings.getZoomLevelRange().setMax(0);
		
		GeoPolygon geoPolygon = GeoPolygon.load(
				"src/main/resources/spring/example/points/polygon-london.json");
		GeoBoundingBox gbb = geoPolygon.getGeoBoundingBox();
		System.out.println(gbb);
		
		System.out.println(
				new GreatCircleDistance.Haversine().distance(
						gbb.getSouthEast(), 
						new GeoCoordinates(
								gbb.getNorthWest().getLongitude(), 
								gbb.getSouthEast().getLatitude())));
		
		TileSize tileSize = EquidistantProjection.getTileSize(
				10, 
				10, 
				true, 
				gbb, 
				new GreatCircleDistance.Haversine());
		System.out.println(tileSize);
		
		settings.setTileSize(tileSize);
		
		EquidistantProjection projection = new EquidistantProjection(
				gbb,
				tileSize);
		
//		RequestGeo requestGeo = new RequestGeo("data_airprobe", "geo_lon", "geo_lat");
//		
//		requestGeo.setLonWest(gbb.getNorthWest().getLongitude());
//		requestGeo.setLatNorth(gbb.getNorthWest().getLatitude());
//		requestGeo.setLonEast(gbb.getSouthEast().getLatitude());
//		requestGeo.setLatSouth(gbb.getSouthEast().getLatitude());
//		
//		requestGeo.setTimestampAttribute("meta_timestamp_recorded");
//		requestGeo.setOrderAsc(false);
//		requestGeo.setGroupAttribute("meta_install_id");
//		requestGeo.setValueAttribute("data_bc_1");
		
		GeoHeatmapDatasource<GeoPoint<String>, GroupValuePixel> dataSouce =
				new GeoHeatmapDatasource<GeoPoint<String>, GroupValuePixel>(
						new CsvGeoDatasource(
								new File("src/test/resources/test_london.txt"),
								",",
								false),
						projection, 
						new GeoPointToGeoCoordinateMapper<String>(), 
						new GeoPointToGroupValuePixelMapper<String>(
								new IdentityMapper<String>()));
		
		IFilter<GroupValuePixel, Map<RelativeCoordinates, PointSize>> filter = 
				new PointFilter<Map<RelativeCoordinates,PointSize>>(
						new MapPixelAccess<PointSize>());

		
		ITileFactory<Map<RelativeCoordinates, PointSize>> tileFactory = 
				new MapTileFactory<PointSize>();

		ProxyGroupFilter<
						GroupValuePixel, 
						Map<RelativeCoordinates, PointSize>, 
						Map<String, Map<RelativeCoordinates, PointSize>>> groupFilter = 
				new ProxyGroupFilter<
					GroupValuePixel, 
					Map<RelativeCoordinates, PointSize>, 
					Map<String, Map<RelativeCoordinates, PointSize>>>(
						new MapGroupAccess<Map<RelativeCoordinates, PointSize>>(tileFactory),
						filter);
		groupFilter.setOverallGroup("11OVER!9900ALL");
		
		IHeatmap<Map<String, Map<RelativeCoordinates, PointSize>>> heatmap =
				new DefaultHeatmap<GroupValuePixel, Map<String, Map<RelativeCoordinates, PointSize>>>(
						new GenericMapTileFactory<String, Map<RelativeCoordinates, PointSize>>(),
						dataSouce, 
						groupFilter,
						settings);

		System.out.println(dataSouce.getData(new TileCoordinates(0, 0, 0), filter).next());
				
		Map<String, Map<RelativeCoordinates, PointSize>> tile = 
				heatmap.getTile(new TileCoordinates(0, 0, 0));
		System.out.println(tile);
		
		Polygon polygon = PolygonRelativeCoordinatesFilter.fromGeoPolygon(
				geoPolygon, 
				null, 
				projection);
		
		ProxyFilteredKeyValueIteratorFactory<Map<RelativeCoordinates, PointSize>, RelativeCoordinates, PointSize> pixelIterator = 
				new ProxyFilteredKeyValueIteratorFactory<Map<RelativeCoordinates, PointSize>, RelativeCoordinates, PointSize>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, PointSize>(),
						new CombinedKeyValueFilter<RelativeCoordinates, PointSize>(
									new PolygonRelativeCoordinatesFilter(
											polygon)));
		
		PointProcessor<Map<String, Map<RelativeCoordinates, PointSize>>, Map<RelativeCoordinates, PointSize>> pointProcessor = 
				new PointProcessor<Map<String, Map<RelativeCoordinates, PointSize>>, Map<RelativeCoordinates, PointSize>>(
						"out/points.json", 
						new StringReplaceMapper(":", ""),
						new MapKeyValueIteratorFactory<String, Map<RelativeCoordinates, PointSize>>(),
						pixelIterator);
		
		BufferedImage colorImage = ImageIO.read(
				new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = ImageColorScheme.ranges(1, 48, colorImage.getHeight());
		
		SimpleVisualizer<Map<RelativeCoordinates, PointSize>, PointSize> simpleVisualizer = 
				new SimpleVisualizer<Map<RelativeCoordinates,PointSize>, PointSize>(
						pixelIterator,
						new CombinedColorPipe<PointSize>(
								new SimpleColorPipe<PointSize>(
										new IToDoubleMapper<PointSize>() {
											@Override
											public Double map(PointSize object) {
												return object.getPoints();
											}
										},
										new ImageColorScheme(colorImage, ranges)),
								null));
		
		StaticPolygonProxyVisualizer<Map<RelativeCoordinates, PointSize>> visualizer =
				new StaticPolygonProxyVisualizer<Map<RelativeCoordinates,PointSize>>(simpleVisualizer, polygon);
		
		VisualizationFileWriterProcessor<Map<RelativeCoordinates, PointSize>> fileWriter =
				new VisualizationFileWriterProcessor<Map<RelativeCoordinates,PointSize>>(
						"", 
						new DefaultFileStrategy(), 
						"png", 
						visualizer);
		
		ProxyGroupFileWriter<Map<RelativeCoordinates, PointSize>, Map<String, Map<RelativeCoordinates, PointSize>>> groupProxyFileWriter =
				new ProxyGroupFileWriter<Map<RelativeCoordinates,PointSize>, Map<String,Map<RelativeCoordinates,PointSize>>>(
						new MapKeyValueIteratorFactory<String, Map<RelativeCoordinates,PointSize>>(), 
						fileWriter, 
						"out/groups");
		groupProxyFileWriter.setGroupIdMapper(new StringReplaceMapper("[^\\w]", ""));
		
		heatmap.processTiles(pointProcessor);
		heatmap.processTiles(groupProxyFileWriter);
		
	}
}
