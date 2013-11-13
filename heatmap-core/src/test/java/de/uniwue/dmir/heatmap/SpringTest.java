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
package de.uniwue.dmir.heatmap;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IGeoDatasource;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.GeoPoint;

public class SpringTest {

	@Test
	public void testHeatmap() throws IOException {
		
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(
						"spring/applicationContext.xml");
		
		@SuppressWarnings("unchecked")
		IGeoDatasource<GeoPoint<String>> geoDataSource = 
				appContext.getBean(IGeoDatasource.class);
		
		List<GeoPoint<String>> points = geoDataSource.getData(
				new GeoBoundingBox(
						new GeoCoordinates(10, 10),
						new GeoCoordinates(100, 100)));

		for (int i = 0; i < 10; i++) {
			System.out.println(points.get(i));
		}
		
		appContext.close();
		
	}
}
