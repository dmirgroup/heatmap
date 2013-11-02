package de.uniwue.dmir.heatmap.impl.core.visualizer.colors;

import java.awt.Color;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CombinedColorPipe<T> 
implements IColorPipe<T> {

	private IColorPipe<T> colorPipe;
	private IAlphaPipe<T> alphaPipe;
	
	@Override
	public Color getColor(T object) {
		
		Color color = this.colorPipe.getColor(object);
		
		if (this.alphaPipe != null) {
			int alphaCode = this.alphaPipe.getAlpha(object);
			color = new Color(
					color.getRed(), 
					color.getGreen(), 
					color.getBlue(), 
					alphaCode);
		}
		
		return color;
	}

}
