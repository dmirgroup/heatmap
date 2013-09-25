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
