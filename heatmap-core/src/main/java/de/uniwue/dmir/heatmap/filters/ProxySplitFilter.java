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

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * Filter which allows to change filter based on the given data point.
 * 
 * @author Martin Becker
 *
 * @param <TData> type of data point
 * @param <TTile> type of tile
 */
public class ProxySplitFilter<TData, TTile> 
extends AbstractFilter<TData, TTile> 
implements IConfigurableFilter<TData, TTile> {

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
	
	private IMapper<? super TData, String> filterIdMapper;
	private Map<String, IFilter<TData, TTile>> filters;
	
	public ProxySplitFilter(			
			IMapper<? super TData, String> filterIdMapper,
			Map<String, IFilter<TData, TTile>> filters) {

		this.filterIdMapper = filterIdMapper;
		this.filters = filters;
		
		// calculate width, height, center x and center y
		FilterDimensions<TData, TTile> filterDimensions = 
				new FilterDimensions<TData, TTile>(filters.values());
		filterDimensions.setDimensions(this);
	}

	@Override
	public void filter(
			TData dataPoint, 
			TTile tile,
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		// ger filter id
		String filterId = this.filterIdMapper.map(dataPoint);
		
		// get filter
		IFilter<TData, TTile> filter = this.filters.get(filterId);
		if (filter == null && filterId != null) {
			// try to get default filter
			filter = this.filters.get(null);
		}
		
		// filter
		if (filter != null) {
			filter.filter(
					dataPoint, 
					tile, 
					tileSize, 
					tileCoordinates);
		}
		
	}
}
