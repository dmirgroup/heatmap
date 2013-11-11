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
package de.uniwue.dmir.heatmap.core.filters;

import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.filters.access.IGroupAccess;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

/**
 * Allows to create tiles which contain grouped data.
 * 
 * @author Martin Becker
 *
 * @param <TData>
 * @param <I>
 */
public class ProxyGroupFilter<TData, TGroupTile, TGroupContainerTile> 
extends AbstractProxyFilter<TData, TGroupTile, TGroupContainerTile> {

	private IMapper<TData, List<String>> groupIdMapper;
	private IGroupAccess<TGroupTile, TGroupContainerTile> groupAccess;
	
	public ProxyGroupFilter(
			IMapper<TData, List<String>> groupIdMapper,
			IGroupAccess<TGroupTile, TGroupContainerTile> groupAccess, 
			IFilter<TData, TGroupTile> filter) {
		
		super(filter);
		this.groupIdMapper = groupIdMapper;
		this.groupAccess = groupAccess;
	}
	
	@Override
	public void filter(
			TData dataPoint, 
			TGroupContainerTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		List<String> groupIds = this.groupIdMapper.map(dataPoint);
		for (String groupId : groupIds) {
			TGroupTile groupData = this.groupAccess.get(
					groupId, 
					tile, 
					tileSize, 
					tileCoordinates);
			
			super.filter.filter(
					dataPoint, 
					groupData, 
					tileSize, 
					tileCoordinates);
		}
		
	}

}
