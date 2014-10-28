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
package de.uniwue.dmir.heatmap.point.sources.geo;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileRange;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapProjectionUtils {
	
	public static GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			IMapProjection mapProjection,
			int zoom,
			TileRange tileRange,
			IFilter<?, ?> filter) {
		
		GeoBoundingBox bottomLeft = mapProjection.fromTileCoordinatesToGeoBoundingBox(
				new TileCoordinates(tileRange.getMinX(), tileRange.getMaxY(), zoom),
				filter);
		
		GeoBoundingBox topRight = mapProjection.fromTileCoordinatesToGeoBoundingBox(
				new TileCoordinates(tileRange.getMaxX(), tileRange.getMinY(), zoom),
				filter);
		
		return new GeoBoundingBox(bottomLeft.getMin(), topRight.getMax());
		
	}
}
