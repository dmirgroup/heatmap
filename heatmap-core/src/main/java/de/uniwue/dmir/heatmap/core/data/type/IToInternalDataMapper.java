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
package de.uniwue.dmir.heatmap.core.data.type;

import de.uniwue.dmir.heatmap.core.IExternalDataSource;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;


/**
 * <p>Convenience interface that can be used by different {@link IExternalDataSource} 
 * implementations to map internal data types to tile relevant data types.</p>
 * 
 * <p>Maps a given object of a certain data type to an object representing
 * a data point to be associated with a certain pixel of that tile.</p>
 * 
 * @author Martin Becker
 *
 * @param <S> type of the data object to map to a pixel
 * @param <T> type of the result of the mapping
 */
public interface IToInternalDataMapper<S, T extends IExternalData> {
	
	/**
	 * @param sourceObject object to map to a data point associated with a pixel 
	 * @param relativeCoordinates relative coordinates within in the tile the 
	 * 		given object is supposed to be associated with
	 * 
	 * @return data point to be associated with a certain pixel of that tile
	 */
	T map(S sourceObject, RelativeCoordinates relativeCoordinates);
}
