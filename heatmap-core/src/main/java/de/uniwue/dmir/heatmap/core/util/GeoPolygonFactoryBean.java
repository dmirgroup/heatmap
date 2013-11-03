package de.uniwue.dmir.heatmap.core.util;

import java.io.InputStream;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

@AllArgsConstructor
public class GeoPolygonFactoryBean 
implements FactoryBean<GeoPolygon> {

	private Resource resource;
	
	@Override
	public GeoPolygon getObject() throws Exception {
		InputStream inputStream = this.resource.getInputStream();
		return GeoPolygon.load(inputStream);
	}

	@Override
	public Class<GeoPolygon> getObjectType() {
		return GeoPolygon.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
