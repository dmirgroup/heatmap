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
package de.uniwue.dmir.heatmap.impl.core.filter;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.IPixelAccess;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class AddingFilter<E extends IExternalData, I, T> 
extends AbstractConfigurableFilter<E, T> {

	private IPixelAccess<I, T> pixelAccess;
	
	private IMapper<E, I> mapper;
	private IAdder<I> adder;
	
	public AddingFilter(
			IPixelAccess<I, T> pixelAccess, 
			IMapper<E, I> mapper, 
			IAdder<I> adder) {
		
		this.pixelAccess = pixelAccess;
		this.mapper = mapper;
		this.adder = adder;
	}
	
	public void filter(
			E dataPoint, 
			T tileData, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {

		I addable = this.mapper.map(dataPoint);
		
		I currentValue = this.pixelAccess.get(
				dataPoint.getCoordinates(),
				tileData,
				tileSize);
		
		I sum;
		if (currentValue == null) {
			sum = addable;
		} else {
			sum = this.adder.add(addable, currentValue);
		}
		
		this.pixelAccess.set(
				sum, 
				dataPoint.getCoordinates(), 
				tileData, 
				tileSize);
	}
	
}