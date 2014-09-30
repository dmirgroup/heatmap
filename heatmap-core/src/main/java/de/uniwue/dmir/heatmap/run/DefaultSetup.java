package de.uniwue.dmir.heatmap.run;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import lombok.Data;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import de.uniwue.dmir.heatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.IPointsource;
import de.uniwue.dmir.heatmap.ITileFactory;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.filters.AddingFilter;
import de.uniwue.dmir.heatmap.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.filters.operators.SumAdder;
import de.uniwue.dmir.heatmap.filters.pixelaccess.IPixelAccess;
import de.uniwue.dmir.heatmap.filters.pixelaccess.MapPixelAccess;
import de.uniwue.dmir.heatmap.filters.pointmappers.geo.SimpleGeoPointToSumPixelMapper;
import de.uniwue.dmir.heatmap.heatmaps.DefaultHeatmap;
import de.uniwue.dmir.heatmap.point.sources.GeoPointsource;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.point.sources.geo.datasources.CsvGeoDatasource;
import de.uniwue.dmir.heatmap.point.sources.geo.projections.MercatorMapProjection;
import de.uniwue.dmir.heatmap.point.types.geo.GeoPointToGeoCoordinateMapper;
import de.uniwue.dmir.heatmap.point.types.geo.SimpleGeoPoint;
import de.uniwue.dmir.heatmap.processors.VisualizerFileWriterProcessor;
import de.uniwue.dmir.heatmap.processors.filestrategies.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.processors.visualizers.SimpleVisualizer;
import de.uniwue.dmir.heatmap.processors.visualizers.color.CombinedColorPipe;
import de.uniwue.dmir.heatmap.processors.visualizers.color.CutpointColorScheme;
import de.uniwue.dmir.heatmap.processors.visualizers.color.SimpleColorPipe;
import de.uniwue.dmir.heatmap.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.geo.GeoPointToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.tiles.factories.MapTileFactory;
import de.uniwue.dmir.heatmap.tiles.pixels.SumPixel;
import de.uniwue.dmir.heatmap.util.iterator.MapKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

public class DefaultSetup {
	
	@Data
	public static class Settings {
		
		@Option(name = "-csv", usage = "CSV file to read (format: lon,lat,value)")
		private File csvFile;
		
		@Option(
				name = "-csvsep", 
				aliases = {"-csvSeparator"}, 
				usage = "Cseparator to split lines in CSV file")
		private String csvSeparator;
		
		@Option(
				name = "-csvskip", 
				aliases = {"-csvSkipFirstLine"}, 
				usage = "whether to skip the first line of the CSV file")
		private boolean csvSkipFirstLine;
		
		@Option(name = "-out", usage = "the output folder")
		private File outputFolder;
		
		
	}
	
	public static void main(String[] args) throws IOException {
		
		Settings settings = new Settings();
		CmdLineParser parser = new CmdLineParser(settings);
		
		try {
			parser.parseArgument(args);
		} catch(CmdLineException e ) {
			System.err.println(e.getMessage());
			System.err.println("java -jar myprogram.jar [options...] arguments...");
			parser.printUsage(System.err);
			return;
		}
		
		test(
				settings.getCsvFile(),
				settings.getCsvSeparator(),
				settings.isCsvSkipFirstLine(),
				settings.getOutputFolder().getAbsolutePath());
	}
	

	public static void test(File file, String separator, boolean skipFirstLine, String outputParentFolder) throws IOException {
		
		// heatmap settings
		HeatmapSettings heatmapSettings = new HeatmapSettings();
		
		// settings up point source
		
		// TODO: more flexible geo data source
		IGeoDatasource<SimpleGeoPoint<String>> datasource = 
				new CsvGeoDatasource(file, separator, skipFirstLine);
		
		IMapProjection mapProjection = new MercatorMapProjection(
				heatmapSettings.getTileSize(), 
				heatmapSettings.getZoomLevelMapper());
		
		IMapper<SimpleGeoPoint<String>, GeoCoordinates> pointToGeoCoordinatesMapper =
				new GeoPointToGeoCoordinateMapper<SimpleGeoPoint<String>>();
		
		IPointsource<SimpleGeoPoint<String>> pointsource = new GeoPointsource<SimpleGeoPoint<String>>(
				datasource, mapProjection, pointToGeoCoordinatesMapper);
		
		// the tile factory
		
		ITileFactory<Map<RelativeCoordinates, SumPixel>> tileFactory = 
				new MapTileFactory<RelativeCoordinates, SumPixel>();
		
		// the filter incorporating points into tiles
		
		IToRelativeCoordinatesMapper<SimpleGeoPoint<String>> dataToRelativeCoordinatesMapper =
				new GeoPointToRelativeCoordinatesMapper<SimpleGeoPoint<String>>(mapProjection);
		IMapper<SimpleGeoPoint<String>, SumPixel> dataToPixelMapper = 
				new SimpleGeoPointToSumPixelMapper<String>();
		IPixelAccess<SumPixel, Map<RelativeCoordinates, SumPixel>> pixelAccess = 
				new MapPixelAccess<SumPixel>();
		IAdder<SumPixel> pixelAdder = new SumAdder();
				
		IFilter<SimpleGeoPoint<String>, Map<RelativeCoordinates, SumPixel>> filter = 
				new AddingFilter<SimpleGeoPoint<String>, SumPixel, Map<RelativeCoordinates, SumPixel>>(
						dataToRelativeCoordinatesMapper, 
						dataToPixelMapper, 
						pixelAccess, 
						pixelAdder);
		
		// the heatmap putting things together
		
		IHeatmap<Map<RelativeCoordinates, SumPixel>> heatmap = new DefaultHeatmap<SimpleGeoPoint<String>, Map<RelativeCoordinates, SumPixel>>(
				tileFactory,
				pointsource,
				filter,
				heatmapSettings);
		
		// the processor processing each tile
		
		IVisualizer<Map<RelativeCoordinates, SumPixel>> visualizer = 
				new SimpleVisualizer<Map<RelativeCoordinates, SumPixel>, SumPixel>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, SumPixel>(),
						new CombinedColorPipe<SumPixel>(
								new SimpleColorPipe<SumPixel>(
										new IMapper<SumPixel, Double>() {
											@Override
											public <TDerived extends SumPixel> Double map(
													TDerived object) {
												return object.getSize();
											}
										},
										new CutpointColorScheme(
												new Double[] {
														1.,
														10.,
														100.,
														1000.
												}, 
												new Color[] {
														Color.BLACK,
														Color.YELLOW,
														Color.ORANGE,
														Color.RED, 
														Color.WHITE
												}, 
												true)), 
						null));
		
		ITileProcessor<Map<RelativeCoordinates, SumPixel>> processor =
				new VisualizerFileWriterProcessor<Map<RelativeCoordinates,SumPixel>>(
						outputParentFolder, 
						new DefaultFileStrategy(), 
						"png", 
						visualizer);
		heatmap.processTiles(processor);
		
		
	}
	
}
