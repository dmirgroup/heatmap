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
package de.uniwue.dmir.heatmap.filters.pixelaccess;

import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.util.Arrays2d;

public class ArrayPixelAccess<I> 
implements IPixelAccess<I, I[]>{

	@Override
	public I get(
			RelativeCoordinates relativeCoordinates, 
			I[] tile,
			TileSize tileSize) {

		return Arrays2d.get(
				relativeCoordinates.getX(), 
				relativeCoordinates.getY(), 
				tile, 
				tileSize.getWidth(),
				tileSize.getHeight());
	}
	
	@Override
	public <TDerived extends I> void set(
			TDerived pixelValue, 
			RelativeCoordinates relativeCoordinates,
			I[] tile, 
			TileSize tileSize) {
		
		Arrays2d.set(
				pixelValue,
				relativeCoordinates.getX(), 
				relativeCoordinates.getY(), 
				tile, 
				tileSize.getWidth(),
				tileSize.getHeight());
	}

}
