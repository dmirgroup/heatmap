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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Allows to merge the output of to-{@link List} mappers.
 * 
 * @author Martin Becker
 *
 * @param <TSource>
 * @param <TListElement>
 */
public class ProxyElementToUnionListMapper<TSource, TListElement>
implements IMapper<TSource, List<TListElement>> {

	private List<IMapper<TSource, TListElement>> mappers;
	
	@Getter
	@Setter
	private boolean filterNullValues;
	
	@Getter
	@Setter
	private boolean addNullIfNullWasFound;
	
	public ProxyElementToUnionListMapper(
			List<IMapper<TSource, TListElement>> mappers) {
		
		this.mappers = mappers;
		this.filterNullValues = true;
		this.addNullIfNullWasFound = true;
	}
	
	@SafeVarargs
	public ProxyElementToUnionListMapper(
			IMapper<TSource, TListElement> ... mappers) {
		
		this(Arrays.asList(mappers));
	}
	
	@SuppressWarnings("serial")
	public ProxyElementToUnionListMapper(
			final IMapper<TSource, TListElement> mapper) {
		
		this(new ArrayList<IMapper<TSource, TListElement>>(){{
			add(mapper);
		}});
	}
	
	@Override
	public List<TListElement> map(TSource object) {
		
		List<TListElement> union = new ArrayList<TListElement>();

		boolean nullWasFound = false;
		for (IMapper<TSource, TListElement> m : this.mappers) {
			
			TListElement element = m.map(object);
			
			// filter nulls
			if (this.filterNullValues && element == null) {
				nullWasFound = true;
			} else {
				union.add(element);
			}
			
		}
		
		if (this.addNullIfNullWasFound && nullWasFound) {
			union.add(null);
		}
		
		return union;
	}

}
