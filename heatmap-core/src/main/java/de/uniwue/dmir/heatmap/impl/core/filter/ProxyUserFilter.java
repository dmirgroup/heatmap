package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalUserData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.type.tile.UserTile;

/**
 * Allows to create tiles which contain data grouped by user.
 * 
 * @author Martin Becker
 *
 * @param <E>
 * @param <I>
 */
@AllArgsConstructor
public class ProxyUserFilter<E extends IExternalUserData, I> 
extends AbstractProxyFilter<E, UserTile<I>>
implements IFilter<E, UserTile<I>> {

	private IFilter<E, I> filter;
	private String defaultUser;

	public ProxyUserFilter(IFilter<E, I> filter) {
		this(filter, null);;
	}
	
	@Override
	public void filter(
			E dataPoint, 
			UserTile<I> tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		String userId = dataPoint.getUserId();
		I userData = tile.getUserData(userId);
		this.filter.filter(dataPoint, userData, tileSize, tileCoordinates);
		
		if (this.defaultUser != null) {
			I defaultData = tile.getUserData(this.defaultUser);
			this.filter.filter(
					dataPoint, 
					defaultData, 
					tileSize, 
					tileCoordinates);
		}
	}

}
