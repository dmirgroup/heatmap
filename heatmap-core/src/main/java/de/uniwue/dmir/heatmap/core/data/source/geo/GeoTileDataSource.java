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
package de.uniwue.dmir.heatmap.core.data.source.geo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IExternalDataSource;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.data.type.IToInternalDataMapper;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * A {@link IExternalDataSource} which works on a {@link IGeoDataSource} and
 * allows to specify the used map projection.
 * 
 * @author Martin Becker
 *
 * @param <S> souce data type
 * @param <T> tile data type
 */
@AllArgsConstructor
public class GeoTileDataSource<S, T extends IExternalData> 
implements IExternalDataSource<T> {

	private IGeoDataSource<S> geoDataSource;
	private IMapProjection projection;
	
	private IToGeoCoordinatesMapper<S> coordinateMapper;
	private IToInternalDataMapper<S, T> externalDataMapper;
	
	public Iterator<T> getData(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter) {
		
		GeoBoundingBox geoBoundingBox = 
				this.projection.fromTileCoordinatesToGeoBoundingBox(
						tileCoordinates, 
						filter);
		
		List<S> sourceData = this.geoDataSource.getData(
				geoBoundingBox.getNorthWest().getLongitude(), 
				geoBoundingBox.getNorthWest().getLatitude(), 
				geoBoundingBox.getSouthEast().getLongitude(), 
				geoBoundingBox.getSouthEast().getLatitude());
		
		return convert(sourceData, tileCoordinates).iterator();
	}
	
	@Override
	public Iterator<TileCoordinates> getTileCoordinatesWithContent(
			int zoom,
			IFilter<?, ?> filter) {
		
		List<S> sourceData = this.geoDataSource.getData(
				-180,
				  90,
				 180,
				 -90);
		
		Set<TileCoordinates> coordinates = new HashSet<TileCoordinates>();
		
		for (S data : sourceData) {

			GeoCoordinates geoCoordinates = this.coordinateMapper.map(data);
			
			List<TileCoordinates> tileCoordinates =
					this.projection.overlappingTiles(geoCoordinates, zoom, filter);

			coordinates.addAll(tileCoordinates);
		}
		
		return coordinates.iterator();
	}
	
	/**
	 * @param sourceData source data
	 * @param tileCoordinates the coordinates of the tile the data is associated with
	 * @return tile data with pixel coordinates relative to the given tile
	 */
	private List<T> convert(
			List<S> sourceData, 
			TileCoordinates tileCoordinates) {
		
		List<T> converted = new ArrayList<T>(sourceData.size());
		for (S object : sourceData) {
			
			GeoCoordinates geoCoordinates = 
					this.coordinateMapper.map(object);
			
			RelativeCoordinates relativeCoordinates =
					this.projection.fromGeoToRelativeCoordinates(
							geoCoordinates, 
							tileCoordinates);
			
			T convertedObject = 
					this.externalDataMapper.map(object, relativeCoordinates);
			
			converted.add(convertedObject);
		}
		
		return converted;
	}
}
