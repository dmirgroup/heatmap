package de.uniwue.dmir.heatmap.impl.core.visualizer.colors;

import java.awt.Color;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.processing.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.visualizer.IColorScheme;

@AllArgsConstructor
public class SimpleColorPipe<T> implements IColorPipe<T> {
	
	private IToDoubleMapper<T> toDoubleMapper;
	private IColorScheme colorScheme;
	
	@Override
	public Color getColor(T object) {
		double colorValue = this.toDoubleMapper.map(object);
		return new Color(this.colorScheme.getColor(colorValue));
	}
}
