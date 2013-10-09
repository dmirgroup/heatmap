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
package de.uniwue.dmir.heatmap.impl.core.data.type.internal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WeightedSum extends WeightedSize {

	protected double sumOfValues;
	protected double sumOfWeightedValues;
	
	public WeightedSum(double value) {
		this(value, 1);
	}
	
	public WeightedSum(double value, double weight) {
		this(
				1, 
				value, 
				value * weight, 
				weight);
	}
	
	public WeightedSum(
			double size,
			double sumOfValues,
			double sumOfWeightedValues,
			double sumOfWeights) {
		
		super(size, sumOfWeights);
		this.sumOfValues = sumOfValues;
		this.sumOfWeightedValues = sumOfWeightedValues;
	}
	
	
	/**
	 * Multiplies the sum of weights and the weighted sum with the given
	 * scaling factor. 
	 * 
	 * <p>Useful for weighting later when initializing this class 
	 * with weight = 1;</p>
	 * 
	 * @param scalingFactor the scaling factor to multiply with
	 */
	public void scaleWeights(double scalingFactor) {
		super.scaleWeights(scalingFactor);
		this.sumOfWeightedValues *= scalingFactor;
	}
	
}