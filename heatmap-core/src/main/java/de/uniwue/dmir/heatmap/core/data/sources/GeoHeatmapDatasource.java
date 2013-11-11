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
package de.uniwue.dmir.heatmap.core.data.sources;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

/**
 * A {@link IHeatmapDatasource} which works on a {@link IGeoDatasource} and
 * allows to specify the used map projection.
 * 
 * @author Martin Becker
 *
 * @param <TSourceData> source data type
 * @param <TExternalData> tile data type
 */
@AllArgsConstructor
public class GeoHeatmapDatasource<TData> 
implements IHeatmapDatasource<TData> {

	private IGeoDatasource<TData> geoDataSource;
	private IMapProjection projection;

	private IMapper<TData, GeoCoordinates> toGeoCoordinatesMapper;
	
	public Iterator<TData> getData(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter) {
		
		GeoBoundingBox geoBoundingBox = 
				this.projection.fromTileCoordinatesToGeoBoundingBox(
						tileCoordinates, 
						filter);
		
		List<TData> sourceData = this.geoDataSource.getData(
				geoBoundingBox);
		
		return sourceData.iterator();
	}
	
	@Override
	public Iterator<TileCoordinates> getTileCoordinatesWithContent(
			int zoom,
			IFilter<?, ?> filter) {
		
		List<TData> sourceDataSet = this.geoDataSource.getData(null);
		
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
