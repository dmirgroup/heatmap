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
package de.uniwue.dmir.heatmap.heatmaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import lombok.Getter;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.ITileRangeProvider;
import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.TileRange;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.ZoomLevelRange;
import de.uniwue.dmir.heatmap.processors.AbstractFileWriterProcessor;
import de.uniwue.dmir.heatmap.processors.filestrategies.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class GroupFilesystemHeatmap<TTile, TParameters> 
implements IHeatmap<TTile, TParameters> {

	public static final String FILE_EXTENSION = "json";
	
	@Getter
	private ITileSizeProvider tileSizeProvider;
	
	private Class<TTile> clazz;
	
	private String parentFolder;
	private IFileStrategy fileStrategy;
	private boolean gzip;

	private ObjectMapper mapper;

	public GroupFilesystemHeatmap(
			ITileSizeProvider tileSizeProvider,
			Class<TTile> clazz,
			String parentFolder,
			boolean gzip) {

		this.tileSizeProvider = tileSizeProvider;
		
		this.clazz = clazz;
		this.parentFolder = parentFolder;
		this.fileStrategy = new DefaultFileStrategy();
		this.gzip = gzip;
		
		this.mapper = new ObjectMapper();
	}

	@Override
	public TTile getTile(TileCoordinates coordinates, TParameters parameters) {
		
		String extension = 
				FILE_EXTENSION 
				+ (this.gzip ? AbstractFileWriterProcessor.GZIP_EXTENSION : "");
		
		String fileName = this.fileStrategy.getFileName(coordinates, extension);
		File file = new File(this.parentFolder, fileName);
		
		try {
			
			if (!file.exists()) {
				return null;
			} else {
				InputStream inputStream = new FileInputStream(file);
				
				if (this.gzip) {
					inputStream = new GZIPInputStream(inputStream);
				}
				
				return this.mapper.readValue(inputStream, this.clazz);
			}
		} catch (JsonParseException e) {
			throw new IllegalArgumentException(e);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void processTiles(
			ITileProcessor<TTile> processor,
			ZoomLevelRange zoomLevelRange,
			ITileRangeProvider tileRangeProvider,
			TParameters parameters) {
		
		TileCoordinates coordinates = new TileCoordinates(0, 0, 0);
		File folder = new File(this.parentFolder);
		
		for (File zoomFolder : folder.listFiles()) {
			
			if (zoomFolder.isDirectory()) {
				
				int zoom = Integer.parseInt(zoomFolder.getName());
				if (!zoomLevelRange.isInRange(zoom)) {
					continue;
				}
				coordinates.setZoom(zoom);
				
				TileRange tileRange = null;
				if (tileRangeProvider != null) {
					tileRange = tileRangeProvider.getTileRange(zoom);
				}
				
				TileSize tileSize = this.tileSizeProvider.getTileSize(zoom);
				
				for (File xFolder : zoomFolder.listFiles()) {
					
					if (xFolder.isDirectory()) {
						
						long x = Long.parseLong(xFolder.getName());
						coordinates.setX(x);
						
						for (File data : xFolder.listFiles()) {
								
								String yString = data.getName().split("\\.")[0];
								long y = Long.parseLong(yString);
								coordinates.setY(y);
								
								if (tileRange != null && !tileRange.isInRange(coordinates)) {
									continue;
								}
								
								TTile tile = this.getTile(coordinates, parameters);
								
								processor.process(
										tile,
										tileSize,
										coordinates);
						}
					}
				}
			}
		}
		
	}
	
//	public static void main(String[] args) throws IOException {
//		FilesystemHeatmap<SumAndSize[]> heatmap = 
//				new FilesystemHeatmap<SumAndSize[]>(
//						new HeatmapSettings(), 
//						SumAndSize[].class, 
//						"out/data");
//				
//				BufferedImage colorScheme = ImageIO.read(
//						new File("src/main/resources/color-schemes/classic_70.png"));
//				double[] ranges = SumAndSizeAlphaVisualizer.ranges(1, 500, colorScheme.getHeight());
//				SumAndSizeAlphaVisualizer visualizer = new SumAndSizeAlphaVisualizer(
//						colorScheme, ranges);
//				visualizer.setAlphaValue(0.5f);
//				visualizer.setBackgroundColor(
//						new Color(colorScheme.getRGB(0, colorScheme.getHeight() - 1), true));
//				visualizer.setForceAlphaValue(true);
//
//				VisualizationFileWriter<SumAndSize[]> heatmapFileWriter =
//						new VisualizationFileWriter<SumAndSize[]>(
//								new DefaultFileStrategy("out/tiles"), 
//								"png",
//								visualizer);
//				
//		heatmap.processTiles(heatmapFileWriter);
//	}
	
}
