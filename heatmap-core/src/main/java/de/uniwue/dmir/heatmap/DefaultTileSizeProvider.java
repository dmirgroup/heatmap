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

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DefaultTileSizeProvider implements ITileSizeProvider {

	public static final int DEFAULT_WIDTH = 256;
	public static final int DEFAULT_HEIGHT = 256;
	
	private int width;
	private int height;
	
	public DefaultTileSizeProvider() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public DefaultTileSizeProvider(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public TileSize getTileSize(int zoom) {
		return new TileSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
}
