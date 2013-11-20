package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.ReferencedData;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.distances.EuclidianDistance;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.rbfs.GaussianRbf;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.util.IAggregator;

@AllArgsConstructor
public abstract class AbstractGenericRbfAggregator<TData>
implements IAggregator<ReferencedData<TData>, Double> {

	private IToDoubleMapper<TData> pixelToValueMapper;
	private IDistanceFunction<RelativeCoordinates> distanceFunction;
	private IRadialBasisFunction radialBasisFunction;
	
	public AbstractGenericRbfAggregator(
			IToDoubleMapper<TData> pixelToValueMapper,
			double pointRadius) {
		
		this.pixelToValueMapper = pixelToValueMapper;
		this.distanceFunction = new EuclidianDistance();
		this.radialBasisFunction = new GaussianRbf(pointRadius);
	}
	
	@Override
	public void addData(ReferencedData<TData> dataContainer) {
		double distance = this.distanceFunction.distance(
				dataContainer.getReferenceCoordaintes(), 
				dataContainer.getDataCoordinates());
		double distanceWeight = this.radialBasisFunction.value(distance);
		double value = this.pixelToValueMapper.map(dataContainer.getData());
		this.addData(value, distanceWeight);
	}
	
	protected abstract void addData(double value, double distanceWeight);
}