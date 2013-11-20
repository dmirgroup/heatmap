package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators;

import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;

public class QuadraticRbfAggregator<TData> 
extends AbstractWeightedRbfAggregator<TData> {

	public QuadraticRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			IDistanceFunction<RelativeCoordinates> distanceFunction,
			IRadialBasisFunction radialBasisFunction) {
		super(pixelToValueMapper, distanceFunction, radialBasisFunction);
	}
	
	public QuadraticRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			double pointRadius) {
		super(pixelToValueMapper, pointRadius);
	}

	@Override
	public Double getAggregate() {
		return this.sumOfWeightedValues / this.sumOfWeights;
	}

	@Override
	protected void addData(double value, double distanceWeight) {
		this.sumOfWeightedValues += value * distanceWeight * distanceWeight;
		this.sumOfWeights += distanceWeight;
	}
	
}