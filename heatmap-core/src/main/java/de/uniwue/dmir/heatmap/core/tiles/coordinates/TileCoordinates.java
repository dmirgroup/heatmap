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
package de.uniwue.dmir.heatmap.core.tiles.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.ITileCoordinatesProjection;

/**
 * <p>Coordinates of a tile consisting of zoom level, and (x,y) coordinates.</p>
 * 
 * Examples:
 * <ul>
 * <li>the coordinates of the top-left tile on zoom level z are (0,0,z)</li>
 * <li>the coordinates of tile to the right of (0,0,z) are (1,0,z)</li>
 * </ul>
 * 
 * <p><strong>Note</strong>, that the (0, 0) coordinates do not necessarily 
 * define the top-left corner of the tile grid; in some cases (0, 0) might be 
 * the bottom-left corner or in the middle of the tile grid depending on the 
 * given {@link ITileCoordinatesProjection}.</p>
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class TileCoordinates {
	
	private long x;
	private long y;
	
	private int zoom;
}
