package de.uniwue.dmir.heatmap.processors.visualizers;

import java.awt.image.BufferedImage;

import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;


/**
 * Marker interface for visualizers which are independent of the tile they are
 * getting. 
 * 
 * @author Martin Becker
 */
public interface IBackgroundVisualizer<TTile> {

	BufferedImage visualize(
			TTile tile, 
			TileSize tileSize, 
			TileCoordinates tileCoordinates);
}
