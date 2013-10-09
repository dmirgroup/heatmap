package de.uniwue.dmir.heatmap.impl.core.data.type.internal;

import lombok.Data;

@Data
public class WeightedSize {
	
	protected double size;
	protected double sumOfWeights;
	
	public WeightedSize() {
		this(1);
	}
	
	public WeightedSize(double size) {
		this(size, 1);
	}
	
	public WeightedSize(double size, double sumOfWeights) {
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
