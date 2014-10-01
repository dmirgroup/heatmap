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

import java.util.Collection;

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * A filter merges a given data point into a given tile.
 * 
 * @author Martin Becker
 *
 * @param <TPoint> data type (external) to merge into the tile 
 * @param <TTile> data type (internal) for merged data as stored in the tile
 */
public interface IFilter<TPoint, TTile> {

	/**
	 * Merges a given data point into the given tile.
	 * 
	 * @param dataPoint data point to merge into the tile
	 * @param tile tile to merge data into; if this is <code>null</code> nothing will happen
	 * @param tileSize size of the tile
	 */
	<TDerived extends TPoint> void filter(
			TDerived dataPoint, 
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates);
	
	/**
	 * Merges given data points into the given tile.
	 * 
	 * @param dataPoints data to merge into the tile
	 * @param tile tile to merge data into; if this is <code>null</code> nothing will happen
	 */
	<TDerived extends TPoint> void filter(
			Collection<TDerived> dataPoints, 
			TTile tile,
			TileSize tileSize,
			TileCoordinates tileCoordinates);
	
	/**
	 * @return width
	 */
	int getWidth();
	
	/**
	 * @return height
	 */
	int getHeight();
	
	/**
	 * @return x coordinate (range from 0 to width-1) of filter center
	 */
	int getCenterX();
	
	/**
	 * @return y coordinate (range from 0 to height-1) of filter center
	 */
	int getCenterY();
	
}
