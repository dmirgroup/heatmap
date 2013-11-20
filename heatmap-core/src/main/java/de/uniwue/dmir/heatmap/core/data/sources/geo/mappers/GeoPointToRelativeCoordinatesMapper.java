package de.uniwue.dmir.heatmap.core.data.sources.geo.mappers;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.IGeoPoint;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

@AllArgsConstructor
public class GeoPointToRelativeCoordinatesMapper<TData extends IGeoPoint>
implements IToRelativeCoordinatesMapper<TData>{

	private IMapProjection mapProjection;
	
	@Override
	public RelativeCoordinates map(
			TData dataPoint,
			TileCoordinates tileCoordinates) {

		return this.mapProjection.fromGeoToRelativeCoordinates(
				dataPoint.getGeoCoordinates(), 
				tileCoordinates);
	}

}
