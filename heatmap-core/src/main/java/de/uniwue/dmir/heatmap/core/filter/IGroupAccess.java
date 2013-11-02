package de.uniwue.dmir.heatmap.core.filter;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;


public interface IGroupAccess<IInner, IOuter> {
	
	/**
	 * This method never returns <code>null</code>.
	 * 
	 * @param groupId the id of the group
	 * @param tile the tile data to get group data from
	 * @param tileSize
	 * @param tileCoordinates
	 * 
	 * @return the tile data related to the group (never <code>null</code>)
	 */
	public IInner get(
			String groupId, 
			IOuter tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates);
}
