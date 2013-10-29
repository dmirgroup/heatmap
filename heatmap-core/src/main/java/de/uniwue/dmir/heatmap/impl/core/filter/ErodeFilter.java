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
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

@AllArgsConstructor
@Getter
public class ErodeFilter<T extends IExternalData, P>
extends AbstractFilter<T, P[]> {

	private IMapper<T, P> mapper;
	private IAdder<P> adder;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;

	public void filter(
			T dataPoint, 
			P[] tileData, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		int startX = dataPoint.getCoordinates().getX();
		int startY = dataPoint.getCoordinates().getY();
		startX -= this.centerX;
		startY -= this.centerY;
		
		int stopX = startX + this.width;
		int stopY = startY + this.height;
		
		for (int x = startX; x < stopX; x++) {
			for (int y = startY; y < stopY; y ++) {
				
				if (!Arrays2d.isIndexWithinBounds(
						x, 
						y, 
						tileSize.getWidth(), 
						tileSize.getHeight())) {
					continue;
				}
				
				
				P addable = this.mapper.map(dataPoint);
				P currentValue = Arrays2d.get(
						x, y, 
						tileData, 
						tileSize.getWidth(), 
						tileSize.getHeight());
				
				P sum;
				if (currentValue == null) {
					sum = addable;
				} else {
					sum = this.adder.add(currentValue, addable);
				}
				
				Arrays2d.set(
						sum, x, y, 
						tileData, 
						tileSize.getWidth(), 
						tileSize.getHeight());
			}
		}
	}
}
