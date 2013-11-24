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
package de.uniwue.dmir.heatmap.tiles.coordinates;

import lombok.Data;

/**
 * Basic implementation of a tile pixel.
 * 
 * @author Martin Becker
 */
@Data
public class BasicRelativeCoordinatesProvider 
implements IRelativeCoordinatesProvider {
	
	private RelativeCoordinates coordinates;

	public BasicRelativeCoordinatesProvider() {
		this(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}
	
	public BasicRelativeCoordinatesProvider(int x, int y) {
		this.coordinates = new RelativeCoordinates(x, y);
	}
	
	public BasicRelativeCoordinatesProvider(RelativeCoordinates relativeCoordinates) {
		this.coordinates = relativeCoordinates;
	}
	
	public void setCoordinateValues(int x, int y) {
		this.coordinates.setX(x);
		this.coordinates.setY(y);
	}
	
	public void setCoordinateValues(RelativeCoordinates coordinates) {
		this.coordinates.setX(coordinates.getX());
		this.coordinates.setY(coordinates.getY());
	}
}
