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
package de.uniwue.dmir.heatmap.core.processors.visualizers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.processors.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.processors.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.processors.visualizers.colors.CombinedColorPipe;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

@AllArgsConstructor
public class SimpleVisualizer<TTile, TPixel> 
extends AbstractDebuggingVisualizer<TTile>
implements IVisualizer<TTile> {

	private IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory;
	
	private CombinedColorPipe<TPixel> colorPipe;

	@Override
	public BufferedImage visualize(
			TTile tile, 
			TileSize tileSize,
			TileCoordinates coordinates) {

		BufferedImage bufferedImage = 
				new BufferedImage(
						tileSize.getWidth(), 
						tileSize.getHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		
		IKeyValueIterator<RelativeCoordinates, TPixel> iterator = 
				this.pixelIteratorFactory.iterator(tile);

		while (iterator.hasNext()) {
			
			iterator.next();
			
			RelativeCoordinates relativeCoordinates = iterator.getKey();
			TPixel pixel = iterator.getValue();
			
			Color color = this.colorPipe.getColor(pixel);
			
			bufferedImage.setRGB(
					relativeCoordinates.getX(), 
					relativeCoordinates.getY(), 
					color.getRGB());
		}
		
		// add debugging information
		super.addDebugInformation(tileSize, coordinates, bufferedImage);
		
		return bufferedImage;
	}

}
