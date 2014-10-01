package de.uniwue.dmir.heatmap.point.sources.geo;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileRange;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapProjectionUtils {
	
	public static GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			IMapProjection mapProjection,
			int zoom,
			TileRange tileRange,
			IFilter<?, ?> filter) {
		
		GeoBoundingBox bottomLeft = mapProjection.fromTileCoordinatesToGeoBoundingBox(
				new TileCoordinates(tileRange.getMinX(), tileRange.getMaxY(), zoom),
				filter);
		
		GeoBoundingBox topRight = mapProjection.fromTileCoordinatesToGeoBoundingBox(
				new TileCoordinates(tileRange.getMaxX(), tileRange.getMinY(), zoom),
				filter);
		
		return new GeoBoundingBox(bottomLeft.getMin(), topRight.getMax());
		
	}
}
