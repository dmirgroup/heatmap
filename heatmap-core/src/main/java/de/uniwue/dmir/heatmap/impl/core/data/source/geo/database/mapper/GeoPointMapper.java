package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import de.uniwue.dmir.heatmap.impl.core.data.source.geo.GeoPoint;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.database.RequestSettingsBasic;

public interface GeoPointMapper {

	@SelectProvider(
			type = RequestHandler.class,
			method = "sql")
	@Results({
			@Result(column = "longitude", property = "geoCoordinates.longitude"),
			@Result(column = "latitude", property = "geoCoordinates.latitude")})
	public List<GeoPoint> getData(RequestSettingsBasic request);
	
}
