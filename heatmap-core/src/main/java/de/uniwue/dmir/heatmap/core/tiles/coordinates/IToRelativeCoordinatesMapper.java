package de.uniwue.dmir.heatmap.core.tiles.coordinates;


public interface IToRelativeCoordinatesMapper<TData> {
	RelativeCoordinates map(
			TData dataPoint, 
			TileCoordinates tileCoordinates);
}
