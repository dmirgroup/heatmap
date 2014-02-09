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
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Wraps a mapper and returns a list with the element wrapped to.
 * A utility mapper when working with to-{@link List} mappers.
 * 
 * @author Martin Becker
 *
 * @param <TSource> type of object to map from
 * @param <TListElement> type oof object to map to
 */
public class ProxyToListMapper<TSource, TListElement> 
implements IMapper<TSource, List<TListElement>> {
	
	private IMapper<TSource, TListElement> mapper;
	
	@Getter
	@Setter
	private TListElement defaultValue;

	public ProxyToListMapper(
			IMapper<TSource, TListElement> mapper, 
			TListElement defaultValue) {

		this.mapper = mapper;
		this.defaultValue = defaultValue;
	}
	
	public ProxyToListMapper(IMapper<TSource, TListElement> mapper) {
		this(mapper, null);
	}
	
	@Override
	public List<TListElement> map(TSource object) {
		
		List<TListElement> ids = new ArrayList<TListElement>();
		
		TListElement listElement = this.mapper.map(object);
		if (listElement != null) {
			ids.add(listElement);
		}
		
		if (this.defaultValue != null) {
			ids.add(this.defaultValue);
		}
		
		return ids;
	}
	
}
