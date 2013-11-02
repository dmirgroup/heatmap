package de.uniwue.dmir.heatmap.core.processing;

public interface IKeyValueIteratorFactory<TSource, TKey, TValue> {
	
	IKeyValueIterator<TKey, TValue> iterator(TSource source);
	
	interface IKeyValueIterator<TKey, TValue> {

		boolean hasNext();
		void next();
		TKey getKey();
		TValue getValue();
		
	}
}
