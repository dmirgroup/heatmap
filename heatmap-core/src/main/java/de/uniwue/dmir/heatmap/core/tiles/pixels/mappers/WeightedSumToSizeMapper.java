package de.uniwue.dmir.heatmap.core.tiles.pixels.mappers;

import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSumPixel;

public class WeightedSumToSizeMapper implements
IMapper<WeightedSumPixel, Double> {

	@Override
	public Double map(WeightedSumPixel object) {
		return object.getSize();
	}
}
