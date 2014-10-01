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
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class StaticPolygonProxyVisualizer<TTile>
implements IVisualizer<TTile> {

	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	@Getter
	private ITileSizeProvider tileSizeProvider;
	
	private IVisualizer<TTile> visualizer;
	private Polygon polygon;
	
	@Getter
	@Setter
	private Color fillColor;

	@Getter
	@Setter
	private boolean fillAbove;
	
	@Getter
	@Setter
	private Color strokeColor;
	
	@Getter
	@Setter
	private boolean strokeAbove;
	
	public StaticPolygonProxyVisualizer(
			ITileSizeProvider tileSizeProvider,
			IVisualizer<TTile> visualizer,
			Polygon polygon) {
		
		this.tileSizeProvider = tileSizeProvider;
		
		this.visualizer = visualizer;
		this.polygon = polygon;
		
		this.fillAbove = false;
		this.fillColor = null;
		
		this.strokeAbove = false;
		this.strokeColor = Color.WHITE;
	}
	
	@Override
	public BufferedImage visualize(
			TTile tile, 
			TileCoordinates coordinates) {
		
		TileSize tileSize = 
				this.tileSizeProvider.getTileSize(coordinates.getZoom());
		
		BufferedImage image = this.visualizer.visualize(
				tile, 
				coordinates);
		
		Graphics g = image.getGraphics();

		if (
				(!this.fillAbove && this.fillColor != null) 
				|| (!this.strokeAbove && this.strokeColor != null)) {
			
			BufferedImage newImage = new BufferedImage(
					tileSize.getWidth(), 
					tileSize.getHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			
			Graphics newG = newImage.getGraphics();
			
			if (!this.fillAbove && this.fillColor != null) {
				newG.setColor(this.fillColor);
				newG.fillPolygon(this.polygon);
			}
			
			if (!this.strokeAbove && this.strokeColor != null) {
				newG.setColor(this.strokeColor);
				newG.drawPolygon(this.polygon);
			}
			
			newG.drawImage(image, 0, 0, null);
			
			image = newImage;
			g = newG;
		}

		if (this.fillAbove && this.fillColor != null) {
			g.setColor(this.fillColor);
			g.fillPolygon(this.polygon);
		}
		
		if (this.strokeAbove && this.strokeColor != null) {
			g.setColor(this.strokeColor);
			g.drawPolygon(this.polygon);
		}
		
		return image;
	}
	
}
