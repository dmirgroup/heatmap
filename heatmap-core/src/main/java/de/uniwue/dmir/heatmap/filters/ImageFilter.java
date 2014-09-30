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

import java.awt.image.BufferedImage;

import lombok.Getter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.filters.operators.IScalarMultiplier;
import de.uniwue.dmir.heatmap.filters.pixelaccess.IPixelAccess;
import de.uniwue.dmir.heatmap.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.Arrays2d;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

@Getter
public class ImageFilter<TPoint, TPixel, TTile> 
extends AbstractPixelAccessFilter<TPoint, TPixel, TTile> {

	/** Maps external data, e.g., from the data base to internal data, i.e., tile data. */
	private IMapper<TPoint, TPixel> dataToPixelMapper;
	
	/** Defines how to add up internal data. */
	private IAdder<TPixel> adder;
	
	/** Defines how to multiply internal data with a scalar value. */
	private IScalarMultiplier<TPixel> multiplier;
	
	/** 
	 * Usually each pixel is initialized by multiplying the red value of 
	 * the given filter image with the internal data.
	 * If this boolean variable is <code>true</code>, this initialization is 
	 * skipped if the alpha value of the pixel is <code>0</code>.
	 */
	private boolean useAlpha;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private Double[] array;

	public ImageFilter(
			IToRelativeCoordinatesMapper<TPoint> dataToRelativeCoordinatesMapper,
			IMapper<TPoint, TPixel> dataToPixelMapper,
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IAdder<TPixel> adder,
			IScalarMultiplier<TPixel> multiplier,
			BufferedImage image) {
		
		this(dataToRelativeCoordinatesMapper, dataToPixelMapper, pixelAccess, adder, multiplier, false);
	}
	
	public ImageFilter(
			IToRelativeCoordinatesMapper<TPoint> tdataTRelativeCoordinatesMapper,
			IMapper<TPoint, TPixel> dataToPixelMapper,
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IAdder<TPixel> adder,
			IScalarMultiplier<TPixel> multiplier,
			BufferedImage image,
			boolean useAlpha) {
		
		this(tdataTRelativeCoordinatesMapper, dataToPixelMapper, pixelAccess, adder, multiplier, useAlpha);
		initialize(image);
	}
	
	private ImageFilter(
			IToRelativeCoordinatesMapper<TPoint> dataToRelativeCoordinatesMapper,
			IMapper<TPoint, TPixel> dataToPixelMapper,
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IAdder<TPixel> adder,
			IScalarMultiplier<TPixel> multiplier,
			boolean useAlpha) {

		super(dataToRelativeCoordinatesMapper, pixelAccess);

		this.dataToPixelMapper = dataToPixelMapper;
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
	}
	
	public <TDerived extends TPoint> void filter(
			TDerived dataTPixeloint, 
			RelativeCoordinates relativeCoordinates,
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		int startX = relativeCoordinates.getX();
		int startY = relativeCoordinates.getY();
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
				
				TPixel addable = this.dataToPixelMapper.map(dataTPixeloint);
				
				this.multiplier.multiply(addable, multiplicator);
				
				TPixel currentValue = this.pixelAccess.get(
						relativeCoordinates,
						tile, 
						tileSize);
				
				TPixel sum;
				if (currentValue == null) {
					sum = addable;
				} else {
					sum = this.adder.add(currentValue, addable);
				}
				
				this.pixelAccess.set(
						sum, 
						relativeCoordinates,
						tile, 
						tileSize);
			}
		}
	}
}
