package de.uniwue.dmir.heatmap.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.DefaultHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoTileDataSource;
import de.uniwue.dmir.heatmap.core.tile.Tile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.CsvGeoDataSource;
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
import de.uniwue.dmir.heatmap.impl.core.visualizer.SumAndSizeGrayScaleVisualizer;

public class TilesTest {

	@Test
	public void testTile() throws IOException {
		
		IHeatmapDimensions dimensions = new DefaultHeatmapDimensions();
		
		GeoTileDataSource<GeoPoint, ValuePixel> dataSouce =
				new GeoTileDataSource<GeoPoint, ValuePixel>(
						new CsvGeoDataSource(
								new File("src/test/resources/lonlat.txt"),
								",",
								false), 
						new MercatorMapProjection(dimensions), 
						new GeoPointCoordinateMapper(), 
						new GeoPointToValuePixelMapper());
		
		IFilter<ValuePixel, SumAndSize> filter = 
				new ImageFilter<ValuePixel, SumAndSize>(
						new ValuePixelToSumAndSizeMapper(),
						new SumAndSizeAdder(),
						new SumAndSizeScalarMultiplier(),
						ImageIO.read(new File("src/main/resources/filter/dot13.png")));
		
		TileCoordinates tileCoordinates = new TileCoordinates(1, 0, 1);
		Tile<ValuePixel, SumAndSize> tile = new Tile<ValuePixel, SumAndSize>(
				dimensions.getTileDimensions(),
				tileCoordinates,
				filter,
				new SumAndSize[] {});
		
		List<ValuePixel> pixels = dataSouce.getData(tileCoordinates, filter);
		for (ValuePixel v : pixels) {
			tile.add(v);
		}
		
		SumAndSizeGrayScaleVisualizer visualizer =
			new SumAndSizeGrayScaleVisualizer();
		
		BufferedImage image = visualizer.visualize(tile);
		
		ImageIO.write(image, "png", new File("out/test.png"));
	}
	
}
