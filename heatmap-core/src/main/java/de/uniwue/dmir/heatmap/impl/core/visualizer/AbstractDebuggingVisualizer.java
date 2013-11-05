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
package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public abstract class AbstractDebuggingVisualizer<I> implements IVisualizer<I> {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Getter
	@Setter
	private boolean debug;
	
	@Getter
	@Setter
	private Color debugColor = Color.BLACK;

	public AbstractDebuggingVisualizer() {
		this.debug = false;
	}
	
	public void addDebugInformation(
			TileSize tileSize,
			TileCoordinates coordinates,
			BufferedImage image) {
		
		if (this.debug) {

			Graphics graphics = image.createGraphics();
			
			graphics.setColor(this.debugColor);

			graphics.drawRect(
					0, 0, 
					tileSize.getWidth() - 1, tileSize.getHeight() - 1);

			graphics.drawString(String.format(
					"x:%d, y:%d, z:%d", 
					coordinates.getX(),
					coordinates.getY(),
					coordinates.getZoom()),
					15, 15);

		}
	}
	
}
