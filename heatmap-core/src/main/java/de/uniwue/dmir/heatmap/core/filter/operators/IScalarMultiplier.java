package de.uniwue.dmir.heatmap.core.filter.operators;

/**
 * Interface useful for filters using scalar multiplication on
 * tile pixel data.
 * 
 * @author Martin Becker
 *
 * @param <P> type of object to modify
 */
public interface IScalarMultiplier<P> {
	void multiply(P object, double multiplicator);
}
