/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalUserData;
import de.uniwue.dmir.heatmap.core.filter.IGroupAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * Allows to create tiles which contain grouped data.
 * 
 * @author Martin Becker
 *
 * @param <E>
 * @param <I>
 */
@AllArgsConstructor
public class ProxyGroupFilter<E extends IExternalUserData, I, T> 
extends AbstractProxyFilter<E, T>
implements IFilter<E, T> {

	private IGroupAccess<I, T> groupAccess;
	private IFilter<E, I> filter;
	private String defaultUser;

	public ProxyGroupFilter(
			IGroupAccess<I, T> groupAccess, 
			IFilter<E, I> filter) {
		this(groupAccess, filter, null);
	}
	
	@Override
	public void filter(
			E dataPoint, 
			T tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		String userId = dataPoint.getUserId();
		I userData = this.groupAccess.get(
				userId, 
				tile, 
				tileSize, 
				tileCoordinates);
		this.filter.filter(
				dataPoint, 
				userData, 
				tileSize, 
				tileCoordinates);
		
		if (this.defaultUser != null) {
			I defaultData = this.groupAccess.get(
					this.defaultUser, 
					tile, 
					tileSize, 
					tileCoordinates);
			this.filter.filter(
					dataPoint, 
					defaultData, 
					tileSize, 
					tileCoordinates);
		}
	}

}
