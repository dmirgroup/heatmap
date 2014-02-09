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
package de.uniwue.dmir.heatmap.filters.groupaccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.point.types.IDatePoint;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

public class DatePointToGroupIdMapper 
implements IMapper<IDatePoint, List<String>> {

	@Getter
	@Setter
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<String> map(IDatePoint object) {
		
		List<String> ids = new ArrayList<String>();
		if (object.getDate() != null) {
			String date = this.dateFormat.format(object.getDate());
			ids.add(date);
		}
		
		return ids;
	}
	
}
