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

import java.awt.image.BufferedImage;

import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.filter.operators.IScalarMultiplier;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

@Getter
public class ImageFilter<E extends IExternalData, P> 
extends AbstractFilter<E, P[]> {

	private IMapper<E, P> internalMapper;
	private IAdder<P> adder;
	private IScalarMultiplier<P> multiplier;
	
	private boolean useAlpha;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private Double[] array;

	public ImageFilter(
			IMapper<E, P> internalMapper,
			IAdder<P> adder,
			IScalarMultiplier<P> multiplier,
			BufferedImage image) {
		this(internalMapper, adder, multiplier, false);
	}
	
	public ImageFilter(
			IMapper<E, P> internalMapper,
			IAdder<P> adder,
			IScalarMultiplier<P> multiplier,
			BufferedImage image,
			boolean useAlpha) {
		
		this(internalMapper, adder, multiplier, useAlpha);
		initialize(image);
	}
	
	private ImageFilter(
			IMapper<E, P> internalMapper,
			IAdder<P> adder,
			IScalarMultiplier<P> multiplier,
			boolean useAlpha) {
		
		this.internalMapper = internalMapper;
		this.adder = adder;
		this.multiplier = multiplier;
		this.useAlpha = useAlpha;
	}
	
	private void initialize(BufferedImage image) {
		if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
			super.logger.warn(
					"Given image is an ARGB image. "
					+ "Note that only the red and the alpha channel are read.");
		}
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		
		// values and sum
		this.array = new Double[this.width * this.height];
		double max = 0;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j ++) {
				
				boolean isTransparent = (image.getRGB(i, j) & 0xFF000000) == 0x00000000;

				double value;
				if (this.useAlpha && isTransparent) {
					value = Double.NaN;
				} else {
					value = image.getData().getSampleDouble(i, j, 0);
					max = Math.max(max, value);
				}
				
				Arrays2d.set(value, i, j, this.array, this.width, this.height);
			}
		}
		
		// normalize value wise
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j ++) {
				
				double value = Arrays2d.get(i, j, this.array, this.width, this.height);
				value /= max;
				
				Arrays2d.set(value, i, j, this.array, this.width, this.height);
			}
		}
		
		System.out.println(Arrays2d.toString(this.array, width, height));
	}
	
	public void filter(E dataPoint, P[] tile, TileSize tileSize) {
		
		int startX = dataPoint.getCoordinates().getX();
		int startY = dataPoint.getCoordinates().getY();
		startX -= this.centerX;
		startY -= this.centerY;
		
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j ++) {
				
				double multiplicator = 
						Arrays2d.get(i, j, this.array, this.width, this.height);
				if (Double.isNaN(multiplicator)) {
					continue;
				}
				
				int x = startX + i;
				int y = startY + j;

				if (!Arrays2d.isIndexWithinBounds(
						x, 
						y, 
						tileSize.getWidth(), 
						tileSize.getHeight())) {
					continue;
				}
				
				P addable = this.internalMapper.map(dataPoint);
				
				this.multiplier.multiply(addable, multiplicator);
				
				P currentValue = Arrays2d.get(
						x, y, 
						tile, 
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
						tile, 
						tileSize.getWidth(), 
						tileSize.getHeight());
			}
		}
	}
}
