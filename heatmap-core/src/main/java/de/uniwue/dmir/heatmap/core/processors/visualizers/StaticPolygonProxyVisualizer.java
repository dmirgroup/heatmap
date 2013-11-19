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
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public class StaticPolygonProxyVisualizer<TTile>
implements IVisualizer<TTile> {

	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	private IVisualizer<TTile> visualizer;
	private Polygon polygon;
	
	@Getter
	@Setter
	private Color fillColor;
	
	@Getter
	@Setter
	private Color strokeColor;
	
	public StaticPolygonProxyVisualizer(
			IVisualizer<TTile> visualizer,
			Polygon polygon) {
		
		this.visualizer = visualizer;
		this.polygon = polygon;
		this.strokeColor = Color.WHITE;
	}
	
	@Override
	public BufferedImage visualize(
			TTile tile, 
			TileSize tileSize,
			TileCoordinates coordinates) {
		
		BufferedImage image = this.visualizer.visualize(
				tile, 
				tileSize, 
				coordinates);
		
		Graphics g = image.getGraphics();

		if (this.fillColor != null) {
			
			BufferedImage newImage = new BufferedImage(
					tileSize.getWidth(), 
					tileSize.getHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			
			Graphics newG = newImage.getGraphics();
			
			newG.setColor(this.fillColor);
			newG.fillPolygon(this.polygon);
			
			newG.drawImage(image, 0, 0, null);
			
			image = newImage;
		}
		
		if (this.strokeColor != null) {
			g.setColor(this.strokeColor);
			g.drawPolygon(this.polygon);
		}
		
		return image;
	}
	
}
