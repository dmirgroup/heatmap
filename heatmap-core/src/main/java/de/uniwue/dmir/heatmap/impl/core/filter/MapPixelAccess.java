package de.uniwue.dmir.heatmap.impl.core.filter;

import java.util.Map;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.filter.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;

public class MapPixelAccess<I> 
implements IPixelAccess<I, Map<RelativeCoordinates, I>>{

	@Override
	public I get(
			RelativeCoordinates relativeCoordinates,
			Map<RelativeCoordinates, I> tile, 
			TileSize tileSize) {

		return tile.get(relativeCoordinates);
	}

	@Override
	public void set(
			I pixelValue, 
			RelativeCoordinates relativeCoordinates,
			Map<RelativeCoordinates, I> tile, 
			TileSize tileSize) {
		
		tile.put(relativeCoordinates, pixelValue);
	}

}
