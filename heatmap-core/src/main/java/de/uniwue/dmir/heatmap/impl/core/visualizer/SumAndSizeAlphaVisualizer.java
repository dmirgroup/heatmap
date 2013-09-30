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
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class SumAndSizeAlphaVisualizer implements IVisualizer<SumAndSize> {

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
	
	public SumAndSizeAlphaVisualizer(
			BufferedImage colorScheme, 
			double[] ranges) {

		this.colorScheme = colorScheme;
		this.ranges = ranges;
	}
	
	public SumAndSizeAlphaVisualizer(double scaleFactor) {
		this.scalingFactor = scaleFactor;
	}
	
	public SumAndSizeAlphaVisualizer() {
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
	
	public static double[] ranges(double min, double max, int colors) {
		
		double[] ranges = new double[colors - 2];
		ranges[0] = min;
		
		double diff = max - min;
		double step = diff / (colors - 3);
		
		for (int i = 1; i < colors - 2; i ++) {
			ranges[i] = min + i * step;
		}
		
		return ranges;
	}
	
	public BufferedImage visualize(ITile<?, SumAndSize> tile) {

		int width = tile.getSize().getWidth();
		int height = tile.getSize().getHeight();
		
		SumAndSize[] objects = tile.getData();
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics graphics = image.createGraphics();
		
		// debugging
		if (this.debug) {
			graphics.setColor(Color.BLACK);
			graphics.drawRect(0, 0, width - 1, height - 1);
			graphics.drawString(String.format(
					"x:%d, y:%d, z:%d", 
					tile.getCoordinates().getX(),
					tile.getCoordinates().getY(),
					tile.getCoordinates().getZoom()),
					5, 15);
		}
		
		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j < height; j++) {

				SumAndSize object = Arrays2d.get(i, j, objects, width, height);

				if (this.colorScheme == null) {
					if (object != null && object.getSum() > 0) {
						float value = (float) Math.min(object.getSum() / this.scalingFactor, 1);
						Color color = new Color(value, value, value, this.alphaValue);
						image.setRGB(i, j, color.getRGB());
					} else {
						image.setRGB(i, j, this.backgroundColor.getRGB());
					}
				} else {
					if (object == null) {
						image.setRGB(i, j, this.backgroundColor.getRGB());
					} else {
						int index = this.colorScheme.getHeight() - 1 - colorIndex(object.getSum());
						int color = this.colorScheme.getRGB(0, index);
						
						if (this.forceAlphaValue) {
							Color alphaColor = new Color(color);
							alphaColor = new Color(
									alphaColor.getRed(), 
									alphaColor.getGreen(), 
									alphaColor.getBlue(), 
									(int) (this.alphaValue * 255));
							color = alphaColor.getRGB();
						}
						image.setRGB(i, j, color);
					}
				}
			}
		}
		
		return image;
	}
	

}
