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
package de.uniwue.dmir.heatmap.core.tiles.coordinates;

import lombok.AllArgsConstructor;

import lombok.Data;

/**
 * <p>Each tile has a number of pixels. 
 * By putting all tiles together a large grid of pixels is formed.
 * Pixel coordinates (x, y) represent to position of a single pixel in this grid.</p>
 * 
 * Examples:
 * <ul>
 * 	<li>the top-left pixel has coordinates (0, 0)</li>
 * 	<li>the pixel to the right of (0,0) has the coordinates (1, 0)</li>
 * </ul>
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class PixelCoordinates {
	private long x;
	private long y;
}
