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
package de.uniwue.dmir.heatmap.core.util;

import java.awt.Polygon;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;

import de.uniwue.dmir.heatmap.core.data.source.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.processing.PolygonRelativeCoordinatesFilter;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class PolygonFromGeoPolygonFacoryBean 
implements FactoryBean<Polygon> {

	private GeoPolygon geoPolygon;
	private TileCoordinates tileCoordinates;
	private IMapProjection mapProjection;
	
	@Override
	public Polygon getObject() throws Exception {
		return PolygonRelativeCoordinatesFilter.fromGeoPolygon(
				this.geoPolygon, 
				this.tileCoordinates, 
				this.mapProjection);
	}

	@Override
	public Class<?> getObjectType() {
		return Polygon.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
