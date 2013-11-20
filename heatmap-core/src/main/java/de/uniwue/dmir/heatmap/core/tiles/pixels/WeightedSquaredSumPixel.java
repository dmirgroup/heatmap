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
package de.uniwue.dmir.heatmap.core.tiles.pixels;

import de.uniwue.dmir.heatmap.core.filters.operators.WeightedSquaredSumAdder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WeightedSquaredSumPixel extends WeightedSumPixel {

	protected double sumOfSquaredValues;
	protected double sumOfWeightedSquaredValues;
	
	public WeightedSquaredSumPixel(double value) {
		this(value, 1);
	}
	
	public WeightedSquaredSumPixel(double value, double weight) {
		this(
				1, 
				value, 
				value * value, 
				value * weight, 
				value * value * weight, 
				weight);
	}
	
	public WeightedSquaredSumPixel(
			
			double size,
			
			double sumOfValues,
			double sumOfSquaredValues,
			
			double sumOfWeightedValues,
			double sumOfWeightedSquaredValues,
			
			double sumOfWeights) {
		
		super(
				size, 
				sumOfValues, 
				sumOfWeightedValues,
				sumOfWeights);

		this.sumOfSquaredValues = sumOfSquaredValues;
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
	
	public static void main(String[] args) {
		
		WeightedSquaredSumPixel sum = new WeightedSquaredSumPixel(2);
		System.out.println(sum);
		sum.scaleWeights(0.5);
		System.out.println(sum);

		WeightedSquaredSumAdder adder = new WeightedSquaredSumAdder();
		
		WeightedSquaredSumPixel sum2 = new WeightedSquaredSumPixel(4);
		sum2.scaleWeights(1. / 3);
		System.out.println(sum2);
		
		WeightedSquaredSumPixel sum3 = new WeightedSquaredSumPixel(1);
		sum3.scaleWeights(2. / 3);
		System.out.println(sum3);

		WeightedSquaredSumPixel sum4 = adder.add(sum2, sum3);
		System.out.println(sum4);
		
		System.out.println(sum4.getSumOfWeightedValues() / sum4.getSumOfWeights());
	}
}