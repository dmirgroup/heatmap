package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.util.Iterator;
import java.util.Map;

import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory;

public class MapKeyValueIteratorFactory<TKey, TValue> 
implements IKeyValueIteratorFactory<Map<TKey, TValue>, TKey, TValue>{

	@Override
	public IKeyValueIterator<TKey, TValue> iterator(final Map<TKey, TValue> source) {
		
		return new IKeyValueIterator<TKey, TValue>() {

			private Iterator<Map.Entry<TKey, TValue>> iterator = 
					source.entrySet().iterator();
			
			private Map.Entry<TKey, TValue> currentEntry = null;
			
			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public void next() {
				this.currentEntry = this.iterator.next();
			}

			@Override
			public TKey getKey() {
				return this.currentEntry.getKey();
			}

			@Override
			public TValue getValue() {
				return this.currentEntry.getValue();
			}
		};
	}

}
