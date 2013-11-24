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
package de.uniwue.dmir.heatmap.processors.visualizers.color;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImageColorScheme implements IColorScheme {

	private BufferedImage image;
	private double[] ranges;
	
	public ImageColorScheme(BufferedImage image, double min, double max) {
		this(image, equdistantRanges(min, max, image.getHeight()));
	}
	
	@Override
	public int getColor(double value) {
		int index = colorIndex(value);
		return this.image.getRGB(0, this.image.getHeight() - index - 1);
	}
	
	private int colorIndex(double value) {
		for (int i = 0; i < this.ranges.length; i ++) {
			if (value < this.ranges[i]) {
				return i;
			}
		}
		return this.image.getHeight() - 1;
	}
	
	public static double[] equdistantRanges(double min, double max, int colors) {
		
		double[] ranges = new double[colors - 2];
		ranges[0] = min;
		
		double diff = max - min;
		double step = diff / (colors - 3);
		
		for (int i = 1; i < colors - 2; i ++) {
			ranges[i] = min + i * step;
		}
		
		return ranges;
	}
	

}
