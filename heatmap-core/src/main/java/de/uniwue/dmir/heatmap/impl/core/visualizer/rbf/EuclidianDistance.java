package de.uniwue.dmir.heatmap.impl.core.visualizer.rbf;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.impl.core.visualizer.RbfVisualizer.IDistanceFunction;

public class EuclidianDistance implements IDistanceFunction<IExternalData> {

	@Override
	public double distance(IExternalData o1, IExternalData o2) {
		
		double diffX = o1.getCoordinates().getX() - o2.getCoordinates().getX();
		double diffY = o1.getCoordinates().getY() - o2.getCoordinates().getY();
		
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}

}
