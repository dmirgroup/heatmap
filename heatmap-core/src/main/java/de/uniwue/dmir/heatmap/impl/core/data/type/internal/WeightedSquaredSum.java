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

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WeightedSquaredSum extends WeightedSum {

	protected double sumOfSquaredValues;
	protected double sumOfWeightedSquaredValues;
	
	public WeightedSquaredSum(double value) {
		this();
	}
	
	public WeightedSquaredSum(double value, double weight) {
		this(1, value, value * value, value * weight, value * value * weight, weight);
	}
	
	public WeightedSquaredSum(
			
			double size, 
			
			double sumOfValues,
			double sumOfsquaredValues,
			
			double sumOfWeightedValues,
			double sumOfWeightedSquaredValues,
			
			double sumOfWeights) {
		
		super(size, sumOfValues, sumOfWeightedValues, sumOfWeights);
		this.sumOfSquaredValues = sumOfsquaredValues;
		this.sumOfWeightedSquaredValues = sumOfWeightedSquaredValues;
	}
	
	/**
	 * Multiplies the sum of weights, the weighted sum and 
	 * the weighted squared sum with the given weight. 
	 * 
	 * <p>Useful for weighting later when initializing this class 
	 * with weight = 1;</p>
	 * 
	 * @param scalingFactor the scaling factor to multiply with
	 */
	public void scaleWeights(double scalingFactor) {
		super.scaleWeights(scalingFactor);
		this.sumOfWeightedSquaredValues *= scalingFactor;
	}
}