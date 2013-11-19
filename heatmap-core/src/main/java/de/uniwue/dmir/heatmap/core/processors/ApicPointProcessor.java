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
package de.uniwue.dmir.heatmap.core.processors;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import lombok.Setter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.core.ITileProcessor;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicCityTile;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicGroupTile;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.StaticPolygonProxyVisualizer;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;

public class ApicPointProcessor 
implements ITileProcessor<ApicOverallTile> {

	public static final String IMAGE_TYPE = "png";
	public static final String CITY_PREFIX = "city-";
	public static final String GROUP_PREFIX = "group-";
	public static final String APIC_FILE = "apic.json";

	private final ObjectMapper objectMapper = new ObjectMapper();

	private String folder;
	private IVisualizer<Map<RelativeCoordinates, PointSize>> visualizer;
	private IMapper<String, TileSize> cityToTileSizeMapper;

	@Setter
	private IMapper<String, Polygon> cityToGeoBoundingBoxMapper;
	
	@Setter
	private IMapper<String, Polygon> cityToPolygonMapper;
	
	@Setter
	private Color polygonFillColor;
	
	@Setter
	private Color polygonStrokeColor;
	
	
	
	public ApicPointProcessor(
			String folder,
			IVisualizer<Map<RelativeCoordinates, PointSize>> visualizer,
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
		
		File folder = new File(this.folder);
		folder.mkdirs();
		
		File apicFile = new File(folder, APIC_FILE);
		try {
			this.objectMapper.writeValue(apicFile, tile);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		for (Entry<String, ApicCityTile> cityEntry : tile.getCityTiles().entrySet()) {

			TileSize cityTileSize = this.cityToTileSizeMapper.map(cityEntry.getKey()); 
			
//			// calculate relative coordinate bounds
//			
//			RelativeCoordinates min = new RelativeCoordinates(0, 0);
//			RelativeCoordinates max = new RelativeCoordinates(
//					cityTileSize.getWidth() - 1, 
//					cityTileSize.getHeight() - 1);
//			
//			for (RelativeCoordinates r : cityEntry.getValue().getPixels().keySet()) {
//				
//				min.setX(Math.min(min.getX(), r.getX()));
//				min.setY(Math.min(min.getY(), r.getY()));
//				
//				max.setX(Math.max(max.getX(), r.getX()));
//				max.setY(Math.max(max.getY(), r.getY()));
//			}
//			
//			// calculate actual offset
//			int offsetX = - min.getX();
//			int offsetY = - min.getY();
//			
//			// translate relative coordinates
//			for (RelativeCoordinates r : cityEntry.getValue().getPixels().keySet()) {
//				r.setX(r.getX() + offsetX);
//				r.setY(r.getY() + offsetY);
//			}
//			
//			// calculate new width and height
//			
//			int width = max.getX() - min.getX() + 1;
//			int height = max.getY() - min.getY() + 1;
//			
//			// calculate new tile size
//			TileSize newCityTileSize = new TileSize(width, height);
					
			// initialize visualizer

			IVisualizer<Map<RelativeCoordinates, PointSize>> visualizer;
			if (this.cityToPolygonMapper != null) {
				
				Polygon cityPolygon = this.cityToPolygonMapper.map(cityEntry.getKey());
				
//				// translate city polygon
//				cityPolygon.translate(offsetX, offsetY);
				
				StaticPolygonProxyVisualizer<Map<RelativeCoordinates, PointSize>> polygonVisualizer = 
						new StaticPolygonProxyVisualizer<Map<RelativeCoordinates,PointSize>>(
								this.visualizer, 
								cityPolygon);
				polygonVisualizer.setFillColor(this.polygonFillColor);
				polygonVisualizer.setStrokeColor(this.polygonStrokeColor);
				
				visualizer = polygonVisualizer;
				
			} else {
				visualizer = this.visualizer;
			}
			
			ApicCityTile cityTile = cityEntry.getValue();
			BufferedImage cityImage = visualizer.visualize(
					cityTile.getPixels(), 
					cityTileSize, 
					tileCoordinates);
			
			String cityName = cityEntry.getKey().replaceAll("[^\\w]*", "");
			File cityFile = new File(folder, CITY_PREFIX + cityName + "." + IMAGE_TYPE);
			
			try {
				ImageIO.write(cityImage, IMAGE_TYPE, cityFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			for (Entry<String, ApicGroupTile> groupEntry : cityTile.getGroupTiles().entrySet()) {

				ApicGroupTile groupTile = groupEntry.getValue();
				BufferedImage groupImage = visualizer.visualize(
						groupTile.getPixels(), 
						cityTileSize, 
						tileCoordinates);
				
				String groupName = groupEntry.getKey().replaceAll("[^\\w]*", "");
				File groupFile = new File(folder, GROUP_PREFIX + cityName + "-" + groupName + "." + IMAGE_TYPE);
				
				try {
					ImageIO.write(groupImage, IMAGE_TYPE, groupFile);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


}
