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
package de.uniwue.dmir.heatmap.tiles.coordinates.projection;

import de.uniwue.dmir.heatmap.IZoomLevelSizeProvider;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * For tile indexing, we adopted a top-left centered indexing scheme with
 * increasing coordinate values from top to bottom and from left to right.
 * In order to allow the heat map to serve tiles with other indexing schemes,
 * this projection is used (e.g. for TMS layers) to convert tile coordinates.
 * 
 * @author Martin Becker
 */
public interface ITileCoordinatesProjection {
	
	TileCoordinates fromCustomToTopLeft(
			TileCoordinates tileCoordinates,
			IZoomLevelSizeProvider zoomLevelMapper);
	
	TileCoordinates fromTopLeftToCustom(
			TileCoordinates tileCoordinates,
			IZoomLevelSizeProvider zoomLevelMapper);
}
