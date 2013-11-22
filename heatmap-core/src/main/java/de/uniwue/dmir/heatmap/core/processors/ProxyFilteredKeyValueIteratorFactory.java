/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.core.processors;

import de.uniwue.dmir.heatmap.core.util.IKeyValueIteratorFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProxyFilteredKeyValueIteratorFactory <TSource, TKey, TValue>
implements IKeyValueIteratorFactory <TSource, TKey, TValue>{

	private IKeyValueIteratorFactory<TSource, TKey, TValue> factory;
	private IKeyValueFilter<TKey, TValue> filter;
	
	@Override
	public IKeyValueIterator<TKey, TValue> instance(TSource source) {
		IKeyValueIterator<TKey, TValue> iterator = this.factory.instance(source);
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
				
				if (!ProxyFilteredKeyValueIteratorFactory.this.filter.isToBeDisregarded(
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
