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
package de.uniwue.dmir.heatmap.util;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.util.RelativeCoordinatesArrayKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.util.IKeyValueIteratorFactory.IKeyValueIterator;

public class RelativeCoordinatesArrayKeyValueIteratorFactoryTest {
	
	@Test
	public void test1() {
		
		String[] strings = new String[] {
				"I", null, "am",
				"not", "here", null
		};
		
		RelativeCoordinatesArrayKeyValueIteratorFactory<String> factory =
				new RelativeCoordinatesArrayKeyValueIteratorFactory<String>(
						3,2, false);
		
		IKeyValueIterator<RelativeCoordinates, String> iterator = 
				factory.instance(strings);
		
		ArrayList<String> test1 = new ArrayList<String>();
		while (iterator.hasNext()) {
			iterator.next();
			test1.add(iterator.getValue());
			System.out.println(iterator.getKey());
			System.out.println(iterator.getValue());
		}
		
		Assert.assertArrayEquals(
				new String[] {"I", "am", "not", "here"}, 
				test1.toArray());
		
	}
	
	@Test
	public void test2() {
		
		String[] strings = new String[] {
				"I", null, "am",
				"not", "here", null
		};
		
		RelativeCoordinatesArrayKeyValueIteratorFactory<String> factory =
				new RelativeCoordinatesArrayKeyValueIteratorFactory<String>(
						3,2, true);
		
		IKeyValueIterator<RelativeCoordinates, String> iterator = 
				factory.instance(strings);
		
		ArrayList<String> test1 = new ArrayList<String>();
		while (iterator.hasNext()) {
			iterator.next();
			test1.add(iterator.getValue());
			System.out.println(iterator.getKey());
			System.out.println(iterator.getValue());
		}
		
		Assert.assertArrayEquals(
				strings, 
				test1.toArray());
		
	}
}
