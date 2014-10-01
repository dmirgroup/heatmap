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
package de.uniwue.dmir.heatmap;

import java.util.Iterator;

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * Data source to retrieve data relevant for a tile.
 * Such data is always a set of "points". 
 * Points are an abstract concept and may also be any kind of spatial object,
 * like lines, polygons etc..
 * 
 * @author Martin Becker
 *
 * @param <TPoint> data type to retrieve
 * @param <TParameters> parameters when accessing data
 */
public interface IPointsource<TPoint, TParameters> {

	/**
	 * @param tileCoordinates coordinates of tile to retrieve data points for
	 * @param parameters parameters influencing the point retrieval process
	 * @param filter the filter used for the tiles; dependent on the size of 
	 * 		the filter, the data relevant for the tile might change
	 * 
	 * @return all data points relevant for the tile with the given coordinates
	 */
	Iterator<TPoint> getPoints(
			TileCoordinates tileCoordinates,
			TParameters parameters,
			IFilter<?, ?> filter);
	
	/**
	 * @param zoom the zoom level to get the tiles with contents for
	 * @param tileRange limit the returned tiles to a certain range;
	 * 		may be <code>null</code> (in this case take the full range)
	 * @param filter the filter used for the tiles; dependent on the size of 
	 * 		the filter, the data relevant for the tile might change
	 * 
	 * @return tile which have data for the given zoom level and tile range
	 */
	Iterator<TileCoordinates> getTileCoordinatesWithContent(
			int zoom,
			TileRange tileRange,
			TParameters parameters,
			IFilter<?, ?> filter);

}
