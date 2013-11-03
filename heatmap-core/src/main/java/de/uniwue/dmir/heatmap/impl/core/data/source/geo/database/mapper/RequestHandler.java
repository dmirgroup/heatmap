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
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestSettingsUser;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestSettingsValue;

public final class RequestHandler {

	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String VALUE = "value";
	public static final String TIMESTAMP = "timestamp";
	public static final String GROUP = "'group'";
	
	public String sql(RequestGeo request) {
		SQL sql = new SQL();
		basic(request, sql);
		time(request, sql);
		geo(request, sql);
		value(request, sql);
		group(request, sql);
		return sql.toString();
	}
	
	public void basic(RequestSettingsBasic request, SQL sql) {
		sql
			.SELECT(request.getLongitudeAttribute() + " AS " + LONGITUDE)
			.SELECT(request.getLatitudeAttribute() + " AS " + LATITUDE)
			.FROM(request.getTable());
	}
	
	public void time(RequestSettingsTime request, SQL sql) {
		if (request.getTimestampAttribute() != null) {

			sql.SELECT(request.getTimestampAttribute() + " AS " + TIMESTAMP);
			
			if (request.getMinimumTimestamp() != null) {
				sql.WHERE(
						request.getTimestampAttribute() 
						+ " > #{minimumTimestamp}");
			}
			
			if (request.getMaximumTimestamp() != null) {
				sql.WHERE(
						request.getTimestampAttribute() 
						+ " <= #{maximumTimestamp}");
			}
			
			if (request.getOrderAsc() != null) {
				if (request.getOrderAsc()) {
					sql.ORDER_BY(request.getTimestampAttribute() + " ASC");
				} else {
					sql.ORDER_BY(request.getTimestampAttribute() + " DESC");
				}
			}
		}
	}

	public void value(RequestSettingsValue request, SQL sql) {
		if (request.getValueAttribute() != null) {
			sql.SELECT(request.getValueAttribute() + " AS " + VALUE);
		} else {
			sql.SELECT(request.getDefaultValue() + " AS " + VALUE);
		}
	}
	
	public void group(RequestSettingsUser request, SQL sql) {
		if (request.getGroupAttribute() != null) {
			sql.SELECT(request.getGroupAttribute() + " AS " + GROUP);
		} else {
			sql.SELECT(request.getDefaultUser() + " AS " + GROUP);
		}
	}

	public void geo(RequestGeo request, SQL sql) {
		
		sql.WHERE(request.getLongitudeAttribute() 
				+ " BETWEEN #{lonWest} AND #{lonEast}");
		
		sql.WHERE(request.getLatitudeAttribute() 
				+ " BETWEEN #{latSouth} AND #{latNorth}");
	}
}