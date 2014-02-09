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
package de.uniwue.dmir.heatmap.filters;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.IFilter;

@Data
@AllArgsConstructor
public final class FilterDimensions<TData, TTile> {
	
	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	@SuppressWarnings("rawtypes")
	public FilterDimensions(
			Collection<IFilter<TData, TTile>> filters) {
		
		int left = 0;
		int right = 0;
		int top = 0;  
		int bottom = 0;
		
		for (IFilter f : filters) {
			
			left = Math.max(left, f.getCenterX());
			top = Math.max(top, f.getCenterY());

			right = Math.max(right, f.getWidth() - f.getCenterX());
			bottom = Math.max(bottom, f.getHeight() - f.getCenterY());
			
		}
		
		this.width = left + right;
		this.height = top + bottom;
		this.centerX = left;
		this.centerY = top;
	}
	
	public void setDimensions(IConfigurableFilter<?, ?> filter) {
		filter.setWidth(this.width);
		filter.setHeight(this.height);
		filter.setCenterX(this.centerX);
		filter.setCenterY(this.centerY);
	}
}