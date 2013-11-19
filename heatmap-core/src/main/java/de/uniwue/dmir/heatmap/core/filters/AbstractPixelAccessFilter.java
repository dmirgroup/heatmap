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

import de.uniwue.dmir.heatmap.core.filters.access.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.IToRelativeCoordinatesMapper;

/** 
 * Abstract filter providing a {@link IPixelAccess} field for extending classes
 * to use.
 * 
 * @author Martin Becker
 *
 * @param <TData> type of the data to be incorporated into the tile
 * @param <TTile> type of the tile to incorporate data into
 */
public abstract class AbstractPixelAccessFilter<TData, TPixel, TTile> 
extends AbstractRelativeCoordinatesMapperFilter<TData, TTile> {

	protected IPixelAccess<TPixel, TTile> pixelAccess;
	
	public AbstractPixelAccessFilter(
			IToRelativeCoordinatesMapper<TData> toRelativeCoordinatesMapper,
			IPixelAccess<TPixel, TTile> pixelAccess) {
		
		super(toRelativeCoordinatesMapper);
		this.pixelAccess = pixelAccess;
	}
	
}
