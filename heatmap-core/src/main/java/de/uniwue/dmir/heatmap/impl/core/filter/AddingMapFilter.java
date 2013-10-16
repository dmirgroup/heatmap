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

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;

@AllArgsConstructor
public class AddingMapFilter<T extends IExternalData, P extends IExternalData> 
extends AbstractFilter<T, Map<RelativeCoordinates, P>> {

	private IMapper<T, P> mapper;
	private IAdder<P> adder;
	
	@Getter
	@Setter
	private int width;
	
	@Getter
	@Setter
	private int height;

	@Getter
	@Setter
	private int centerX;

	@Getter
	@Setter
	private int centerY;
	
	public AddingMapFilter(IMapper<T, P> mapper, IAdder<P> adder) {
		this(mapper, adder, 1, 1, 0, 0);
	}
	
	public void filter(
			T dataPoint, 
			Map<RelativeCoordinates, P> tileData, 
			TileSize tileSize) {

		P addable = this.mapper.map(dataPoint);
		
		P currentValue = tileData.get(addable.getCoordinates());
		
		P sum;
		if (currentValue == null) {
			sum = addable;
		} else {
			sum = this.adder.add(addable, currentValue);
		}
		
		tileData.put(addable.getCoordinates(), sum);
		
	}
	
}