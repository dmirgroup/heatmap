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
package de.uniwue.dmir.heatmap;

public interface ITileSizeProvider {

	TileSize getTileSize(int zoom);
	
	/**
	 * {@link ITileRangeProvider}s are equal when the return the same
	 * {@link TileSize} as this provider for each zoom level.
	 * As there are no zoom level ranges, this usually means, that
	 * they have to be of the same type and have the same parameters.
	 * 
	 * @param obj the {@link ITileRangeProvider} to compare to
	 * 
	 * @return <code>true</code> if the object is a {@link ITileRangeProvider}s
	 * 		and return the same {@link TileSize} as this provider for each zoom level
	 */
	@Override
	public boolean equals(Object obj);
}
