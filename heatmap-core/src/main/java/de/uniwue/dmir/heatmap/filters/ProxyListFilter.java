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

import java.util.List;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * Applies a set of filters. 
 * The  
 * {@link IFilter#getWidth()},
 * {@link IFilter#getHeight()},
 * {@link IFilter#getCenterX()} and 
 * {@link IFilter#getCenterY()} 
 * values are adjusted accordingly.
 * 
 * TODO: finish (width, height, x and y need to be derived from the given filters)
 * 
 * @author Martin Becker
 *
 * @param <TPoint>
 * @param <TTile>
 */
public class ProxyListFilter<TPoint, TTile>
extends AbstractConfigurableFilter<TPoint, TTile>
implements IFilter<TPoint, TTile>{

	private List<IFilter<TPoint, TTile>> filters;

	
	public ProxyListFilter(ITileSizeProvider tileSizeProvider) {
		super(tileSizeProvider);
	}

	
	public void addFilter(IFilter<TPoint, TTile> filter) {
		
		if (!filter.getTileSizeProvider().equals(super.tileSizeProvider)) {
			throw new IllegalArgumentException(
					"The filter's tile size provider does not match.");
		}
		
		this.filters.add(filter);
		
		// set width, height, x and y
	}
	
	@Override
	public <TDerived extends TPoint>void filter(
			TDerived dataPoint, 
			TTile tile, 
			TileCoordinates tileCoordinates) {

		for (IFilter<TPoint, TTile> filter : this.filters) {
			filter.filter(dataPoint, tile, tileCoordinates);
		}
	}

}
