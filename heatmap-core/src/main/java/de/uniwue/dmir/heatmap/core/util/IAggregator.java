package de.uniwue.dmir.heatmap.core.util;

public interface IAggregator<TData, TAggregate> {
	void reset();
	void addData(TData data);
	TAggregate getAggregate();
}