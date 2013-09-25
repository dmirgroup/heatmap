package de.uniwue.dmir.heatmap.core.util;

import java.lang.reflect.Array;

/**
 * Utility class to store two dimensional arrays into one dimensional arrays.
 * 
 * @author Martin Becker
 *
 */
public class ArraysNd {
	

	public static <T> T[] clipByMax(
			
			int[] min,
			int[] max,
			
			T[] array, 
			int[] arrayWidth) {
		
		int[] diff = new int[min.length];
		for (int i = 0; i < min.length; i ++) {
			diff[i] = max[i] - min[i];
		}
		
		return clip(min, diff, array, arrayWidth);
	}
	
	public static int length(int[] width) {
		int size = 1;
		for (int i = 0; i < width.length; i ++) {
			size *= width[i];
		}
		return size;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] clip(

			int[] min,
			int[] range,
			
			T[] array, 
			int[] width) {
		
		int dimensions = min.length;
		
		int length = length(range);
		
		T[] clip = (T[]) Array.newInstance(
				array.getClass().getComponentType(), 
				length);
		
		for (int i = 0; i < length; i ++) {
			
			int[] outerCoordinates = coordinates(i, range);
			for (int j = 0; j < dimensions; j++) {
				outerCoordinates[j] = outerCoordinates[j] + min[j];
			}
			
			int outerIndex = index(outerCoordinates, width);
			
			clip[i] = array[outerIndex];
		}
		
		return clip;
	}
	
	public static <T> int height(T[] array, int width) {
		
		if (array.length % width != 0) {
			throw new IllegalArgumentException("Wrong width.");
		}
		
		return array.length / width;
		
	}
	
	public static int index(
			int[] coordinates,
			int[] widths) {
		
		int index = coordinates[0];
		int dimSize = 1;
		for (int i = 0; i < coordinates.length - 1; i ++) {
			dimSize *= widths[i];
			index += dimSize * coordinates[i + 1];
		}
		
		return index;
	}
	
	public static int[] coordinates(
			int index, 
			int width[]) {
		
		int[] coordinates = new int[width.length];
		for (int i = 0; i < width.length - 1; i ++) {
			coordinates[i] = index % width[i];
			index -= coordinates[i];
			index /= width[i];
		}
		coordinates[width.length - 1] = index;
		
		return coordinates;
	}
	
	public static boolean checkIndex(int coordinates[], int[] width) {
		
		for (int i = 0; i < coordinates.length; i++) {
			if (coordinates[i] < 0 || coordinates[i] >= width[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	public static <T> T get(
			int[] coordinates, 
			int[] width,
			T[] array) {
		
		int index = index(coordinates, width);
		return array[index];
	}
	
	public static <T> void set(
			T element,
			int[] coordinates,
			int[] width,
			T[] array) {
		
		int index = index(coordinates, width);
		array[index] = element;
	}
	
	public static int get(
			int[] coordinates, 
			int[] width,
			int[] array) {
		
		int index = index(coordinates, width);
		return array[index];
	}
	
	public static void set(
			int element,
			int[] coordinates,
			int[] width,
			int[] array) {
		
		int index = index(coordinates, width);
		array[index] = element;
	}
}
