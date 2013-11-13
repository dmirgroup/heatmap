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
	
	
	
	public ApicPointProcessor(
			String folder,
			IVisualizer<Map<RelativeCoordinates, PointSize>> visualizer,
			IMapper<String, TileSize> cityToTileSizeMapper) {
		
		super();
		this.folder = folder;
		this.visualizer = visualizer;
		this.cityToTileSizeMapper = cityToTileSizeMapper;
	}

	@Setter
	private IMapper<String, Polygon> cityToPolygonMapper;
	
	@Setter
	private Color polygonFillColor;
	
	@Setter
	private Color polygonStrokeColor;
	
	
	
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

			Polygon cityPolygon = this.cityToPolygonMapper.map(cityEntry.getKey());
			
			IVisualizer<Map<RelativeCoordinates, PointSize>> visualizer;
			if (cityToPolygonMapper != null) {
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
			
			TileSize cityTileSize = this.cityToTileSizeMapper.map(cityEntry.getKey()); 
			
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
				File groupFile = new File(folder, GROUP_PREFIX + groupName + "." + IMAGE_TYPE);
				
				try {
					ImageIO.write(groupImage, IMAGE_TYPE, groupFile);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


}
