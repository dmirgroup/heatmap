package de.uniwue.dmir.heatmap.core.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Utility class to store two dimensional arrays into one dimensional arrays.
 * 
 * @author Martin Becker
 *
 */
public class Arrays2d {
	

	public static <T> T[] clipRange(
			
			int minX,
			int minY,
			
			int width,
			int height,
			
			T[] array, 
			int arrayWidth,
			int arrayHeight) {
		
		int length = width * height;
		
		@SuppressWarnings("unchecked")
		T[] clip = (T[]) Array.newInstance(
				array.getClass().getComponentType(), 
				length);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int outerIndex = index(x + minX, y + minY, width, height);
				int innerIndex = index(x, y, width, height);
				clip[innerIndex] = array[outerIndex];
			}
		}
		
		return clip;
	}
	
	public static <T> T[] clip(
			
			int minX,
			int minY,
			
			int maxX,
			int maxY,
			
			T[] array, 
			int width,
			int height) {
		
		int diffX = maxX - minX;
		int diffY = maxY - minY;
		
		return clipRange(minX, minY, diffX, diffY, array, width, height);
	}
	
	public static <T> T[] line(int line, T[] array, int width, int height) {
		return clip(0, line, width, line + 1, array, width, height);
	}
	
	public static <T> int height(T[] array, int width) {
		
		if (array.length % width != 0) {
			throw new IllegalArgumentException("Wrong width.");
		}
		
		return array.length / width;
		
	}
	
	public static int index(
			int x, int y,
			int width, int height) {
		
		if (x < 0 || x >= width) {
			throw new IndexOutOfBoundsException(
					String.format(
							"x must be within [0, width-1]=[0, %d], but was: %d",
							width - 1, x));
		}
		
		return y * width + x;
		
	}
	
	public static int x(
			int index, 
			int width, 
			int height) {
		
		return index % width;
		
	}
	
	public static int y(
			int index, 
			int width, 
			int height) {

		return index / width;
	}
	
	public static boolean checkIndex(int x, int y, int width, int height) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	public static <T> void set(
			T element,
			int x, 
			int y, 
			T[] array, 
			int width,
			int height) {
		
		int index = index(x, y, width, height);
		array[index] = element;
	}
	
	public static <T> T get(
			int x, 
			int y, 
			T[] array, 
			int width,
			int height) {
		
		int index = index(x, y, width, height);
		return array[index];
	}
	
	public static void set(
			int element,
			int x, 
			int y, 
			int[] array, 
			int width,
			int height) {
		
		int index = index(x, y, width, height);
		array[index] = element;
	}
	
	public static int get(
			int x, 
			int y, 
			int[] array, 
			int width,
			int height) {
		
		int index = index(x, y, width, height);
		return array[index];
	}
	
	public static <T> String toString(T[] array, int width, int height) {

		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < height(array, width); i ++) {
			T[] line = line(i, array, width, height);
			buffer.append(Arrays.toString(line));
			buffer.append(System.getProperty("line.separator"));
		}
		
		return buffer.toString();
	}
}
