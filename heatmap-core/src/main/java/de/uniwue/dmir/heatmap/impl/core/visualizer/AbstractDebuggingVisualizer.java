package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public abstract class AbstractDebuggingVisualizer<I> implements IVisualizer<I> {
	
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
