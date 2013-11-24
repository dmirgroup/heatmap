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
package de.uniwue.dmir.heatmap.processors.pixeliterators;

import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.util.Arrays2d;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory;

@AllArgsConstructor
public class RelativeCoordinatesArrayKeyValueIteratorFactory<TValue> 
implements IKeyValueIteratorFactory<TValue[], RelativeCoordinates, TValue>{

	private int width;
	private int height;
	private boolean returnNullValues;
	
	@Override
	public IKeyValueIterator<RelativeCoordinates, TValue> instance(final TValue[] source) {
		
		return new IKeyValueIterator<RelativeCoordinates, TValue>() {

			private int index = 0;

			private RelativeCoordinates currentRelativeCoordinates = null;
			private TValue currentValue = null;

			private RelativeCoordinates tmpRelativeCoordinates = null;
			private TValue tmpValue = null;
			
			@Override
			public boolean hasNext() {
				
				if (this.tmpRelativeCoordinates != null) {
					
					return true;
					
				} else if (this.index >= source.length) {
					
					return false;
					
				} else {

					this.tmpValue = source[this.index];
					
					if (
							!RelativeCoordinatesArrayKeyValueIteratorFactory.this.returnNullValues
							&& this.tmpValue == null) {
						
						this.index ++;
						return hasNext();
						
					} else {
						
						int x = Arrays2d.x(
								this.index, 
								RelativeCoordinatesArrayKeyValueIteratorFactory.this.width, 
								RelativeCoordinatesArrayKeyValueIteratorFactory.this.height);
						
						int y = Arrays2d.y(
								this.index, 
								RelativeCoordinatesArrayKeyValueIteratorFactory.this.width, 
								RelativeCoordinatesArrayKeyValueIteratorFactory.this.height);
						
						this.tmpRelativeCoordinates = new RelativeCoordinates(x, y);
						
						this.index ++;
						return true;
					}
				}
			}

			@Override
			public void next() {
				if (this.hasNext()) {
					this.currentRelativeCoordinates = this.tmpRelativeCoordinates;
					this.currentValue = this.tmpValue;
					this.tmpRelativeCoordinates = null;
					this.tmpValue = null;
				} else {
					throw new NoSuchElementException();
				}
				
			}

			@Override
			public RelativeCoordinates getKey() {
				return this.currentRelativeCoordinates;
			}

			@Override
			public TValue getValue() {
				return this.currentValue;
			}
		};
	}

}
