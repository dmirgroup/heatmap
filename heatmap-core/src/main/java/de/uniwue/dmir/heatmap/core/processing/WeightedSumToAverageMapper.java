package de.uniwue.dmir.heatmap.core.processing;

import de.uniwue.dmir.heatmap.impl.core.data.type.internal.WeightedSum;

public class WeightedSumToAverageMapper 
implements IToDoubleMapper<WeightedSum> {

	@Override
	public Double map(WeightedSum object) {
		return object.getSumOfValues() / object.getSize();
	}

	
}
