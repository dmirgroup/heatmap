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
package de.uniwue.dmir.heatmap.core.data.sources.geo.mappers;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.IGeoPoint;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

@AllArgsConstructor
public class GeoPointToRelativeCoordinatesMapper<TData extends IGeoPoint>
implements IToRelativeCoordinatesMapper<TData>{

	private IMapProjection mapProjection;
	
	@Override
	public RelativeCoordinates map(
			TData dataPoint,
			TileCoordinates tileCoordinates) {

		return this.mapProjection.fromGeoToRelativeCoordinates(
				dataPoint.getGeoCoordinates(), 
				tileCoordinates);
	}

}
