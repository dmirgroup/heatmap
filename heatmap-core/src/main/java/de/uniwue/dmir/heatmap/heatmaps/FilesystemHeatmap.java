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
import java.io.IOException;

import de.uniwue.dmir.heatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.processors.filestrategies.DefaultFileStrategy;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class FilesystemHeatmap<I> 
implements IHeatmap<I> {

	public static final String FILE_EXTENSION = "json";
	
	private HeatmapSettings settings;
	
	private String parentFolder;
	private IFileStrategy fileStrategy;
	
	private IFileReader<I> fileReader;

	public FilesystemHeatmap(
			HeatmapSettings settings,
			String parentFolder,
			IFileReader<I> fileReader) {

		this.settings = settings;
		this.parentFolder = parentFolder;
		this.fileStrategy = new DefaultFileStrategy();
		this.fileReader = fileReader;
	}

	@Override
	public HeatmapSettings getSettings() {
		return this.settings;
	}

	@Override
	public I getTile(TileCoordinates coordinates) {
		
		String fileName = this.fileStrategy.getFileName(coordinates, this.fileReader.getExtension());
		File file = new File(this.parentFolder, fileName);
		
		if (!file.exists()) {
			return null;
		} else {
			try {
				return this.fileReader.readFile(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void processTiles(ITileProcessor<I> processor) {
		
		TileCoordinates coordinates = new TileCoordinates(0, 0, 0);
		File folder = new File(this.parentFolder);
		
		for (File zoomFolder : folder.listFiles()) {
			
			if (zoomFolder.isDirectory()) {
				
				int zoom = Integer.parseInt(zoomFolder.getName());
				coordinates.setZoom(zoom);
				
				for (File xFolder : zoomFolder.listFiles()) {
					
					if (xFolder.isDirectory()) {
						
						long x = Long.parseLong(xFolder.getName());
						coordinates.setX(x);
						
						for (File data : xFolder.listFiles()) {
								
								String yString = data.getName().split("\\.")[0];
								long y = Long.parseLong(yString);
								coordinates.setY(y);
								
								I tile = this.getTile(coordinates);
								
								processor.process(
										tile, 
										this.settings.getTileSize(), 
										coordinates);
						}
					}
				}
			}
		}
		
	}
	
	public static interface IFileParser {
		
	}
	
}
