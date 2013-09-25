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
package de.uniwue.dmir.heatmap.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * Data source to retrieve data relevant for a tile.
 * 
 * @author Martin Becker
 *
 * @param <T> data type to retrieve
 */
public interface IExternalDataSource<T extends IExternalData> {

	/**
	 * @param tileCoordinates coordinates of tile to retrieve data for
	 * @param filter the filter used for the tiles (dependent on the size of 
	 * 		the filter, the data relevant for the tile might change)
	 * 
	 * @return all data relevant for the tile with the given coordinates
	 */
	List<T> getData(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter);
	
	/**
	 * @param zoomStart
	 * @param zoomStop
	 * @param filter
	 * 
	 * @return tile which have data for the given zoom level
	 */
	Map<Integer, Set<TileCoordinates>> getNonEmptyTiles(
			int zoomStart, 
			int zoomStop,
			IFilter<?, ?> filter);

}
