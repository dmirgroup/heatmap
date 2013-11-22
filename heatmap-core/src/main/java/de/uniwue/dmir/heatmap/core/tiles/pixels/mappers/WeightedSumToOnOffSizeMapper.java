package de.uniwue.dmir.heatmap.core.tiles.pixels.mappers;

import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSumPixel;

public class WeightedSumToOnOffSizeMapper implements
IMapper<WeightedSumPixel, Double> {

	@Override
	public Double map(WeightedSumPixel object) {
		if (object != null && object.getSize() > 0) {
			return 1.;
		} else {
			return 0.;
		}
	}
}
