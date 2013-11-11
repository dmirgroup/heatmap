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

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.projections.EquidistantProjection;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;

@AllArgsConstructor
public class EquidistantProjectionTileSizeFactoryBean 
implements FactoryBean<TileSize> {
	
	private double widthMeters;
	private double heightMeters;
	private boolean northReference;
	private GeoBoundingBox geoBoundingBox;
	private IDistanceFunction<GeoCoordinates> distanceFunction;
	
	@Override
	public TileSize getObject() throws Exception {
		return EquidistantProjection.getTileSize(
				this.widthMeters, 
				this.heightMeters, 
				this.northReference, 
				this.geoBoundingBox, 
				this.distanceFunction);
	}
	@Override
	public Class<TileSize> getObjectType() {
		return TileSize.class;
	}
	@Override
	public boolean isSingleton() {
		return false;
	}

}
