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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.uniwue.dmir.heatmap.core.ArrayTileFactory;
import de.uniwue.dmir.heatmap.core.Heatmap;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoTileDataSource;
import de.uniwue.dmir.heatmap.core.processing.HeatmapFileWriter;
import de.uniwue.dmir.heatmap.core.processing.HeatmapFileWriter.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.CsvGeoDataSource;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToGeoCoordinateMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToValuePixelMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.MercatorMapProjection;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;
import de.uniwue.dmir.heatmap.impl.core.data.type.mapper.ValuePixelToSumAndSizeMapper;
import de.uniwue.dmir.heatmap.impl.core.filter.ImageFilter;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumAndSizeAdder;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumAndSizeScalarMultiplier;
import de.uniwue.dmir.heatmap.impl.core.visualizer.SumAndSizeAlphaVisualizer;

public class HeatmapTest {

	@Test
	public void testHeatmap() throws IOException {
		
		HeatmapSettings settings = new HeatmapSettings();
		settings.getZoomLevelRange().setMax(7);
		
		GeoTileDataSource<GeoPoint, ValuePixel> dataSouce =
				new GeoTileDataSource<GeoPoint, ValuePixel>(
						new CsvGeoDataSource(
								new File("src/test/resources/lonlat.txt"),
								",",
								false), 
						new MercatorMapProjection(
								settings.getTileSize(),
								settings.getZoomLevelMapper()), 
						new GeoPointToGeoCoordinateMapper(), 
						new GeoPointToValuePixelMapper());
		
		IFilter<ValuePixel, SumAndSize[]> filter = 
				new ImageFilter<ValuePixel, SumAndSize>(
						new ValuePixelToSumAndSizeMapper(),
						new SumAndSizeAdder(),
						new SumAndSizeScalarMultiplier(),
						ImageIO.read(new File(
								"src/main/resources/filter/dot13_black.png")));
		
//		System.out.println(dataSouce.getData(new TileCoordinates(6, 10, 5), filter));
		
//		IFilter<ValuePixel, SumAndSize> filter = 
//				new ErodeFilter<ValuePixel, SumAndSize>(
//						new ValuePixelToSumAndSizeMapper(),
//						new SumAndSizeAdder(),
//						42, 42, 21, 21);
		
		IHeatmap<ValuePixel, SumAndSize[]> heatmap =
				new Heatmap<ValuePixel, SumAndSize[]>(
						new ArrayTileFactory<SumAndSize>(SumAndSize.class),
						dataSouce, 
						filter, 
						settings);
		
		BufferedImage colorScheme = ImageIO.read(
				new File("src/main/resources/color-schemes/classic_70.png"));
		
		double[] ranges = SumAndSizeAlphaVisualizer.ranges(1, 500, colorScheme.getHeight());
		
//		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeBinaryVisualizer(),
		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeAlphaVisualizer(
				colorScheme, ranges);
		visualizer.setAlphaValue(0.5f);
		visualizer.setBackgroundColor(
				new Color(colorScheme.getRGB(0, colorScheme.getHeight() - 1), true));
//		visualizer.setForceAlphaValue(true);

		HeatmapFileWriter<SumAndSize[]> heatmapFileWriter =
				new HeatmapFileWriter<SumAndSize[]>(
						visualizer,
						new DefaultFileStrategy("out/tiles"), 
						"png");
		
		heatmap.processTiles(heatmapFileWriter);
	}
}
