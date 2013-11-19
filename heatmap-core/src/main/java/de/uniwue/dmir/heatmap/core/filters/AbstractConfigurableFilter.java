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
package de.uniwue.dmir.heatmap.core.filters;

import de.uniwue.dmir.heatmap.core.IFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract implementation of the {@link IFilter} interface implementing
 * filter dimensions handling, 
 * i.e. access and modification of values retrieved by
 * 
 * {@link IFilter#getWidth()},
 * {@link IFilter#getHeight()},
 * {@link IFilter#getCenterX()} and 
 * {@link IFilter#getCenterY()}.
 * 
 * The default values are 
 * <code>width=1</code>, 
 * <code>height=1</code>, 
 * <code>centerX=0</code>, 
 * <code>centerY=0</code>.
 * 
 * 
 * @author Martin Becker
 *
 * @param <TData> type of the data to be incorporated into the tile
 * @param <TTile> type of the tile to incorporate data into
 */
@Getter
@Setter
public abstract class AbstractConfigurableFilter<TData, TTile> 
extends AbstractFilter<TData, TTile> {

	private int width = 1;
	private int height = 1;
	private int centerX = 0;
	private int centerY = 0;
	
}
