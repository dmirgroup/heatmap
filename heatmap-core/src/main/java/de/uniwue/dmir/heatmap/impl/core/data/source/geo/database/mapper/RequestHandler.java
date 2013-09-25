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