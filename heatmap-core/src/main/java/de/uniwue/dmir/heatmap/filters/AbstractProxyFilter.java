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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.IFilter;

/**
 * A proxy filter helps to relay a given tile type to a filter which was built
 * for another tile type.
 * This abstract implementation provides a filter property and relays the
 * dimensions of this filter to the 
 * {@link IFilter#getWidth()},
 * {@link IFilter#getHeight()},
 * {@link IFilter#getCenterX()} and 
 * {@link IFilter#getCenterY()} 
 * methods.
 * 
 * @author Martin Becker
 *
 * @param <TData> data to incorporate into the outer tile
 * @param <TInnerTile> type of the outer tile to incorporate data into
 * @param <TOuterTile> type of the inner tile to incorporate data into
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractProxyFilter<TData, TInnerTile, TOuterTile> 
extends AbstractFilter<TData, TOuterTile> {

	/** Proxied filter. */
	protected IFilter<TData, TInnerTile> filter;

	@Override
	public int getWidth() {
		return this.filter.getWidth();
	}

	@Override
	public int getHeight() {
		return this.filter.getHeight();
	}

	@Override
	public int getCenterX() {
		return this.filter.getCenterX();
	}

	@Override
	public int getCenterY() {
		return this.filter.getCenterY();
	}

}
