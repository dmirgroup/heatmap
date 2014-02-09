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

/**
 * A {@link IZoomLevelMapper} returning a (2^zoom, 2^zoom) tile grid size for a 
 * given zoom level "zoom".
 * 
 * @author Martin Becker
 *
 */
public class DefaultZoomLevelMapper 
implements IZoomLevelMapper {

	@Override
	public ZoomLevelSize getSize(int zoom) {
		int xy = (1 << zoom); // log scale using base 2
		return new ZoomLevelSize(xy, xy);
	}
	
}