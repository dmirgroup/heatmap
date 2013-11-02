package de.uniwue.dmir.heatmap.impl.core.visualizer.colors;

import de.uniwue.dmir.heatmap.core.processing.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.visualizer.IAlphaScheme;

public class SimpleAlphaPipe<T> implements IAlphaPipe<T> {
	
	private IToDoubleMapper<T> toDoubleMapper;
	private IAlphaScheme alphaScheme;
	
	@Override
	public int getAlpha(T object) {
		double alphaValue = this.toDoubleMapper.map(object);
		return this.alphaScheme.getColor(alphaValue);
	}
}
