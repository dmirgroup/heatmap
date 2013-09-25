package de.uniwue.dmir.heatmap.impl.core.filter.operators;

import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;


public class SumAndSizeAdder
implements IAdder<SumAndSize> {

	public SumAndSize add(SumAndSize o1, SumAndSize o2) {
		
		return new SumAndSize(
				o1.getSum() + o2.getSum(),
				o1.getSize() + o2.getSize());
	}
	
}