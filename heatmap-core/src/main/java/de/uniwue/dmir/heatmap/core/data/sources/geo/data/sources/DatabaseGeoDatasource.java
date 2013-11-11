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
package de.uniwue.dmir.heatmap.core.data.sources.geo.data.sources;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.sources.database.RequestGeo;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.mybatis.GeoPointMapper;

public class DatabaseGeoDatasource<TGroupDescription>
implements IGeoDatasource<GeoPoint<TGroupDescription>> {

	private RequestGeo request;
	
	@Autowired
	@Getter
	@Setter
	private GeoPointMapper<TGroupDescription> mapper;
	
	public DatabaseGeoDatasource(RequestGeo request) {
		
		if (request.getTable() == null 
				&& request.getLongitudeAttribute() == null 
				&& request.getLatitudeAttribute() == null) {
			
			throw new IllegalArgumentException("");
		}
		
		this.request = request;
	}
	
	@Override
	public List<GeoPoint<TGroupDescription>> getData(
			GeoBoundingBox geoBoundingBox) {
		
		if (geoBoundingBox != null) {
			this.request.setWest(geoBoundingBox.getMin().getLongitude());
			this.request.setEast(geoBoundingBox.getMax().getLongitude());
			this.request.setSouth(geoBoundingBox.getMin().getLatitude());
			this.request.setNorth(geoBoundingBox.getMax().getLatitude());
		}
		
		List<GeoPoint<TGroupDescription>> points = 
				this.mapper.getData(this.request);

		return points;
	}

}
