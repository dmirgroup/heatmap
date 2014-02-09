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
package de.uniwue.dmir.heatmap.point.sources;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.IPointsource;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.AbstractConfigurableFilter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * A {@link IPointsource} which works on a {@link IGeoDatasource} and
 * allows to specify the used map projection.
 * 
 * @author Martin Becker
 *
 */
@AllArgsConstructor
public class GeoPointsource<TData> 
implements IPointsource<TData> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IGeoDatasource<TData> geoDatasource;
	private IMapProjection projection;

	private IMapper<TData, GeoCoordinates> toGeoCoordinatesMapper;
	
	public Iterator<TData> getPoints(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter) {
		
		this.logger.debug(
				"Filter dimensions: width={}, height={}, centerX={}, centerY={}",
				filter.getWidth(), 
				filter.getHeight(), 
				filter.getCenterX(), 
				filter.getCenterY());
		
		this.logger.debug(
				"Tile bounding box: {}", 
				this.projection.fromTileCoordinatesToGeoBoundingBox(
						tileCoordinates, 
						new AbstractConfigurableFilter<Object, Object>() {
							@Override
							public void filter(Object dataPoint, Object tile,
									TileSize tileSize,
									TileCoordinates tileCoordinates) {
							}
						}));
		
		GeoBoundingBox geoBoundingBox = 
				this.projection.fromTileCoordinatesToGeoBoundingBox(
						tileCoordinates, 
						filter);
		
		this.logger.debug("Extended bounding box: {}", geoBoundingBox);
		
		List<TData> sourceData = this.geoDatasource.getData(geoBoundingBox);
		
		return sourceData.iterator();
	}
	
	@Override
	public Iterator<TileCoordinates> getTileCoordinatesWithContent(
			int zoom,
			IFilter<?, ?> filter) {
		
		List<TData> sourceDataSet = this.geoDatasource.getData(null);
		
		Set<TileCoordinates> coordinates = new HashSet<TileCoordinates>();
		
		for (TData data : sourceDataSet) {

			GeoCoordinates geoCoordinates = 
					this.toGeoCoordinatesMapper.map(data);
			
			List<TileCoordinates> tileCoordinates =
					this.projection.overlappingTiles(
							geoCoordinates, 
							zoom, 
							filter);

			coordinates.addAll(tileCoordinates);
		}
		
		return coordinates.iterator();
	}
	
}
