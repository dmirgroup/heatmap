package de.uniwue.dmir.heatmap.impl.core.visualizer.rbf;

import de.uniwue.dmir.heatmap.impl.core.visualizer.RbfVisualizer.IRadialBasisFunction;

public class GaussianRdf implements IRadialBasisFunction {

	public static final double EPSILON = 0.5;
	
	private double epsilon;
	
	public GaussianRdf(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public GaussianRdf() {
		this(EPSILON);
	}
	
	@Override
	public double value(double value) {
		
		double weightedValue = value * this.epsilon;
		
		return Math.exp(-weightedValue * weightedValue);
	}

}
