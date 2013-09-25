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

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

@AllArgsConstructor
public class SimpleAdderFilter<T extends IExternalData, P> 
implements IFilter<T, P> {

	private IMapper<T, P> mapper;
	private IAdder<P> adder;
	
	public void filter(T dataPoint, ITile<T, P> tile) {
		
		P[] tileData = tile.getData();
		
		int tileWidth = tile.getDimensions().getWidth();
		int tileHeight = tile.getDimensions().getHeight();
		
		P addable = this.mapper.map(dataPoint);
		
		P currentValue = Arrays2d.get(
				dataPoint.getCoordinates().getX(), 
				dataPoint.getCoordinates().getY(), 
				tileData,
				tileWidth, 
				tileHeight);
		
		P sum;
		if (currentValue == null) {
			sum = addable;
		} else {
			sum = this.adder.add(addable, currentValue);
		}
		
		Arrays2d.set(
				sum, 
				dataPoint.getCoordinates().getX(), 
				dataPoint.getCoordinates().getY(), 
				tileData, 
				tileWidth,
				tileHeight);
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	public int getCenterX() {
		return 0;
	}

	public int getCenterY() {
		return 0;
	}
	
}