package de.uniwue.dmir.heatmap.impl.core.visualizer.colors;

import java.awt.Color;

public interface IColorPipe<T> {
	Color getColor(T object);
}
