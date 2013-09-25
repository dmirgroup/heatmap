package de.uniwue.dmir.heatmap.tile.util;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.core.util.Arrays2d;

public class Arrays2dTest {

	public static final int DIM_3 = 3;
	public static final Integer[] ARRAY_3X3 = new Integer[] {
			1, 2, 3,
			4, 5, 6,
			7, 8, 9};
	
	
	@Test
	public void testClip() {
		
		Integer[] array = new Integer[] {
				1, 2, 3,
				4, 5, 6,
				7, 8, 9};
		
		// clip 1
		
		Integer[] clip1Test = new Integer[] {
				1, 2,
				4, 5};

		Integer[] clip1 = Arrays2d.clip(0, 0, 2, 2, array, 3, 3);
		
		Assert.assertArrayEquals(clip1Test, clip1);
		
		// clip 2
		
		Integer[] clip2Test = new Integer[] {
				5, 6,
				8, 9};

		Integer[] clip2 = Arrays2d.clip(1, 1, 3, 3, array, 3, 3);
		
		Assert.assertArrayEquals(clip2Test, clip2);
		
		// clip 3
		
		Integer[] clip3Test = new Integer[] {5};

		Integer[] clip3 = Arrays2d.clip(1, 1, 2, 2, array, 3, 3);
		
		Assert.assertArrayEquals(clip3Test, clip3);
	}
	
	@Test
	public void testIndex() {
		
		Assert.assertEquals(0, Arrays2d.index(0, 0, 3, 3));
		Assert.assertEquals(1, Arrays2d.index(1, 0, 3, 3));
		Assert.assertEquals(2, Arrays2d.index(2, 0, 3, 3));

		Assert.assertEquals(3, Arrays2d.index(0, 1, 3, 3));
		Assert.assertEquals(4, Arrays2d.index(1, 1, 3, 3));
		Assert.assertEquals(5, Arrays2d.index(2, 1, 3, 3));

		Assert.assertEquals(6, Arrays2d.index(0, 2, 3, 3));
		Assert.assertEquals(7, Arrays2d.index(1, 2, 3, 3));
		Assert.assertEquals(8, Arrays2d.index(2, 2, 3, 3));
	}
	
	public void testX() {
		
		Assert.assertEquals(0, Arrays2d.x(0, 3, 3));
		Assert.assertEquals(1, Arrays2d.x(1, 3, 3));
		Assert.assertEquals(2, Arrays2d.x(2, 3, 3));

		Assert.assertEquals(0, Arrays2d.x(3, 3, 3));
		Assert.assertEquals(1, Arrays2d.x(4, 3, 3));
		Assert.assertEquals(2, Arrays2d.x(5, 3, 3));

		Assert.assertEquals(0, Arrays2d.x(6, 3, 3));
		Assert.assertEquals(1, Arrays2d.x(7, 3, 3));
		Assert.assertEquals(2, Arrays2d.x(8, 3, 3));
	}
	public void testY() {
		
		Assert.assertEquals(0, Arrays2d.x(0, 3, 3));
		Assert.assertEquals(0, Arrays2d.x(1, 3, 3));
		Assert.assertEquals(0, Arrays2d.x(2, 3, 3));

		Assert.assertEquals(1, Arrays2d.x(3, 3, 3));
		Assert.assertEquals(1, Arrays2d.x(4, 3, 3));
		Assert.assertEquals(1, Arrays2d.x(5, 3, 3));

		Assert.assertEquals(2, Arrays2d.x(6, 3, 3));
		Assert.assertEquals(2, Arrays2d.x(7, 3, 3));
		Assert.assertEquals(2, Arrays2d.x(8, 3, 3));
	}
	
	@Test
	public void testToString() {
		
		Integer[] array = new Integer[] {
				1, 2, 3,
				4, 5, 6,
				7, 8, 9};
		
		String string = 
				"[1, 2, 3]" + System.getProperty("line.separator") +
				"[4, 5, 6]" + System.getProperty("line.separator") +
				"[7, 8, 9]" + System.getProperty("line.separator");

		Assert.assertEquals(string, Arrays2d.toString(array, 3, 3));
	}
	
	@Test
	public void testGetSet() {
		
		Integer[] array = new Integer[] {
				1, 2, 3,
				4, 5, 6,
				7, 8, 9};
		
		// get
		Assert.assertEquals(new Integer(5), Arrays2d.get(1, 1, array, 3, 3));
		
		// set
		Arrays2d.set(10, 1, 1, array, 3, 3);
		Assert.assertEquals(new Integer(10), Arrays2d.get(1, 1, array, 3, 3));
	}

}
