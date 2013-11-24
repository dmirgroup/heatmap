package de.uniwue.dmir.heatmap;

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class IdentityTileCoordinatesProjection 
implements ITileCoordinatesProjection {

	@Override
	public TileCoordinates fromCustomToTopLeft(
			TileCoordinates tileCoordinates, 
			IZoomLevelMapper zoomLevelMapper) {
		return tileCoordinates;
	}

	@Override
	public TileCoordinates fromTopLeftToCustom(
			TileCoordinates tileCoordinates, 
			IZoomLevelMapper zoomLevelMapper) {
		return tileCoordinates;
	}

	
}