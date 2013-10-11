package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.visualizer.IColorScheme;

@AllArgsConstructor
public class ImageColorScheme implements IColorScheme {

	private BufferedImage image;
	private double[] ranges;
	
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
	

}
