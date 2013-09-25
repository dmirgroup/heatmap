package de.uniwue.dmir.heatmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.Heatmap;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.ZoomLevelRange;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.DefaultHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoTileDataSource;
import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;
import de.uniwue.dmir.heatmap.core.processing.HeatmapFileWriter;
import de.uniwue.dmir.heatmap.core.processing.HeatmapFileWriter.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.core.processing.NonEmptyTileIterator;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointCoordinateMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPointToValuePixelMapper;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.MercatorMapProjection;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;
import de.uniwue.dmir.heatmap.impl.core.data.type.mapper.ValuePixelToSumAndSizeMapper;
import de.uniwue.dmir.heatmap.impl.core.filter.ImageFilter;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumAndSizeAdder;
import de.uniwue.dmir.heatmap.impl.core.filter.operators.SumAndSizeScalarMultiplier;
import de.uniwue.dmir.heatmap.impl.core.visualizer.SumAndSizeAlphaVisualizer;

public class SpringTest2 {

	@Test
	public void testHeatmap() throws IOException {
		
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(
						"spring/applicationContext.xml");
		
		@SuppressWarnings("unchecked")
		IGeoDataSource<GeoPoint> dataSource = 
				appContext.getBean(IGeoDataSource.class);
		
//		RTreeGeoDataSource<GeoPoint> rTreeGeoDataSource = 
//				new RTreeGeoDataSource<GeoPoint>(dataSource, new GeoPointCoordinateMapper());
		
		IHeatmapDimensions dimensions = new DefaultHeatmapDimensions();
		
		GeoTileDataSource<GeoPoint, ValuePixel> dataSouce =
				new GeoTileDataSource<GeoPoint, ValuePixel>(
						dataSource,
						new MercatorMapProjection(dimensions), 
						new GeoPointCoordinateMapper(), 
						new GeoPointToValuePixelMapper());
		
		
		IFilter<ValuePixel, SumAndSize> filter = 
				new ImageFilter<ValuePixel, SumAndSize>(
						new ValuePixelToSumAndSizeMapper(),
						new SumAndSizeAdder(),
						new SumAndSizeScalarMultiplier(),
						ImageIO.read(new File("src/main/resources/filter/dot13_black.png")));
		
//		System.out.println(dataSouce.getData(new TileCoordinates(6, 10, 5), filter));
		
//		IFilter<ValuePixel, SumAndSize> filter = 
//				new ErodeFilter<ValuePixel, SumAndSize>(
//						new ValuePixelToSumAndSizeMapper(),
//						new SumAndSizeAdder(),
//						42, 42, 21, 21);
		ZoomLevelRange zoomLevelRange = new ZoomLevelRange(0, 10);
		
		IHeatmap<ValuePixel, SumAndSize> heatmap = new Heatmap<ValuePixel, SumAndSize>(
				dataSouce, 
				filter, 
				zoomLevelRange);
		
		NonEmptyTileIterator<ValuePixel, SumAndSize> tileIterator =
				new NonEmptyTileIterator<ValuePixel, SumAndSize>(SumAndSize.class);

		BufferedImage colorScheme = ImageIO.read(
				new File("src/main/resources/color-schemes/classic_alpha70.png"));
		
		double[] ranges = SumAndSizeAlphaVisualizer.ranges(1, 500, colorScheme.getHeight());
//		System.out.println(Arrays.toString(ranges));
		
//		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeBinaryVisualizer(),
		SumAndSizeAlphaVisualizer visualizer = new SumAndSizeAlphaVisualizer(
				colorScheme, ranges);
		visualizer.setAlphaValue(0.2f);
		visualizer.setForceAlphaValue(true);

		HeatmapFileWriter<ValuePixel, SumAndSize> heatmapFileWriter =
				new HeatmapFileWriter<ValuePixel, SumAndSize>(
						visualizer,
						new DefaultFileStrategy("out/tiles"), 
						"png");
		
		tileIterator.iterate(heatmap, heatmapFileWriter);

		appContext.close();
	}
}
