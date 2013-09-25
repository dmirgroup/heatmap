package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.SumAndSize;

public class SumAndSizeBinaryVisualizer 
implements IVisualizer<SumAndSize> {

	public BufferedImage visualize(ITile<?, SumAndSize> tile) {
		
		int width = tile.getDimensions().getWidth();
		int height = tile.getDimensions().getHeight();

		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);

		Graphics graphics = image.createGraphics();

		SumAndSize[] data = tile.getData();
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
