package de.uniwue.dmir.heatmap.impl.core.filter.operators;

import de.uniwue.dmir.heatmap.core.filter.operators.IScalarMultiplier;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class SumAndSizeScalarMultiplier
implements IScalarMultiplier<SumAndSize> {

	public void multiply(SumAndSize object, double multiplicator) {
		double value = object.getSum();
		value *= multiplicator;
		object.setSum(value);
	}

}
