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
 * MTDataRCHANTABILITY or FITNTDataSS FOR A PARTICULAR PURPOSTData.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.core.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IFilter;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractProxyFilter<TData, TInnerTile, TOuterTile> 
extends AbstractFilter<TData, TOuterTile> {

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
