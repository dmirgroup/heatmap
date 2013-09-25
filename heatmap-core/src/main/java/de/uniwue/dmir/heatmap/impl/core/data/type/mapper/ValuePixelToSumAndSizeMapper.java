package de.uniwue.dmir.heatmap.impl.core.data.type.mapper;

import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class ValuePixelToSumAndSizeMapper
implements IMapper<ValuePixel, SumAndSize> {
	
	public SumAndSize map(ValuePixel object) {
		return new SumAndSize(object.getValue(), 1);
	}
}