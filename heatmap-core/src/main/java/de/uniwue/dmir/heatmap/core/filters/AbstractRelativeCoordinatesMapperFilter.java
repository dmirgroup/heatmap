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

import lombok.ToString;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

/** 
 * Abstract filter handling the mapping from the given data type to relative
 * coordinates using an {@link IToRelativeCoordinatesMapper}.
 * 
 * @author Martin Becker
 *
 * @param <TData> type of the data to be incorporated into the tile
 * @param <TTile> type of the tile to incorporate data into
 */
@ToString(callSuper = true)
public abstract class AbstractRelativeCoordinatesMapperFilter<TData, TTile> 
extends AbstractConfigurableFilter<TData, TTile> {

	protected IToRelativeCoordinatesMapper<TData> dataToRelativeCoordinatesMapper;
	
	public AbstractRelativeCoordinatesMapperFilter(IToRelativeCoordinatesMapper<TData> toRelativeCoordinatesMapper) {
		this.dataToRelativeCoordinatesMapper = toRelativeCoordinatesMapper;
	}
	
	public abstract void filter(
			TData dataPoint, 
			RelativeCoordinates relativeCoordinates,
			TTile tile,
			TileSize tileSize,
			TileCoordinates tileCoordinates);
	
	@Override
	public void filter(
			TData dataPoint, 
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		RelativeCoordinates relativeCoordinates = 
				this.dataToRelativeCoordinatesMapper.map(dataPoint, tileCoordinates);
		
		this.filter(
				dataPoint, 
				relativeCoordinates, 
				tile, 
				tileSize, 
				tileCoordinates);
	}
	
}
