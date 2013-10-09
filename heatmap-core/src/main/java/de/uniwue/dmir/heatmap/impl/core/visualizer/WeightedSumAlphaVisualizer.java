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
package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.WeightedSum;

public class WeightedSumAlphaVisualizer
extends AbstractDebuggingVisualizer<WeightedSum[]> {

	public static final double DEFAULT_SCALE_FACTOR = 100;
	public static final float DEFAULT_ALPHA_VALUE = 0.7f;
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 0);
	
	@Setter
	private boolean debug = false;
	
	@Setter
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
	
	@Setter
	private float alphaValue = DEFAULT_ALPHA_VALUE;
	
	@Setter
	private boolean forceAlphaValue = false;
	
	private BufferedImage colorScheme;
	private double[] ranges;
	
	private double scalingFactor = DEFAULT_SCALE_FACTOR;
	
	public WeightedSumAlphaVisualizer(
			BufferedImage colorScheme, 
			double[] ranges) {

		this.colorScheme = colorScheme;
		this.ranges = ranges;
	}
	
	public WeightedSumAlphaVisualizer(double scaleFactor) {
		this.scalingFactor = scaleFactor;
	}
	
	public WeightedSumAlphaVisualizer() {
		this(DEFAULT_SCALE_FACTOR);
	}
	
	
	private int colorIndex(double value) {
		for (int i = 0; i < this.ranges.length; i ++) {
			if (value < this.ranges[i]) {
				return i;
			}
		}
		return this.colorScheme.getHeight() - 1;
	}
	
	private double average(WeightedSum weightedSum) {
		if (weightedSum.getSumOfWeights() == 0) {
			return 0;
		} else {
			return  weightedSum.getSumOfWeightedValues() / weightedSum.getSumOfWeights();
		}
	}
	
	public BufferedImage visualize(
			WeightedSum[] data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		int width = tileSize.getWidth();
		int height = tileSize.getHeight();
		
		// initialize buffered image 
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);
		
		// run through pixels
		
		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j < height; j++) {

				WeightedSum object = Arrays2d.get(i, j, data, width, height);

				if (this.colorScheme == null) {
					
					// we are using gray scale now
					
					if (object != null && object.getSize() > 0) {
						
						// scale the given value by the configured scaling factor 
						// the corresponding value may not exceed 0
						float value = (float) Math.min(average(object) / this.scalingFactor, 1);
						
						// draw the pixel
						
						Color color = new Color(value, value, value, this.alphaValue);
						image.setRGB(i, j, color.getRGB());
						
					} else {
						
						// no object given: set default background color
						image.setRGB(i, j, this.backgroundColor.getRGB());
					}
				} else {
					
					// we are using the given color scheme
					
					if (object != null) {
						
						// get color index
						int index = this.colorScheme.getHeight() - 1 - colorIndex(average(object));
						
						// get the color
						int color = this.colorScheme.getRGB(0, index);
						
						// we may want to force a configured alpha value
						if (this.forceAlphaValue) {
							Color alphaColor = new Color(color);
							
//							int alphaValue = (int) (this.alphaValue * 255);
							int alphaValue = (int) (
									object.getSumOfWeights() / object.getSize() * 255);
							
							alphaColor = new Color(
									alphaColor.getRed(), 
									alphaColor.getGreen(), 
									alphaColor.getBlue(), 
									alphaValue);
							color = alphaColor.getRGB();
						}
						
						// draw the pixel
						image.setRGB(i, j, color);
						
					} else {

						// no object given: set default background color
						image.setRGB(i, j, this.backgroundColor.getRGB());
					}
				}
			}
		}
		
		// debugging
		this.addDebugInformation(tileSize, coordinates, image);
		
		return image;
	}

}

