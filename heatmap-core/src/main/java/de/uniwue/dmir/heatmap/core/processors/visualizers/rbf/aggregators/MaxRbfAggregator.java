package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators;

import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;

public class MaxRbfAggregator<TData> 
extends AbstractGenericRbfAggregator<TData> {

	private double max;
	
	public MaxRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			IDistanceFunction<RelativeCoordinates> distanceFunction,
			IRadialBasisFunction radialBasisFunction) {
		super(pixelToValueMapper, distanceFunction, radialBasisFunction);
	}
	
	public MaxRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			double pointRadius) {
		super(pixelToValueMapper, pointRadius);
	}

	@Override
	public Double getAggregate() {
		return this.max;
	}

	@Override
	protected void addData(double value, double distanceWeight) {
		this.max = Math.max(this.max, value * distanceWeight);
	}

	@Override
	public void reset() {
		this.max = 0;
	}
	
}