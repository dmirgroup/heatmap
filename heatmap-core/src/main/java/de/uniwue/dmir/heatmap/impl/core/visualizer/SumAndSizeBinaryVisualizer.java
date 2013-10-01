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
import java.awt.image.WritableRaster;
import java.util.Arrays;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class SumAndSizeBinaryVisualizer 
implements IVisualizer<SumAndSize[]> {

	public BufferedImage visualize(
			SumAndSize[] data,
			TileSize tileSize,
			TileCoordinates coordinates) {
		
		int width = tileSize.getWidth();
		int height = tileSize.getHeight();

		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);

		Graphics graphics = image.createGraphics();

		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j < height; j++) {
				SumAndSize object = Arrays2d.get(i, j, data, width, height);
				if (object != null && object.getSum() > 0) {
					graphics.setColor(new Color(255,255,255));
				} else {
					graphics.setColor(new Color(0,0,0));
				}
				graphics.fillRect(i, j, 1, 1);
			}
		}

//		int[] colors = new int[width * height];
//		for (int i  = 0; i < width; i++) {
//			for (int j  = 0; j < height; j++) {
//				SumAndSize object = Arrays2d.get(i, j, data, width, height);
//				int color = 0;
//				if (object != null && object.getSum() > 0) {
//					graphics.setColor(new Color(255,255,255));
//					color = 255;
//				}
//				Arrays2d.set(color, i, j, colors, width, height);
//			}
//		}

//		WritableRaster colorRaster = image.getRaster();

		double[] alpha = new double[width * height];
		Arrays.fill(alpha, 125);

		WritableRaster raster = image.getAlphaRaster();
		raster.setPixels(0, 0, width, height, alpha);

		return image;
	}
}
