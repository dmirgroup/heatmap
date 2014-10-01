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

import de.uniwue.dmir.heatmap.filters.operators.IAdder;
import de.uniwue.dmir.heatmap.filters.pixelaccess.IPixelAccess;
import de.uniwue.dmir.heatmap.tiles.coordinates.IToRelativeCoordinatesMapper;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

/**
 * 
 * @see ErodingFilter
 *
 * @author Martin Becker
 *
 * @param <TPoint>
 * @param <TPixel>
 * @param <TTile>
 */
public class AddingFilter<TPoint, TPixel, TTile> 
extends ErodingFilter<TPoint, TPixel, TTile> {
	
	public AddingFilter(
			IToRelativeCoordinatesMapper<? super TPoint> dataToRelativeCoordinatesMapper, 
			IMapper<? super TPoint, TPixel> dataToPixelMapper,
			IPixelAccess<TPixel, TTile> pixelAccess, 
			IAdder<TPixel> pixelAdder) {
		
		super(
				dataToRelativeCoordinatesMapper, 
				dataToPixelMapper, 
				pixelAccess,
				pixelAdder,
				1,
				1,
				0,
				0);
	}
}