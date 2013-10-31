package de.uniwue.dmir.heatmap.impl.core.filter;

import java.util.Map;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.ITileFactory;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class MapGroupAccess<I> 
extends AbstractGroupAccess<I, Map<String, I>> {

	public MapGroupAccess(ITileFactory<I> tileFactory) {
		super(tileFactory);
	}

	@Override
	public I get(
			String groupId, 
			Map<String, I> tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		I groupData = tile.get(groupId);
		if (groupData == null) {
			groupData = super.tileFactory.newInstance(tileSize, tileCoordinates);
		}
		return groupData;
	}

}
