package de.uniwue.dmir.heatmap.core.filter.operators;

/**
 * Interface useful for filters adding up tile pixel data.
 * 
 * @author Martin Becker
 *
 * @param <S> type to sum
 */
public interface IAdder<S> {
	S add(S o1, S o2);
}