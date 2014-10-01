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
package de.uniwue.dmir.heatmap.filters;

import java.util.Collection;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class NoFilter<TPoint, TTile> implements IFilter<TPoint, TTile> {

	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	public NoFilter() {
		this(1,1,0,0);
	}
	
	public NoFilter(int width, int height, int centerX, int centerY) {
		super();
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	@Override
	public <TDerived extends TPoint> void filter(
			TDerived dataPoint,
			TTile tile, 
			TileSize tileSize, 
			TileCoordinates tileCoordinates) {
	}

	@Override
	public <TDerived extends TPoint> void filter(
			Collection<TDerived> dataPoints, 
			TTile tile, 
			TileSize 
			tileSize,
			TileCoordinates tileCoordinates) {
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

}
