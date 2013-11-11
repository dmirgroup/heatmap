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
package de.uniwue.dmir.heatmap.core.filters;

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.filters.access.IPixelAccess;
import de.uniwue.dmir.heatmap.core.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public class AddingFilter<TData, TPixel, TTile> 
extends AbstractRelativeCoordinatesMapperFilter<TData, TTile> {

	private IPixelAccess<TPixel, TTile> pixelAccess;
	
	private IMapper<TData, TPixel> dataToPixelMapper;
	private IAdder<TPixel> adder;
	
	public AddingFilter(
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IToRelativeCoordinatesMapper<TData> toRelativeCoordinatesMapper,
			IMapper<TData, TPixel> dataToPixelMapper, 
			IAdder<TPixel> pixelAdder) {
		
		super(toRelativeCoordinatesMapper);
		
		this.pixelAccess = pixelAccess;
		this.dataToPixelMapper = dataToPixelMapper;
		this.adder = pixelAdder;
	}
	
	public void filter(
			TData dataPoint, 
			RelativeCoordinates relativeCoordinates,
			TTile tileData, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {

		TPixel addable = this.dataToPixelMapper.map(dataPoint);
		
		TPixel currentValue = this.pixelAccess.get(
				relativeCoordinates,
				tileData,
				tileSize);
		
		TPixel sum;
		if (currentValue == null) {
			sum = addable;
		} else {
			sum = this.adder.add(addable, currentValue);
		}
		
		this.pixelAccess.set(
				sum, 
				relativeCoordinates, 
				tileData, 
				tileSize);
	}
	
}