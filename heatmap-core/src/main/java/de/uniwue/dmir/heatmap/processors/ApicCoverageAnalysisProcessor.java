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
package de.uniwue.dmir.heatmap.processors;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.ApicPointFilter.ApicCityTile;
import de.uniwue.dmir.heatmap.filters.ApicPointFilter.ApicGroupTile;
import de.uniwue.dmir.heatmap.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.processors.visualizers.StaticPolygonProxyVisualizer;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.tiles.pixels.PointSizePixel;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

public class ApicCoverageAnalysisProcessor 
implements ITileProcessor<ApicOverallTile> {

	public static final String IMAGE_TYPE = "png";
	public static final String CITY_PREFIX = "city-";
	public static final String GROUP_PREFIX = "group-";
	public static final String APIC_FILE = "apic-analysis.json";
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ObjectMapper objectMapper = new ObjectMapper();

	private String folder;
	private IVisualizer<Map<RelativeCoordinates, PointSizePixel>> visualizer;
	private IMapper<String, TileSize> cityToTileSizeMapper;

	@Setter
	private IMapper<String, Polygon> cityToGeoBoundingBoxMapper;
	
	@Setter
	private IMapper<String, Polygon> cityToPolygonMapper;
	
	@Setter
	private Color polygonFillColor;
	
	@Setter
	private Color polygonStrokeColor;
	
	
	
	public ApicCoverageAnalysisProcessor(
			String folder,
			IVisualizer<Map<RelativeCoordinates, PointSizePixel>> visualizer,
			IMapper<String, TileSize> cityToTileSizeMapper) {
		
		super();
		this.folder = folder;
		this.visualizer = visualizer;
		this.cityToTileSizeMapper = cityToTileSizeMapper;
	}

	
	
	@Override
	public void process(
			ApicOverallTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		// create folder for output data
		File folder = new File(this.folder);
		folder.mkdirs();
		
		// initialize analysis container
		OverallAnalysis analysis = new OverallAnalysis();
		
		// run through cities to collect statistics
		for (Entry<String, ApicCityTile> cityEntry : tile.getCityTiles().entrySet()) {

			// create city analysis object
			CityAnalysis cityAnalysis = new CityAnalysis();
			analysis.cities.put(cityEntry.getKey(), cityAnalysis);
			
			// get city data
			ApicCityTile cityTile = cityEntry.getValue();

			// run through city pixels
			for (PointSizePixel pixel : cityTile.getPixels().values()) {
				
				// update overall number of data points to number of squares mapping
				
				updateIntegerToSumMap(
						analysis.datapointsToSquaresMap,
						(int) pixel.getSize());
			}
			
			// run through groups
			for (Entry<String, ApicGroupTile> groupEntry : cityTile.getGroupTiles().entrySet()) {
				
				// create group analysis object within city
				GroupAnalysis groupAnalysis = new GroupAnalysis();
				cityAnalysis.groups.put(groupEntry.getKey(), groupAnalysis);
				
				// get group data
				ApicGroupTile groupTile = groupEntry.getValue();
				
				// run through group pixels\
				for (PointSizePixel pixel : groupTile.getPixels().values()) {
					groupAnalysis.sumDatapointsPerSquare += pixel.getSize();
				}
				
			}
		}
		
		
		
		// write analysis data to files
		
		writeDatapointsSquaresToCsvFile(analysis);
		writeGroupAveragesToCsvFile(analysis);
		
		// write complete analysis
		
		File apicFile = new File(folder, APIC_FILE);
		try {
			this.objectMapper.writeValue(apicFile, analysis);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	private void writeGroupAveragesToCsvFile(OverallAnalysis analysis) {
		
		for (Entry<String, CityAnalysis> cityEntry : analysis.getCities().entrySet()) {
			
			String cityName = cityEntry.getKey();
			CityAnalysis cityAnalysis = cityEntry.getValue();
			
			for (Entry<String, GroupAnalysis> groupEntry : cityAnalysis.getGroups().entrySet()) {
				
				String groupName = groupEntry.getKey();
				GroupAnalysis groupAnalysis = groupEntry.getValue();
				
				
				
			}
		}
	}



	private void updateIntegerToSumMap(
			Map<Integer, Integer> datapointsToSquaresMap, 
			int numberOfDatapoints) {
		
		Integer currentCount = datapointsToSquaresMap.get(numberOfDatapoints);
		
		if (currentCount == null) {
			datapointsToSquaresMap.put(numberOfDatapoints, 1);
		} else {
			datapointsToSquaresMap.put(numberOfDatapoints, currentCount + 1);
		}
	}



	private void writeDatapointsSquaresToCsvFile(OverallAnalysis analysis) {

		// write overall statistics
		try {

			// write number of data points to number of squares entries
			
			File apicFile = new File(this.folder, "apic-overall-datapoints-squares-mapping.csv");
			FileWriter writer = new FileWriter(apicFile);
			writer.write("datapoints,squares" + System.getProperty("line.separator"));
			for (Entry<Integer, Integer> e : analysis.datapointsToSquaresMap.entrySet()) {
				writer.write(e.getKey() + "," + e.getValue() + System.getProperty("line.separator"));
			}
			writer.close();

		} catch (IOException e) {

			throw new RuntimeException(e);

		}
	}
	
	@Override
	public void close() {
	}
	
	@Data
	public static class BasicAnalysis {
		private double sumDatapointsPerSquare;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class OverallAnalysis extends BasicAnalysis {
		
		// city analysis data
		
		private Map<String, CityAnalysis> cities = 
				new HashMap<String, CityAnalysis>();
		
		// overall analysis
		
		private Map<Integer, Integer> datapointsToSquaresMap = 
				new HashMap<Integer, Integer>();
		

	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class CityAnalysis extends BasicAnalysis {

		// group analysis data
		
		private Map<String, GroupAnalysis> groups = 
				new HashMap<String, GroupAnalysis>();
		
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class GroupAnalysis extends BasicAnalysis {
		
		private double sumDatapointsPerSquare;
		
	}

}
