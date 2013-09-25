package de.uniwue.dmir.heatmap.core.filter.operators;


/**
 * Useful for generic filters to map the given data point to 
 * tile pixel data.
 * 
 * @author Martin Becker
 *
 * @param <T> type to map from
 * @param <P> type to map to
 */
public interface IMapper<T, P> {
	P map(T object); 
}