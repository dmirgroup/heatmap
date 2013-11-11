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

import de.uniwue.dmir.heatmap.core.filters.access.ArrayPixelAccess;
import de.uniwue.dmir.heatmap.core.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;

public class AddingArrayFilter<TData, TPixel> 
extends AddingFilter<TData, TPixel, TPixel[]> {

	public AddingArrayFilter(
			IToRelativeCoordinatesMapper<TData> toRelativeCoordinatesMapper,
			IMapper<TData, TPixel> dataToPixelMapper, 
			IAdder<TPixel> pixelAdder) {
		super(
				new ArrayPixelAccess<TPixel>(), 
				toRelativeCoordinatesMapper,
				dataToPixelMapper, 
				pixelAdder);
	}
}