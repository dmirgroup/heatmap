package de.uniwue.dmir.heatmap.processors.visualizers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * Draws {@link IVisualizer} output over one another. 
 * 
 * @author Martin Becker
 * 
 * @param <TTile> the tile type to process
 */
public class PipeProxyVisualizer<TTile> extends AbstractDebuggingVisualizer<TTile> {

	private List<IVisualizer<TTile>> visualizers;
	
	public PipeProxyVisualizer(
			IVisualizer<TTile> bottom,
			IVisualizer<TTile> top) {
		
		this.visualizers = 
				new ArrayList<IVisualizer<TTile>>();

		visualizers.add(bottom);
		visualizers.add(top);
	}
	
	public PipeProxyVisualizer(List<IVisualizer<TTile>> visualizers) {
		this.visualizers = visualizers;
	}

	@Override
	public BufferedImage visualizeWithDebuggingInformation(
			TTile tile,
			TileSize tileSize,
			TileCoordinates tileCoordinates) {

		if (this.visualizers == null) {
			return null;
		}
		
		BufferedImage stack = null;
		Graphics g = null;
		for (IVisualizer<TTile> v : this.visualizers) {
			BufferedImage layer = v.visualize(tile, tileSize, tileCoordinates);
			if (stack == null && layer != null) {
				stack = layer;
				g = layer.getGraphics();
			} else {
				g.drawImage(layer, 0, 0, 256, 256, null);
			}
		}
		
		return stack;
	}

}
