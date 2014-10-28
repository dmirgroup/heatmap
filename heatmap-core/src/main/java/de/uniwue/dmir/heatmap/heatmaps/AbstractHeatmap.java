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
package de.uniwue.dmir.heatmap.heatmaps;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.DefaultTileSizeProvider;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileSizeProvider;

public abstract class AbstractHeatmap<TTile, TParameters> 
implements IHeatmap<TTile, TParameters> {
	
	@Getter
	@Setter
	protected ITileSizeProvider tileSizeProvider;


	public AbstractHeatmap() {
		this(new DefaultTileSizeProvider());
	}
	
	public AbstractHeatmap(ITileSizeProvider tileSizeProvider) {
		this.tileSizeProvider = tileSizeProvider;
	}
}
