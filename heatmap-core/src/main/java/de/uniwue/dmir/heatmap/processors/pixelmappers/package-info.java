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
package de.uniwue.dmir.heatmap.processors.pixelmappers;

/**
 * <p>The <code>pixelmappers</code> package contains classes to map 
 * pixels of a tile to the information needed by 
 * {@link de.uniwue.dmir.heatmap.core.ITileProcessor}s and
 * {@link de.uniwue.dmir.heatmap.core.IFilter}s.
 * This makes processors and filters applicable to a larger variety of tile and 
 * pixel types.</p>
 * 
 * <p>Note that a tile does not necessarily consist of pixels (but does 
 * most of the time).
 * Thus, this abstraction is only applicable if tiles consist of pixels.</p>
 */
