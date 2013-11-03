package de.uniwue.dmir.heatmap.core.processing;

import de.uniwue.dmir.heatmap.impl.core.data.type.internal.PointSize;

public class PointSizeToPointsMapper implements IToDoubleMapper<PointSize> {

	@Override
	public Double map(PointSize object) {
		return object.getPoints();
	}

}
