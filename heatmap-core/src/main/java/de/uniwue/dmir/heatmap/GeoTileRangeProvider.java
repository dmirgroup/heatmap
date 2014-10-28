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

import java.util.List;

import de.uniwue.dmir.heatmap.filters.NullFilter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox.GeoBoundingBoxCorners;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class GeoTileRangeProvider 
implements ITileRangeProvider {

	private IMapProjection mapProjection;
	
	private GeoBoundingBoxCorners corners;
	private NullFilter<?, ?> filter = new NullFilter<Object, Object>();
	
	public GeoTileRangeProvider(
			GeoBoundingBox geoBoundingBox,
			IMapProjection mapProjection) {
		
		this.corners = geoBoundingBox.getCorners();
		this.mapProjection = mapProjection;
	}
	
	@Override
	public TileRange getTileRange(int zoom) {
		
		List<TileCoordinates> topLeftList = 
				this.mapProjection.overlappingTiles(
						this.corners.getTopLeft(),
						zoom,
						this.filter);
		
		List<TileCoordinates> bottomRightList = 
				this.mapProjection.overlappingTiles(
						this.corners.getBottomRight(),
						zoom, 
						this.filter);
		
		TileCoordinates topLeft = topLeftList.get(0);
		TileCoordinates bottomRight = bottomRightList.get(0);
		
		TileRange range = new TileRange();
		range.setMinX(topLeft.getX());
		range.setMinY(topLeft.getY());
		range.setMaxX(bottomRight.getX());
		range.setMaxY(bottomRight.getY());
		
		return range;
	}

}
