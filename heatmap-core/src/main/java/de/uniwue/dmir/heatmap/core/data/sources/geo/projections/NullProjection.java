package de.uniwue.dmir.heatmap.core.data.sources.geo.projections;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public class NullProjection 
implements IMapProjection {

	@Override
	public GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates, 
			IFilter<?, ?> filter) {

		return null;
	}

	@Override
	public RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates, 
			TileCoordinates tileCoordinates) {
		
		return null;
	}

	@Override
	public List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates, 
			int zoom, 
			IFilter<?, ?> filter) {
		
		List<TileCoordinates> tileCoordinateList = 
				new ArrayList<TileCoordinates>();

		TileCoordinates tileCoordinates =
				new TileCoordinates(0, 0, zoom);
		
		tileCoordinateList.add(tileCoordinates);
		
		return tileCoordinateList;
	}

}
