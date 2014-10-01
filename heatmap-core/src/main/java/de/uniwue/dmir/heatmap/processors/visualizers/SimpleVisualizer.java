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
package de.uniwue.dmir.heatmap.processors.visualizers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.processors.visualizers.color.CombinedColorPipe;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory.IKeyValueIterator;

public class SimpleVisualizer<TTile, TPixel> 
extends AbstractGenericVisualizer<TTile, TPixel>
implements IVisualizer<TTile> {
	
	public SimpleVisualizer(
			ITileSizeProvider tileSizeProvider,
			IKeyValueIteratorFactory<TTile, RelativeCoordinates, 
			TPixel> pixelIteratorFactory,
			CombinedColorPipe<TPixel> colorPipe) {
		
		super(tileSizeProvider, pixelIteratorFactory);
		this.colorPipe = colorPipe;
	}

	private CombinedColorPipe<TPixel> colorPipe;
	
	@Override
	public BufferedImage visualizeWithDebuggingInformation(
			TTile tile, 
			TileCoordinates coordinates) {

		TileSize tileSize = 
				super.tileSizeProvider.getTileSize(coordinates.getZoom());
		
		BufferedImage bufferedImage = 
				new BufferedImage(
						tileSize.getWidth(), 
						tileSize.getHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		
		IKeyValueIterator<RelativeCoordinates, TPixel> iterator = 
				super.pixelIteratorFactory.instance(tile);

		while (iterator.hasNext()) {
			
			iterator.next();
			
			RelativeCoordinates relativeCoordinates = iterator.getKey();
			TPixel pixel = iterator.getValue();
			
			if (
					relativeCoordinates.getX() < 0 
					|| relativeCoordinates.getX() >= tileSize.getWidth()
					|| relativeCoordinates.getY() < 0 
					|| relativeCoordinates.getY() >= tileSize.getHeight()) {
				
				super.logger.info(
						"Skipping out of bounds coordinates: {}", 
						relativeCoordinates);
				
				continue;
			}
			
			
			Color color = this.colorPipe.getColor(pixel);
			
			bufferedImage.setRGB(
					relativeCoordinates.getX(), 
					relativeCoordinates.getY(), 
					color.getRGB());
		}
		
		return bufferedImage;
	}

}
