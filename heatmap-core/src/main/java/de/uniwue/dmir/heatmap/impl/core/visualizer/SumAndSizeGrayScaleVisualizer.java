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

import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class SumAndSizeGrayScaleVisualizer implements IVisualizer<SumAndSize> {

	public BufferedImage visualize(ITile<?, SumAndSize> tile) {

		int width = tile.getDimensions().getWidth();
		int height = tile.getDimensions().getHeight();
		
		SumAndSize[] objects = tile.getData();
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics graphics = image.createGraphics();
		
		
		double max = 0;
		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j <height; j++) {
				SumAndSize object = Arrays2d.get(i, j, objects, width, height);
				if (object != null && object.getSize() != 0 && object.getSum() > 0) {
					max = Math.max(max, object.getSum());
				}
			}
		}
		
		for (int i  = 0; i < width; i++) {
			for (int j  = 0; j < height; j++) {
				SumAndSize object = Arrays2d.get(i, j, objects, width, height);
				if (object != null && object.getSum() > 0) {
					
					float value = (float) (object.getSum() / max);
//					System.out.println(value);
					
					Color color = new Color(value, value, value);
					graphics.setColor(color);
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		
		return image;
	}
	

}
