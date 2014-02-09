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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.filters.pixelaccess.IPixelAccess;
import de.uniwue.dmir.heatmap.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.Arrays2d;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

@Getter
@Setter
@ToString(callSuper = true)
public class ErodingFilter<TData, TPixel, TTile>
extends AbstractPixelAccessFilter<TData, TPixel, TTile> {

	protected int erodingWidth;
	protected int erodingHeight;
	protected int erodingCenterX;
	protected int erodingCenterY;;
	
	private IMapper<? super TData, TPixel> dataToTPixelixelMapper;
	private IAdder<TPixel> adder;
	
	private boolean skipOutOfBoundsPoints;
	
	public ErodingFilter(
			IToRelativeCoordinatesMapper<TData> dataToRelativeCoordinatesMapper,
			IMapper<? super TData, TPixel> dataToTPixelixelMapper,
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IAdder<TPixel> pixelAdder,
			
			int width,
			int height,
			int centerX, 
			int centerY) {

		super(dataToRelativeCoordinatesMapper, pixelAccess);
		
		this.dataToTPixelixelMapper = dataToTPixelixelMapper;
		this.adder = pixelAdder;

		this.erodingWidth = 	this.width = 	width;
		this.erodingHeight = 	this.height = 	height;
		this.erodingCenterX = 	this.centerX = 	centerX;
		this.erodingCenterY = 	this.centerY = 	centerY;
		
		this.skipOutOfBoundsPoints = false;
	}

	public void filter(
			TData dataTPixeloint, 
			RelativeCoordinates relativeCoordinates,
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		int startX = relativeCoordinates.getX();
		int startY = relativeCoordinates.getY();
		startX -= this.erodingCenterX;
		startY -= this.erodingCenterY;
		
		int stopX = startX + this.erodingWidth;
		int stopY = startY + this.erodingHeight;
		
		for (int x = startX; x < stopX; x++) {
			for (int y = startY; y < stopY; y ++) {
				
				if (this.skipOutOfBoundsPoints
						&& !Arrays2d.isIndexWithinBounds(
								x, 
								y, 
								tileSize.getWidth(), 
								tileSize.getHeight())) {
					continue;
				}
				
				TPixel addable = this.dataToTPixelixelMapper.map(dataTPixeloint);
				TPixel currentValue = super.pixelAccess.get(relativeCoordinates, tile, tileSize);
				
				TPixel sum;
				if (currentValue == null) {
					sum = addable;
				} else {
					sum = this.adder.add(currentValue, addable);
				}
				
				super.pixelAccess.set(sum, relativeCoordinates, tile, tileSize);
			}
		}
	}
}
