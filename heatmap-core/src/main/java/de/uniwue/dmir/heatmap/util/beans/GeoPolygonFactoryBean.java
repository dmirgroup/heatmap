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
package de.uniwue.dmir.heatmap.util.beans;

import java.io.InputStream;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import de.uniwue.dmir.heatmap.util.GeoPolygon;

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
