package de.uniwue.dmir.heatmap.core.processors.visualizers.color;

import java.awt.Color;

public class SimpleAlphaColorScheme implements IColorScheme {

	@Override
	public int getColor(double value) {
		Color color = new Color(0,0,0, (int) (value * 255));
		return color.getRGB();
	}

}
