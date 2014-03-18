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
package de.uniwue.dmir.heatmap.util.iterator;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.util.iterator.ProxyFilteredKeyValueIteratorFactory.IKeyValueFilter;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * Creates {@link IKeyValueIterator} which returns only instances which 
 * are not filtered by the given {@link IKeyValueFilter}.
 * 
 * @author Martin Becker
 *
 * @param <TSource>
 * @param <TSourceKey>
 * @param <TSourceValue>
 */
@AllArgsConstructor
public class ProxyTransformingKeyValueIteratorFactory <TSource, TSourceKey, TSourceValue, TKey, TValue>
implements IKeyValueIteratorFactory <TSource, TKey, TValue> {

	private IKeyValueIteratorFactory<TSource, TSourceKey, TSourceValue> factory;
	private IMapper<TSourceKey, TKey> keyMapper;
	private IMapper<TSourceValue, TValue> valueMapper;
	
	@Override
	public IKeyValueIterator<TKey, TValue> instance(TSource source) {
		IKeyValueIterator<TSourceKey, TSourceValue> iterator = this.factory.instance(source);
		return new TransformedKeyValueIterator(iterator);
	}
	
	public class TransformedKeyValueIterator
	implements IKeyValueIterator<TKey, TValue> {

		public TransformedKeyValueIterator(
				IKeyValueIterator<TSourceKey, TSourceValue> iterator) {
			this.iterator = iterator;
		}
		
		private IKeyValueIterator<TSourceKey, TSourceValue> iterator;
		
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
				
				TSourceKey sourceKey = this.iterator.getKey();
				TSourceValue sourceValue = this.iterator.getValue();
				
				this.tmpKey = ProxyTransformingKeyValueIteratorFactory.this.keyMapper.map(sourceKey);
				this.tmpValue = ProxyTransformingKeyValueIteratorFactory.this.valueMapper.map(sourceValue);
				
				this.hasNext = true;
				return true;
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
