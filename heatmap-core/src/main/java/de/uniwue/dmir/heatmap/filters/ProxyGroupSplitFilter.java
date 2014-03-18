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
package de.uniwue.dmir.heatmap.filters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.groupaccess.IGroupAccess;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * Allows to create tiles which contain grouped data, i.e.,
 * the tile containers a set of tiles: one tile for each group.
 * 
 * At the same time it is possible to apply different filters for each group 
 * based on the group name.
 * 
 * @author Martin Becker
 *
 * @param <TData>
 * @param <TGroupTile>>
 * @param <TGroupContainerTile>>
 */
public class ProxyGroupSplitFilter<TData, TGroupTile, TGroupContainerTile> 
extends AbstractFilter<TData, TGroupContainerTile> 
implements IConfigurableFilter<TData, TGroupContainerTile>{

	@Setter
	@Getter
	private int width;

	@Setter
	@Getter
	private int height;

	@Setter
	@Getter
	private int centerX;

	@Setter
	@Getter
	private int centerY;
	
	private IMapper<? super TData, List<String>> groupIdMapper;
	private IGroupAccess<TGroupTile, TGroupContainerTile> groupAccess;
	private Map<String, IFilter<TData, TGroupTile>> filters;
	
	public ProxyGroupSplitFilter(
			IMapper<? super TData, List<String>> groupIdMapper,
			IGroupAccess<TGroupTile, TGroupContainerTile> groupAccess, 
			Map<String, IFilter<TData, TGroupTile>> filters) {

		this.groupIdMapper = groupIdMapper;
		this.groupAccess = groupAccess;
		this.filters = filters;
		
		// calculate width, height, center x and center y
		FilterDimensions<TData, TGroupTile> filterDimensions = 
				new FilterDimensions<TData, TGroupTile>(filters.values());
		filterDimensions.setDimensions(this);
	}
	
	@SuppressWarnings("serial")
	public ProxyGroupSplitFilter(
			IMapper<? super TData, List<String>> groupIdMapper,
			IGroupAccess<TGroupTile, TGroupContainerTile> groupAccess, 
			final IFilter<TData, TGroupTile> filter) {

		this(
				groupIdMapper, 
				groupAccess, 
				new HashMap<String, IFilter<TData, TGroupTile>>() {{
					put(null, filter);
				}});
	
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
			
			// get filter
			IFilter<TData, TGroupTile> filter = this.filters.get(groupId);
			if (filter == null && groupId != null) {
				// try to get default filter
				filter = this.filters.get(null);
			}
			
			if (filter != null) {
				filter.filter(
						dataPoint, 
						groupData, 
						tileSize, 
						tileCoordinates);
			}
		}
		
	}
	
	

}