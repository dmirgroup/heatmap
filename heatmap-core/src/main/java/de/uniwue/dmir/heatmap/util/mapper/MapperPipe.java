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
package de.uniwue.dmir.heatmap.util.mapper;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * A mapper pipe will utilizes a list of mappers. 
 * The first mapper will be applied to the given value.
 * If the resulting object is <code>null</code> the next mapper is applied
 * and so on.
 * 
 * @author Martin Becker
 *
 * @param <TKey>
 * @param <TValue>
 */
@AllArgsConstructor
public class MapperPipe<TKey, TValue> 
implements IMapper<TKey, TValue> {

	private List<IMapper<TKey, TValue>> mappers;
	private boolean first;
	
	public MapperPipe(List<IMapper<TKey, TValue>> mappers) {
		this(mappers, true);
	}
	
	@Override
	public <TDerived extends TKey> TValue map(TDerived object) {
		
		TValue result = null;
		
		for (IMapper<TKey, TValue> mapper : this.mappers) {
			
			if (result == null) {
				result = mapper.map(object);
			} else {
				if (!this.first) {
					TValue value = mapper.map(object);
					if (value != null) {
						result = value;
					}
				}
			}
		}
		
		return result;
	}

}
