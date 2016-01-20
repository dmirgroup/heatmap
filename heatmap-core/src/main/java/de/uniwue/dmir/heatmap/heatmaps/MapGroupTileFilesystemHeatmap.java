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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.processors.AbstractFileWriterProcessor;
import de.uniwue.dmir.heatmap.processors.filestrategies.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapGroupTileFilesystemHeatmap<I> 
implements IHeatmap<Map<String, I>> {

	public static final String FILE_EXTENSION = "json";
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private HeatmapSettings settings;
	private Class<I> clazz;
	
	private String parentFolder;
	private IFileStrategy fileStrategy;
	private boolean gzip;

	private ObjectMapper mapper;

	public MapGroupTileFilesystemHeatmap(
			HeatmapSettings settings,
			Class<I> clazz,
			String parentFolder,
			boolean gzip) {

		this.settings = settings;
		this.clazz = clazz;
		this.parentFolder = parentFolder;
		this.fileStrategy = new DefaultFileStrategy();
		this.gzip = gzip;
		
		this.mapper = new ObjectMapper();
	}

	@Override
	public HeatmapSettings getSettings() {
		return this.settings;
	}

	@Override
	public Map<String, I> getTile(TileCoordinates coordinates) {
		
		String extension = 
				FILE_EXTENSION 
				+ (this.gzip ? AbstractFileWriterProcessor.GZIP_EXTENSION : "");
		
		File parentFolder = new File(this.parentFolder);
		
		this.logger.debug("Reading groups from: {}", parentFolder);
		String[] groups = parentFolder.list(DirectoryFileFilter.DIRECTORY);
		if (groups == null) {
			this.logger.debug("No groups found");
			return null;
		} else {
			this.logger.debug("Found groups: {}", groups.length);
		}
		
		Map<String, I> groupTile = new HashMap<String, I>();
		String tileName = this.fileStrategy.getFileName(coordinates, extension);
		for (String group: groups){
			File tileFile = new File(new File(this.parentFolder, group), tileName);
			try {
				if (tileFile.exists()) {
					InputStream inputStream = new FileInputStream(tileFile);
					
					if (this.gzip) {
						inputStream = new GZIPInputStream(inputStream);
					}
					
					I tile = this.mapper.readValue(inputStream, this.clazz);
					groupTile.put(group, tile);
				}
			} catch (JsonParseException e) {
				throw new IllegalArgumentException(e);
			} catch (JsonMappingException e) {
				throw new IllegalArgumentException(e);
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		if (groupTile.isEmpty()) {
			return null;
		} else {
			return groupTile;
		}
	}

	@Override
	public void processTiles(ITileProcessor<Map<String, I>> processor) {
		
		TileCoordinates coordinates = new TileCoordinates(0, 0, 0);
		File parentFolder = new File(this.parentFolder);
		
		// get groups
		String[] groups = parentFolder.list(DirectoryFileFilter.DIRECTORY);
		if (groups == null) return;
		
		// collect zoom folders
		Set<String> zoomFolders = new HashSet<String>();
		for (String group : groups) {
			String[] localZoomFolders = new File(parentFolder, group)
					.list(DirectoryFileFilter.DIRECTORY);
			zoomFolders.addAll(Arrays.asList(localZoomFolders));
		}
		
		for (String zoomFolder : zoomFolders) {
				
			int zoom = Integer.parseInt(zoomFolder);
			coordinates.setZoom(zoom);
			
			// collect local x folders
			Set<String> xFolders = new HashSet<String>();
			for (String group : groups) {
				String[] localXFolders = new File(new File(parentFolder, group), zoomFolder)
						.list(DirectoryFileFilter.DIRECTORY);
				xFolders.addAll(Arrays.asList(localXFolders));
			}
			
			for (String xFolder: xFolders) {
				
				long x = Long.parseLong(xFolder);
				coordinates.setX(x);
				
				// get local y folders
				Set<String> yFiles = new HashSet<String>();
				for (String group : groups) {
					String[] localYFiles = new File(new File(new File(parentFolder, group), zoomFolder), xFolder)
							.list(DirectoryFileFilter.DIRECTORY);
					yFiles.addAll(Arrays.asList(localYFiles));
				}
				
				for (String yFile: yFiles) {
						
						String yString = yFile.split("\\.")[0];
						long y = Long.parseLong(yString);
						coordinates.setY(y);
						
						Map<String, I> tile = this.getTile(coordinates);
						
						processor.process(
								tile, 
								this.settings.getTileSize(), 
								coordinates);
				}
			}
		}
		
	}
	
}