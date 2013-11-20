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
package de.uniwue.dmir.heatmap.core.processors.visualizers.color;

import java.awt.Color;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

@AllArgsConstructor
public class SimpleColorPipe<T> implements IColorPipe<T> {
	
	private IMapper<T, Double> toDoubleMapper;
	private IColorScheme colorScheme;
	
	@Override
	public Color getColor(T object) {
		double colorValue = this.toDoubleMapper.map(object);
		return new Color(this.colorScheme.getColor(colorValue));
	}
}
