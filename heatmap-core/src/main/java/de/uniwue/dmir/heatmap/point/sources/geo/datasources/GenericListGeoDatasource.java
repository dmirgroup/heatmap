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
package de.uniwue.dmir.heatmap.point.sources.geo.datasources;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * A simple (not optimized) geo datasource implementation holds a list of data points and
 * scans this list for each {@link #getData(GeoBoundingBox)} request.
 * During each scan it extracts {@link GeoCoordinates} from each 
 * data point using the given {@link IMapper} and check if that point falls into
 * the requested {@link GeoBoundingBox}.
 * 
 * @author Martin Becker
 *
 * @param <TData>
 */
public class GenericListGeoDatasource<TData, TParameters> 
implements IGeoDatasource<TData, TParameters> {

	private IMapper<TData, GeoCoordinates> toGeoCoordinatesMapper;
	
	@Getter
	private List<TData> list;
	
	public GenericListGeoDatasource(IMapper<TData, GeoCoordinates> toGeoCoordinatesMapper) {
		this(new ArrayList<TData>(), toGeoCoordinatesMapper);
	}
	
	public GenericListGeoDatasource(
			List<TData> list, 
			IMapper<TData, GeoCoordinates> toGeoCoordinatesMapper) {
		
		this.list = list;
		this.toGeoCoordinatesMapper = toGeoCoordinatesMapper;
	}
	
	public List<TData> getData(GeoBoundingBox geoBoundingBox, TParameters parameters) {
		
		// return everything if no bounding box is given
		if (geoBoundingBox == null) {
			return new ArrayList<TData>(this.list);
		}
		
		// calculate result
		List<TData> result = new ArrayList<TData>();
		for (TData d : this.list) {
			
			GeoCoordinates geoCoordinates = this.toGeoCoordinatesMapper.map(d);
			
			if (
					geoCoordinates.getLongitude() 
						>= geoBoundingBox.getMin().getLongitude()
					&& geoCoordinates.getLongitude() 
						<= geoBoundingBox.getMax().getLongitude()
					&& geoCoordinates.getLatitude() 
						>= geoBoundingBox.getMin().getLatitude()
					&& geoCoordinates.getLatitude() 
						<= geoBoundingBox.getMax().getLatitude()) {
				
				result.add(d);
			}
		}
		
		return result;
	}
}
