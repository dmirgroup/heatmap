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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class VisualizerFileWriterProcessor<TTile> 
extends AbstractFileWriterProcessor<TTile> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IVisualizer<TTile> visualizer;
	
	public VisualizerFileWriterProcessor(
			String parentFolder,
			IFileStrategy fileStrategy, 
			String fileFormat, 
			IVisualizer<TTile> visualizer) {
		
		super(parentFolder, fileStrategy, fileFormat, false);
		this.visualizer = visualizer;
	}
	
	@Override
	public void process(TTile tile, TileSize tileSize, TileCoordinates coordinates) {
		
		if (tile == null) {
			return;
		}
		
		BufferedImage image = this.visualizer.visualize(tile, tileSize, coordinates);
		try {
			
			OutputStream outputStream = this.getOutputStream(coordinates);
			ImageIO.write(image, super.fileFormat, outputStream);
			outputStream.close();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class Factory<TTile>
	implements IFileWriterProcessorFactory<TTile> {

		protected IFileStrategy fileStrategy;
		protected String fileFormat;
		protected IVisualizer<TTile> visualizer;

		public Factory(
				IFileStrategy fileStrategy,
				String fileFormat,
				IVisualizer<TTile> visualizer) {
			this.fileStrategy = fileStrategy;
			this.fileFormat = fileFormat;
			this.visualizer = visualizer;
		}
		
		@Setter
		@Getter
		private boolean gzip;
		
		@Override
		public ITileProcessor<TTile> getInstance(String parentFolder) {
			return new VisualizerFileWriterProcessor<TTile>(
					parentFolder, 
					this.fileStrategy, 
					this.fileFormat, 
					this.visualizer);
		}
		
	}

}
