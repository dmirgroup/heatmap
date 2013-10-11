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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.ArrayTileFactory;
import de.uniwue.dmir.heatmap.core.Heatmap;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoTileDataSource;
import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;
import de.uniwue.dmir.heatmap.core.processing.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.core.processing.VisualizationFileWriter;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToGeoCoordinateMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToValuePixelMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.MercatorMapProjection;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.Sum;
import de.uniwue.dmir.heatmap.impl.core.data.type.mapper.ValuePixelToSumMapper;
import de.uniwue.dmir.heatmap.impl.core.filter.ImageFilter;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumAdder;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumScalarMultiplier;
import de.uniwue.dmir.heatmap.impl.core.visualizer.ImageColorScheme;
import de.uniwue.dmir.heatmap.impl.core.visualizer.SumAlphaVisualizer;

public class SpringTest2 {

	@Test
	public void testHeatmap() throws IOException {
		
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(
						"spring/example/settings.xml");
		
		@SuppressWarnings("unchecked")
		IGeoDataSource<GeoPoint> dataSource = 
				appContext.getBean(IGeoDataSource.class);
		
		HeatmapSettings settings = new HeatmapSettings();
		settings.getZoomLevelRange().setMax(7);
		
		GeoTileDataSource<GeoPoint, ValuePixel> dataSouce =
				new GeoTileDataSource<GeoPoint, ValuePixel>(
						dataSource,
						new MercatorMapProjection(
								settings.getTileSize(),
								settings.getZoomLevelMapper()), 
						new GeoPointToGeoCoordinateMapper(), 
						new GeoPointToValuePixelMapper());
		
		IFilter<ValuePixel, Sum[]> filter = 
				new ImageFilter<ValuePixel, Sum>(
						new ValuePixelToSumMapper(),
						new SumAdder(),
						new SumScalarMultiplier(),
						ImageIO.read(new File("src/main/resources/filter/dot13_black.png")));
		
//		System.out.println(dataSouce.getData(new TileCoordinates(6, 10, 5), filter));
		
//		IFilter<ValuePixel, SumAndSize> filter = 
//				new ErodeFilter<ValuePixel, SumAndSize>(
//						new ValuePixelToSumAndSizeMapper(),
//						new SumAndSizeAdder(),
//						42, 42, 21, 21);
		
		IHeatmap<Sum[]> heatmap = new Heatmap<ValuePixel, Sum[]>(
				new ArrayTileFactory<Sum>(Sum.class),
				dataSouce, 
				filter, 
				settings);
		
		BufferedImage colorScheme = ImageIO.read(
				new File("src/main/resources/color-schemes/classic_alpha70.png"));
		
		double[] ranges = ImageColorScheme.ranges(1, 500, colorScheme.getHeight());
//		System.out.println(Arrays.toString(ranges));
		
//		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeBinaryVisualizer(),
		SumAlphaVisualizer visualizer = new SumAlphaVisualizer(
				colorScheme, ranges);
		visualizer.setAlphaValue(0.2f);
		visualizer.setForceAlphaValue(true);

		VisualizationFileWriter<Sum[]> heatmapFileWriter =
				new VisualizationFileWriter<Sum[]>(
						new DefaultFileStrategy("out/tiles"), 
						"png",
						visualizer);
		
		heatmap.processTiles(heatmapFileWriter);

		appContext.close();
	}
}
