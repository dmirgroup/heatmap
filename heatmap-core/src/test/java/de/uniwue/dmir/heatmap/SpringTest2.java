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


public class SpringTest2 {

//	@Test
//	public void testHeatmap() throws IOException {
//		
//		ClassPathXmlApplicationContext appContext = 
//				new ClassPathXmlApplicationContext(
//						"spring/example/settings.xml");
//		
//		@SuppressWarnings("unchecked")
//		IGeoDatasource<GeoPoint<String>> dataSource = 
//				appContext.getBean(IGeoDatasource.class);
//		
//		HeatmapSettings settings = new HeatmapSettings();
//		settings.getZoomLevelRange().setMax(7);
//		
//		GeoHeatmapDatasource<GeoPoint<String>> dataSouce =
//				new GeoHeatmapDatasource<GeoPoint<String>>(
//						dataSource,
//						new MercatorMapProjection(
//								settings.getTileSize(),
//								settings.getZoomLevelMapper()), 
//						new GeoPointToGeoCoordinateMapper<GeoPoint<String>>());
//		
//		IFilter<ValuePixel, Sum[]> filter = 
//				new ImageFilter<ValuePixel, Sum>(
//						new ValuePixelToSumMapper(),
//						new SumAdder(),
//						new SumScalarMultiplier(),
//						ImageIO.read(new File("src/main/resources/filter/dot13_black.png")));
//		
////		System.out.println(dataSouce.getData(new TileCoordinates(6, 10, 5), filter));
//		
////		IFilter<ValuePixel, SumAndSize> filter = 
////				new ErodeFilter<ValuePixel, SumAndSize>(
////						new ValuePixelToSumAndSizeMapper(),
////						new SumAndSizeAdder(),
////						42, 42, 21, 21);
//		
//		IHeatmap<Sum[]> heatmap = new DefaultHeatmap<ValuePixel, Sum[]>(
//				new ArrayTileFactory<Sum>(Sum.class),
//				dataSouce, 
//				filter, 
//				settings);
//		
//		BufferedImage colorImage = ImageIO.read(
//				new File("src/main/resources/color-schemes/classic_alpha70.png"));
//		
//		double[] ranges = ImageColorScheme.ranges(1, 500, colorImage.getHeight());
//
//		IColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
//		
//		//		System.out.println(Arrays.toString(ranges));
//		
////		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeBinaryVisualizer(),
//		SumAlphaVisualizer visualizer = new SumAlphaVisualizer(colorScheme);
//		visualizer.setAlphaValue(0.2f);
//		visualizer.setForceAlphaValue(true);
//
//		VisualizationFileWriterProcessor<Sum[]> heatmapFileWriter =
//				new VisualizationFileWriterProcessor<Sum[]>(
//						"out/tiles",
//						new DefaultFileStrategy(), 
//						"png",
//						visualizer);
//		
//		heatmap.processTiles(heatmapFileWriter);
//
//		appContext.close();
//	}
}
