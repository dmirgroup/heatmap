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
package de.uniwue.dmir.heatmap.core.data.sources.geo;

import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public interface IMapProjection {
	
	/**
	 * @param tileCoordinates tile coordinates
	 * @param filter filter to be used for the tile
	 * 
	 * @return geo bounding box corresponding to the tile and the filter
	 */
	GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter);
	
	/**
	 * @param geoCoordinates geo coordinates
	 * @return relative pixel coordinates within a tile
	 */
	RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates,
			TileCoordinates tileCoordinates);
	
	
	List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates,
			int zoom,
			IFilter<?, ?> filter);
	

}
