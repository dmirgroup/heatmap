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
import de.uniwue.dmir.heatmap.core.visualizer.IColorScheme;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.Sum;

public class SumAlphaVisualizer 
extends AbstractDebuggingVisualizer<Sum[]> {

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
	
	private double scalingFactor = DEFAULT_SCALE_FACTOR;
	
	private IColorScheme colorScheme;
	
	public SumAlphaVisualizer(IColorScheme colorScheme) {
		this.colorScheme = colorScheme;
	}
	
	public SumAlphaVisualizer(double scaleFactor) {
		this.scalingFactor = scaleFactor;
	}
	
	public SumAlphaVisualizer() {
		this(DEFAULT_SCALE_FACTOR);
	}
	
	
	public BufferedImage visualize(
			Sum[] data,
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

				Sum object = Arrays2d.get(i, j, data, width, height);

				if (this.colorScheme == null) {
					
					// we are using gray scale now
					
					if (object != null && object.getSum() > 0) {
						
						// scale the given value by the configured scaling factor 
						// the corresponding value may not exceed 0
						float value = (float) Math.min(object.getSum() / this.scalingFactor, 1);
						
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
						int color = this.colorScheme.getColor(object.getSum());
						
						// we may want to force a configured alpha value
						if (this.forceAlphaValue) {
							Color alphaColor = new Color(color);
							alphaColor = new Color(
									alphaColor.getRed(), 
									alphaColor.getGreen(), 
									alphaColor.getBlue(), 
									(int) (this.alphaValue * 255));
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
