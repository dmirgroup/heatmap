package de.uniwue.dmir.heatmap.core.util;

import java.awt.Polygon;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;

import de.uniwue.dmir.heatmap.core.data.source.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.processing.PolygonRelativeCoordinatesFilter;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class PolygonFromGeoPolygonFacoryBean 
implements FactoryBean<Polygon> {

	private GeoPolygon geoPolygon;
	private TileCoordinates tileCoordinates;
	private IMapProjection mapProjection;
	
	@Override
	public Polygon getObject() throws Exception {
		return PolygonRelativeCoordinatesFilter.fromGeoPolygon(
				this.geoPolygon, 
				this.tileCoordinates, 
				this.mapProjection);
	}

	@Override
	public Class<?> getObjectType() {
		return Polygon.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
