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
package de.uniwue.dmir.heatmap.core.tile;

import java.lang.reflect.Array;

import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.TileDimensions;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Tile<E extends IExternalData, I> implements ITile<E, I>{

	@Getter
	private TileDimensions dimensions;
	
	@Getter
	private TileCoordinates coordinates;
	
	private IFilter<E, I> filter;

	private I[] tileData;
	
	@SuppressWarnings("unchecked")
	public Tile(
			TileDimensions tileDimensions,
			TileCoordinates coordinates,
			IFilter<E, I> filter, 
			I[] initialData) {
		
		if (tileDimensions == null) {
			throw new IllegalArgumentException("Tile dimensions must not be null.");
		}
		
		if (coordinates == null) {
			throw new IllegalArgumentException("Coordinates must not be null.");
		}
		
		if (filter == null) {
			throw new IllegalArgumentException("Filter must not be null.");
		}
		
		if (initialData == null) {
			throw new IllegalArgumentException(
					"Initial data must not be null, but may be empty.");
		}
		
		this.dimensions = tileDimensions;
		this.coordinates = coordinates;
		this.filter = filter;
		
		int length = this.dimensions.getWidth() * this.dimensions.getHeight();
		if (initialData.length != 0 && initialData.length != length) {
			throw new IllegalArgumentException(
					"Initial data must have a certain size: " + length);
		}
		
		if (initialData.length > 0) {
			this.tileData = initialData;
		} else {
			this.tileData = (I[]) Array.newInstance(
					initialData.getClass().getComponentType(), 
					length);
		}

	}
	
	public void add(E dataPoint) {
		this.filter.filter(dataPoint, this);
	}
	
	public I[] getData() {
		return this.tileData;
	}

}
