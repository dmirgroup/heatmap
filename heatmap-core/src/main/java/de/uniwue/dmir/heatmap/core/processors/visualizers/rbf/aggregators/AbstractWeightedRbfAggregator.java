package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators;

import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;

public abstract class AbstractWeightedRbfAggregator<TData> 
extends AbstractGenericRbfAggregator<TData> {

	public AbstractWeightedRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			IDistanceFunction<RelativeCoordinates> distanceFunction,
			IRadialBasisFunction radialBasisFunction) {
		super(pixelToValueMapper, distanceFunction, radialBasisFunction);
	}
	
	public AbstractWeightedRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			double pointRadius) {
		super(pixelToValueMapper, pointRadius);
	}

	protected double sumOfWeightedValues = 0;
	protected double sumOfWeights = 0;
	
	@Override
	public void reset() {
		this.sumOfWeightedValues = 0;
		this.sumOfWeights = 0;
	}
	
}