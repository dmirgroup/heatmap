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
package de.uniwue.dmir.heatmap.core.filters.access.mappers;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

public abstract class AbstractGroupIdMapper<T> 
implements IMapper<T, List<String>> {

	public static final String DEFAULT_OVERALL_GROUP_ID = "OVERALL";
	
	@Getter
	@Setter
	private String overallGroupId;
	
	protected abstract void add(T object, List<String> ids);
	
	@Override
	public List<String> map(T object) {
		
		List<String> ids = new ArrayList<String>();
		if (this.overallGroupId != null) {
			ids.add(this.overallGroupId);
		}
		this.add(object, ids);
		
		return ids;
	}

}
