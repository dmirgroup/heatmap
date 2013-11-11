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
package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf;

import de.uniwue.dmir.heatmap.core.data.types.IDataWithRelativeCoordinates;

public class EuclidianDistance implements IDistanceFunction<IDataWithRelativeCoordinates> {

	@Override
	public double distance(IDataWithRelativeCoordinates o1, IDataWithRelativeCoordinates o2) {
		
		double diffX = o1.getCoordinates().getX() - o2.getCoordinates().getX();
		double diffY = o1.getCoordinates().getY() - o2.getCoordinates().getY();
		
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}

}
