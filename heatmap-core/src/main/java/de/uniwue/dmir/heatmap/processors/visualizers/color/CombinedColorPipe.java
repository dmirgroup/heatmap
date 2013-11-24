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
package de.uniwue.dmir.heatmap.processors.visualizers.color;

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
