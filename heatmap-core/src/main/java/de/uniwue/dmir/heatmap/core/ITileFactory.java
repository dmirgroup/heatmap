package de.uniwue.dmir.heatmap.core;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public interface ITileFactory<I> {
	
	I newInstance(
			TileSize size, 
			TileCoordinates coordinates);
	
}
