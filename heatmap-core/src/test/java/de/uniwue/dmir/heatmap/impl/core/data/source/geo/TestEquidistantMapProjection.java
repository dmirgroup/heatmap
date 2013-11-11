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
package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.projections.EquidistantProjection;
import de.uniwue.dmir.heatmap.core.data.types.ValuePixel;
import de.uniwue.dmir.heatmap.core.filters.AbstractConfigurableFilter;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

public class TestEquidistantMapProjection {
	
	@Test
	public void testRelativeCoordinates() {
		
		double offset = 5;
		
		EquidistantProjection equidistantProjection = new EquidistantProjection(
				new GeoBoundingBox(
						new GeoCoordinates(0 + offset, 0 + offset), 
						new GeoCoordinates(10 + offset, 10 + offset)), 
				new TileSize(10, 10));
		
		RelativeCoordinates r1 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0.5 + offset, 9.5 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 0), r1);
		
		RelativeCoordinates r2 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(-0.5 + offset, 10.5 + offset), 
						null);

		Assert.assertEquals(new RelativeCoordinates(-1, -1), r2);
		
		RelativeCoordinates r3 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0 + offset, 0.5 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 9), r3);
		
		// points on the south east border are not considered as part of the tile
		
		RelativeCoordinates r4 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0 + offset, 0 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 10), r4);
		
	}
	
	@Test
	public void test() {

		double offset = 5;
		
		EquidistantProjection equidistantProjection = new EquidistantProjection(
				new GeoBoundingBox(
						new GeoCoordinates(0 + offset, 0 + offset), 
						new GeoCoordinates(10 + offset, 10 + offset)), 
				new TileSize(10, 10));
		
		IFilter<?, ?> filter = new AbstractConfigurableFilter<ValuePixel, Object>() {
			@Override
			public void filter(
					ValuePixel dataPoint, 
					Object tile,
					TileSize tileSize,
					TileCoordinates tileCoordinates) {
			}
		};
		
		List<TileCoordinates> tileCoordinates = 
				equidistantProjection.overlappingTiles(
						new GeoCoordinates(offset, offset + .5), 
						0, 
						filter);

		Assert.assertEquals(tileCoordinates.size(), 1);
		
		List<TileCoordinates> tileCoordinates2 = 
				equidistantProjection.overlappingTiles(
						new GeoCoordinates(offset, offset), 
						0, 
						filter);

		Assert.assertEquals(tileCoordinates2.size(), 0);
	}
}
