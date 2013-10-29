package de.uniwue.dmir.heatmap.core.filter;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;

public interface IPixelAccess<I, T> {

	public I get(
			RelativeCoordinates relativeCoordinates,
			T tile,
			TileSize tileSize);
	
	public void set(
			I pixelValue,
			RelativeCoordinates relativeCoordinates,
			T tile,
			TileSize tileSize);
}
