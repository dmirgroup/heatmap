package de.uniwue.dmir.heatmap.core;

import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * For tile indexing, we adopted a top-left centered indexing scheme with
 * increasing coordinate values from top to bottom and from left to right.
 * In order to allow the heat map to serve tiles with other indexing schemes,
 * this projection is used (e.g. for TMS layers) to convert tile coordinates.
 * 
 * @author Martin Becker
 */
public interface ITileCoordinatesProjection {
	
	TileCoordinates fromCustomToTopLeft(
			TileCoordinates tileCoordinates,
			IHeatmapDimensions dimensions);
	
	TileCoordinates fromTopLeftToCustom(
			TileCoordinates tileCoordinates,
			IHeatmapDimensions dimensions);
	
	public static class DefaultTileCoordinatesProjection 
	implements ITileCoordinatesProjection {

		@Override
		public TileCoordinates fromCustomToTopLeft(
				TileCoordinates tileCoordinates, 
				IHeatmapDimensions dimensions) {
			return tileCoordinates;
		}

		@Override
		public TileCoordinates fromTopLeftToCustom(
				TileCoordinates tileCoordinates, 
				IHeatmapDimensions dimensions) {
			return tileCoordinates;
		}

		
	}
}
