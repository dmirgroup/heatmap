package de.uniwue.dmir.heatmap.impl.core.filter;

import java.util.Map;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class ProxyMapFilter<E extends IExternalData, I> 
extends AbstractProxyFilter<E, Map<RelativeCoordinates, I>>
implements IFilter<E, Map<RelativeCoordinates, I>> {

	@Override
	public void filter(
			E dataPoint, 
			Map<RelativeCoordinates, I> tile,
			TileSize tileSize, 
			TileCoordinates tileCoordinates) {
		
		
	}

}
