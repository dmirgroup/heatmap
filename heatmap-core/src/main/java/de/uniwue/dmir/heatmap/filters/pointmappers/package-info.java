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
package de.uniwue.dmir.heatmap.filters.pointmappers;

/**
 * Pointmappers are used by {@link de.uniwue.dmir.heatmap.core.IFilter}s 
 * to map data points to pixels stored by tiles.
 * Those pixels are then combined with the existing pixels within the tile.
 * While this mapping step is not necessary in general and filters can 
 * combined the given data directly, it allows filters to operate on arbitrary
 * data types given the correct pointmapper.
 */
