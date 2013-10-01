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
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Tile<I> implements ITile<I[]>{

	@Getter
	private TileSize size;
	
	@Getter
	private TileCoordinates coordinates;
	
	@Getter
	@Setter
	private I[] data;
	
	@SuppressWarnings("unchecked")
	public Tile(
			TileSize tileSize,
			TileCoordinates coordinates,
			Class<I> clazz) {

		this(
				tileSize, 
				coordinates, 
				(I[]) Array.newInstance(
						clazz, 
						tileSize.getWidth() * tileSize.getHeight()));
		
	}
	
	public Tile(
			TileSize tileSize,
			TileCoordinates coordinates,
			I[] initialData) {
		
		if (tileSize == null) {
			throw new IllegalArgumentException(
					"Tile dimensions must not be null.");
		}
		
		if (coordinates == null) {
			throw new IllegalArgumentException(
					"Coordinates must not be null.");
		}
		
		if (initialData == null) {
			throw new IllegalArgumentException(
					"Initial data must not be null, but may be empty.");
		}
		
		this.size = tileSize;
		this.coordinates = coordinates;
		
		int length = this.size.getWidth() * this.size.getHeight();
		if (initialData.length != length) {
			throw new IllegalArgumentException(
					"Initial data must have a certain size: " + length);
		}
	}
}
