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

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.projection.ITileCoordinatesProjection;

/**
 * A {@link ITileCoordinatesProjection} 
 * 
 * @author Martin Becker
 *
 * @param <TTile>
 */
public interface ITileProcessor<TTile> {
	
	/**
	 * Process a given tile.
	 * 
	 * @param tile tile to process
	 * @param tileSize the size of the tile to process
	 * @param tileCoordinates the coordinates of the given tile
	 */
	void process(
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates);
	
	/**
	 * Closes to processor.
	 * Releasing file handles, cached data etc.
	 */
	void close();
}