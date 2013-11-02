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
