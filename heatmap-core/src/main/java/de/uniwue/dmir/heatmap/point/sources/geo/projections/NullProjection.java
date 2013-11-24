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
package de.uniwue.dmir.heatmap.point.sources.geo.projections;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class NullProjection 
implements IMapProjection {

	@Override
	public GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates, 
			IFilter<?, ?> filter) {

		return null;
	}

	@Override
	public RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates, 
			TileCoordinates tileCoordinates) {
		
		return null;
	}

	@Override
	public List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates, 
			int zoom, 
			IFilter<?, ?> filter) {
		
		List<TileCoordinates> tileCoordinateList = 
				new ArrayList<TileCoordinates>();

		TileCoordinates tileCoordinates =
				new TileCoordinates(0, 0, zoom);
		
		tileCoordinateList.add(tileCoordinates);
		
		return tileCoordinateList;
	}

}
