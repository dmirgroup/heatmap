package de.uniwue.dmir.heatmap.core.processing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilteredProxyKeyValueIteratorFactory <TSource, TKey, TValue>
implements IKeyValueIteratorFactory <TSource, TKey, TValue>{

	private IKeyValueIteratorFactory<TSource, TKey, TValue> factory;
	private IKeyValueFilter<TKey, TValue> filter;
	
	@Override
	public IKeyValueIterator<TKey, TValue> iterator(TSource source) {
		IKeyValueIterator<TKey, TValue> iterator = this.factory.iterator(source);
		return new FilteredKeyValueIterator(iterator);
	}
	
	public static interface IKeyValueFilter<TKey, TValue> {
		boolean isToBeDisregarded(TKey key, TValue value);
	}
	
	public static interface IObjectFilter<T> {
		boolean isToBeDisregarded(T object);
	}
	
	@AllArgsConstructor
	public static class CombinedKeyValueFilter<TKey, TValue>
	implements IKeyValueFilter<TKey, TValue> {
		
		private boolean and;
		private IObjectFilter<TKey> keyFilter;
		private IObjectFilter<TValue> valueFilter;
		
		public CombinedKeyValueFilter(IObjectFilter<TKey> keyFilter) {
			this(false, keyFilter, null);
		}
		
		@Override
		public boolean isToBeDisregarded(TKey key, TValue value) {
			
			boolean keyDisregarded = false;
			if (this.keyFilter != null) {
				keyDisregarded = this.keyFilter.isToBeDisregarded(key);
			}
			
			boolean valueDisregarded = false;
			if (this.valueFilter != null) {
				valueDisregarded = this.valueFilter.isToBeDisregarded(value);
			}
			
			if (this.and) {
				return keyDisregarded && valueDisregarded;
			} else {
				return keyDisregarded || valueDisregarded;
			}
		}
	}
	
	public class FilteredKeyValueIterator
	implements IKeyValueIterator<TKey, TValue> {

		public FilteredKeyValueIterator(
				IKeyValueIterator<TKey, TValue> iterator) {
			this.iterator = iterator;
		}
		
		private IKeyValueIterator<TKey, TValue> iterator;
		
		private TKey tmpKey;
		private TValue tmpValue;

		private TKey currentKey;
		private TValue currentValue;
		
		private Boolean hasNext;
		
		
		@Override
		public boolean hasNext() {
			
			if (this.hasNext != null) {
				return this.hasNext;
			}
			
			this.tmpKey = null;
			this.tmpValue = null;
			
			while (this.iterator.hasNext()) {
				
				this.iterator.next();
				
				this.tmpKey = this.iterator.getKey();
				this.tmpValue = this.iterator.getValue();
				
				if (!FilteredProxyKeyValueIteratorFactory.this.filter.isToBeDisregarded(
						this.tmpKey, 
						this.tmpValue)) {
					
					this.hasNext = true;
					return true;
				}
			}
			
			this.hasNext = false;
			return false;
		}

		@Override
		public void next() {
			
			if (!this.hasNext()) {
				throw new IllegalStateException("No next value.");
			}
			
			this.hasNext = null;
			this.currentKey = this.tmpKey;
			this.currentValue = this.tmpValue;
			
		}

		@Override
		public TKey getKey() {
			return this.currentKey;
		}

		@Override
		public TValue getValue() {
			return this.currentValue;
		}
		
	}

}
