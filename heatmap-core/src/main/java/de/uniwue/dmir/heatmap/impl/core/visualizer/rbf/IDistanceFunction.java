package de.uniwue.dmir.heatmap.impl.core.visualizer.rbf;

public interface IDistanceFunction<T> {
	double distance(T o1, T o2);
}