package de.uniwue.dmir.heatmap.core.tiles.factories;

import de.uniwue.dmir.heatmap.core.ITileFactory;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public class ApicTileFactory 
implements ITileFactory<ApicOverallTile>{

	@Override
	public ApicOverallTile newInstance(
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		return new ApicOverallTile();
	}

}
