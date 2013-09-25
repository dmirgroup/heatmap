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
package de.uniwue.dmir.heatmap.core.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class HeatmapFileWriter<E extends IExternalData, I> 
implements ITileProcessor<E, I> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IVisualizer<I> visualizer;
	private IFileStrategy fileStrategy;
	private String imageFormat;
	
	@Override
	public void process(ITile<E, I> tile) {
		if (tile == null) {
			return;
		}
		
		BufferedImage image = this.visualizer.visualize(tile);
		
		File file = this.fileStrategy.getFile(tile.getCoordinates(), this.imageFormat);
		file.getParentFile().mkdirs();
		
		try {
			ImageIO.write(image, this.imageFormat, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static interface IFileStrategy {
		File getFile(TileCoordinates coordinates, String extension);
	}
	
	@AllArgsConstructor
	public static class DefaultFileStrategy implements IFileStrategy {

		private String folder;
		
		public DefaultFileStrategy() {
			this(null);
		}
		
		@Override
		public File getFile(TileCoordinates coordinates, String extension) {
			String file = String.format("%d/%d/%d.%s",
					coordinates.getZoom(),
					coordinates.getX(),
					coordinates.getY(),
					extension);
			
			if (this.folder == null) {
				return new File(file);
			} else {
				return new File(this.folder, file);
			}
		}
		
	}

}
