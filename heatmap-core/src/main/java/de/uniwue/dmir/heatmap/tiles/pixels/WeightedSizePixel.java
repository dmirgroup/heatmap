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
package de.uniwue.dmir.heatmap.tiles.pixels;

import lombok.Data;

@Data
public class WeightedSizePixel {
	
	protected double size;
	protected double sumOfWeights;
	
	public WeightedSizePixel() {
		this(1);
	}
	
	public WeightedSizePixel(double size) {
		this(size, 1);
	}
	
	public WeightedSizePixel(double size, double sumOfWeights) {
		super();
		this.size = size;
		this.sumOfWeights = sumOfWeights;
	}
	
	
	/**
	 * Multiplies the sum of weights with the given scaling factor. 
	 * 
	 * <p>Useful for weighting later when initializing this class 
	 * with weight = 1;</p>
	 * 
	 * @param scalingFactor the scaling factor to multiply with
	 */
	public void scaleWeights(double scalingFactor) {
		this.sumOfWeights *= scalingFactor;
	}
}
