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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.newbrightidea.util.RTree;

public class RTreeTest {
	
	@Test
	public void test() {
		
		RTree<String> rTree = new RTree<String>();
		rTree.insert(new float[] {0, 0},  "test");
		
		// top left
		List<String> result1 = rTree.search(
				new float[] {2, -2}, 
				new float[] {3, 3});
		Assert.assertEquals(0, result1.size());
		
		// bottom left
		List<String> result2 = rTree.search(
				new float[] {-2, -2}, 
				new float[] {3, 3});
		Assert.assertEquals(1, result2.size());
		
		// inclusive (top right)
		List<String> result3 = rTree.search(
				new float[] {-2, -2}, 
				new float[] {2, 2});
		Assert.assertEquals(1, result3.size());
		

		// inclusive (bottom left)
		List<String> result4 = rTree.search(
				new float[] {0, 0}, 
				new float[] {2, 2});
		Assert.assertEquals(1, result4.size());
		
	}
}
