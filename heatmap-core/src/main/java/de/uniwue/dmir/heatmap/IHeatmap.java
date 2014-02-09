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

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * <h1>Tiles</h1>
 * <p>Overlays of maps are commonly built separately for each zoom level (note 
 * that a map may also be a text document or a web page with only one zoom level).
 * On each zoom level, the overlay is divided into equally sized tiles to 
 * support partial access.
 * Thus, each tile is uniquely identified by its zoom level and its coordinates 
 * in the tile space (x, y, zoom).
 * 
 * <p>For example 
 * the top-left tile on zoom level z is identified by (0,0,z), 
 * the tile to the right of (0,0,z) is identified by (1,0,z).</p>
 * 
 * <p>Note that by using a {@link ITileCoordinatesProjection} the default
 * top-left oriented way of indexing tiles can be altered.</p>
 * 
 * <h1>Process</h1>
 * We need the following components to define a heatmap:
 * <ul>
 * 	<li>
 * 		the heat map's dimensions are set via {@link IHeatmapDimensions},
 * 	</li>
 * 	<li>
 * 		an {@link IPointsource}, to get the data for each tile 
 * 	</li>
 * 	<li>
 * 		a {@link IFilter} to incorporate data points into a tile.
 * 	</li>
 * </ul>
 * 
 * Heat maps can be written to images using the {@link IHeatmapWriter} and a
 * matching {@link IVisualizer}.
 * 
 * @author Martin Becker
 */
public interface IHeatmap<TTile> {
	
	public static final int DEFAULT_TILE_WIDTH = 256;
	public static final int DEFAULT_TILE_HEIGHT = 256;
	
	public static final int DEFAULT_MIN_ZOOM_LEVEL = 0;
	public static final int DEFAULT_MAX_ZOOM_LEVEL = 18;
	
	/**
	 * @return current heatmap settings
	 */
	HeatmapSettings getSettings();
	
	/**
	 * @param coordinates tile coordinates
	 * 
	 * @return <code>null</code> if the tile does not contain data; 
	 * 		the tile corresponding to the coordinates otherwise
	 */
	TTile getTile(TileCoordinates coordinates);
	
	/**
	 * Processes all tiles. 
	 * The order of the tiles being processed is determined by the heatmap 
	 * implementation.
	 * Thus, the heatmap implementation can optimize internal tile and data 
	 * access.
	 * 
	 * @param processor the processor to process the tiles
	 */
	void processTiles(ITileProcessor<TTile> processor);
}
