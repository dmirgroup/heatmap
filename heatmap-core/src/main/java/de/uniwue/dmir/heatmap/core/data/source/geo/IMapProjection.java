package de.uniwue.dmir.heatmap.core.data.source.geo;

import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public interface IMapProjection {
	
	/**
	 * @param tileCoordinates tile coordinates
	 * @param filter filter to be used for the tile
	 * 
	 * @return geo bounding box corresponding to the tile and the filter
	 */
	GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter);
	
	/**
	 * @param geoCoordinates geo coordinates
	 * @return relative pixel coordinates within a tile
	 */
	RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates,
			TileCoordinates tileCoordinates);
	
	
	List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates,
			int zoom,
			IFilter<?, ?> filter);
	

}
