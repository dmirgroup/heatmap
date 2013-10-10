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
	
	public static double[] clipRangeDouble(
			
			int minX,
			int minY,
			
			int width,
			int height,
			
			double[] array, 
			int arrayWidth,
			int arrayHeight) {
		
		int length = width * height;
		
		double[] clip = (double[]) Array.newInstance(
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
	
	public static double[] clipDouble(
			
			int minX,
			int minY,
			
			int maxX,
			int maxY,
			
			double[] array, 
			int width,
			int height) {
		
		int diffX = maxX - minX;
		int diffY = maxY - minY;
		
		return clipRangeDouble(minX, minY, diffX, diffY, array, width, height);
	}
	
	public static <T> T[] line(int line, T[] array, int width, int height) {
		return clip(0, line, width, line + 1, array, width, height);
	}
	
	public static double[] lineDouble(int line, double[] array, int width, int height) {
		return clipDouble(0, line, width, line + 1, array, width, height);
	}
	
	public static <T> int height(T[] array, int width) {
		
		if (array.length % width != 0) {
			throw new IllegalArgumentException("Wrong width.");
		}
		
		return array.length / width;
		
	}
	
	public static <T> int heightDouble(double[] array, int width) {
		
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
	
	public static boolean isIndexWithinBounds(int x, int y, int width, int height) {
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
	
	public static void setInt(
			int element,
			int x, 
			int y, 
			int[] array, 
			int width,
			int height) {
		
		int index = index(x, y, width, height);
		array[index] = element;
	}
	
	public static void setDouble(
			double element,
			int x, 
			int y, 
			double[] array, 
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
	
	public static String toStringDouble(double[] array, int width, int height) {

		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < heightDouble(array, width); i ++) {
			double[] line = lineDouble(i, array, width, height);
			buffer.append(Arrays.toString(line));
			buffer.append(System.getProperty("line.separator"));
		}
		
		return buffer.toString();
	}
}
