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
package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.mapper;

import org.apache.ibatis.jdbc.SQL;

import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestGeo;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestSettingsBasic;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestSettingsTime;

public final class RequestHandler {
	
	public String sql(RequestGeo request) {
		SQL sql = new SQL();
		basic(request, sql);
		time(request, sql);
		geo(request, sql);
		return sql.toString();
	}
	
	public void basic(RequestSettingsBasic request, SQL sql) {
		sql
			.SELECT(request.getLongitudeAttribute() + " AS longitude")
			.SELECT(request.getLatitudeAttribute() + " AS latitude")
			.FROM(request.getTable());
	}
	
	public void time(RequestSettingsTime request, SQL sql) {
		if (request.getTimestampAttribute() != null) {
			if (request.getMinimumTimestamp() != null) {
				sql.WHERE(
						request.getTimestampAttribute() 
						+ " > #{minimumTimestamp}");
			}
			
			if (request.getMaximumTimestamp() != null)
				sql.WHERE(
						request.getTimestampAttribute() 
						+ " <= #{maximumTimestamp}");
		}
	}
	
	public void geo(RequestGeo request, SQL sql) {
		
		sql.WHERE(request.getLongitudeAttribute() 
				+ " BETWEEN #{lonWest} AND #{lonEast}");
		
		sql.WHERE(request.getLatitudeAttribute() 
				+ " BETWEEN #{latSouth} AND #{latNorth}");
	}
}