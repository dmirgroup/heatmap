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
package de.uniwue.dmir.heatmap.filters;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FilterDimensionsTest {
	
	@Test
	public void simpleTestY1() {

		List<IFilter> filters =
			Arrays.asList(new IFilter[] {
					new TestFilter(1, 1, 0, 0),
					new TestFilter(1, 1, 0, 0),
					new TestFilter(1, 1, 0, 0),
			});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(1,1,0,0), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestY2() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 3, 0, 0),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(1,3,0,0), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestY3() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 3, 0, 2),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(1,3,0,2), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestY4() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(1, 2, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 3, 0, 2),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(1,4,0,2), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestX2() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(3, 1, 0, 0),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(3,1,0,0), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestX3() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(1, 1, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(3, 1, 2, 0),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(3,1,2,0), 
				filterDimensions);
	}
	
	@Test
	public void simpleTestX4() {

		List<IFilter> filters = Arrays.asList(new IFilter[] {
				new TestFilter(2, 1, 0, 0),
				new TestFilter(1, 1, 0, 0),
				new TestFilter(3, 1, 2, 0),
		});

		FilterDimensions filterDimensions = new FilterDimensions(filters);

		Assert.assertEquals(
				new FilterDimensions(4,1,2,0), 
				filterDimensions);
	}
	
	public static class TestFilter extends AbstractConfigurableFilter {

		public TestFilter(
				int width, 
				int height, 
				int centerX, 
				int centerY) {
			
			this.width = width;
			this.height = height;
			this.centerX = centerX;
			this.centerY = centerY;
		}
		
		@Override
		public void filter(
				Object dataPoint, 
				Object tile, 
				TileSize tileSize,
				TileCoordinates tileCoordinates) {
		}
		
	}
}
