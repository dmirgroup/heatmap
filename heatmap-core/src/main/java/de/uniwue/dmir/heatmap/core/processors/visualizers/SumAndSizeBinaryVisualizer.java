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
package de.uniwue.dmir.heatmap.core.processors.visualizers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.SumPixel;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

/**
 * Sets all non-null and non-zero values to black and everything else to white.
 * 
 * @author Martin Becker
 *
 */
public class SumAndSizeBinaryVisualizer 
extends AbstractDebuggingVisualizer<SumPixel[]> {

	@Getter
	@Setter
	private double alpha;
	
	public BufferedImage visualizeWithDebuggingInformation(
			SumPixel[] data,
			TileSize tileSize,
			TileCoordinates coordinates) {
		
		int width = tileSize.getWidth();
		int height = tileSize.getHeight();

		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);

		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j < height; j++) {
				SumPixel object = Arrays2d.get(i, j, data, width, height);
				if (object != null && object.getSum() > 0) {
					image.setRGB(i, j, Color.BLACK.getRGB());
				} else {
					image.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}

		double[] alpha = new double[width * height];
		Arrays.fill(alpha, this.alpha);

		WritableRaster alphaRaster = image.getAlphaRaster();
		alphaRaster.setPixels(0, 0, width, height, alpha);

		return image;
	}
	
}
