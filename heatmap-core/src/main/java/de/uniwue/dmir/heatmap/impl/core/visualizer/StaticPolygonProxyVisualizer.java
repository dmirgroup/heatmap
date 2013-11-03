package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class StaticPolygonProxyVisualizer<I>
implements IVisualizer<I> {

	private IVisualizer<I> visualizer;
	private Polygon polygon;
	
	@Getter
	@Setter
	private Color color;
	
	public StaticPolygonProxyVisualizer(
			IVisualizer<I> visualizer,
			Polygon polygon) {
		
		this.visualizer = visualizer;
		this.polygon = polygon;
		this.color = Color.WHITE;
	}
	
	@Override
	public BufferedImage visualize(
			I tile, 
			TileSize tileSize,
			TileCoordinates coordinates) {
		
		BufferedImage image = this.visualizer.visualize(
				tile, 
				tileSize, 
				coordinates);
		
		Graphics g = image.getGraphics();
		g.setColor(this.color);
		
		image.getGraphics().drawPolygon(this.polygon);
		
		return image;
	}

}
