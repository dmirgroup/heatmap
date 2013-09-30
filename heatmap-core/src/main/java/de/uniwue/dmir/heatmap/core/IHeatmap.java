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
package de.uniwue.dmir.heatmap.core;

import java.util.Iterator;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.ITileCoordinatesProjection.IdentityTileCoordinatesProjection;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

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
 * We need the following components are needed to define a heatmap:
 * <ul>
 * 	<li>
 * 		the heat map's dimensions are set via {@link IHeatmapDimensions},
 * 	</li>
 * 	<li>
 * 		an {@link IExternalDataSource}, to get the data according to tile coordinates
 * 	</li>
 * 	<li>
 * 		a {@link IFilter} to merge data points into a tile.
 * 	</li>
 * </ul>
 * 
 * Heat maps can be written to images using the {@link IHeatmapWriter} and a
 * matching {@link IVisualizer}.
 * 
 * 
 * @author Martin Becker
 */
public interface IHeatmap<E extends IExternalData, I> {
	
	public static final int DEFAULT_TILE_WIDTH = 256;
	public static final int DEFAULT_TILE_HEIGHT = 256;
	
	public static final int DEFAULT_MIN_ZOOM_LEVEL = 0;
	public static final int DEFAULT_MAX_ZOOM_LEVEL = 18;
	
	HeatmapSettings getSettings();
	
	/**
	 * @param coordinates
	 * @param additionalData
	 * 
	 * @return <code>null</code> if the tile does not contain data; 
	 * 		the tile corresponding to the coordinates otherwise
	 */
	ITile<E, I> getTile(TileCoordinates coordinates, I[] additionalData);
	
	/**
	 * @param zoom zoom level
	 * @return iterator with all tile coordinates of the given zoom level that
	 * 		contain data points
	 */
	Iterator<TileCoordinates> getTileCoordinatesWithContent(int zoom);
	
	@Data
	@AllArgsConstructor
	public static class HeatmapSettings {
		
		private TileSize tileSize;
		private ZoomLevelRange zoomLevelRange;
		private IZoomLevelMapper zoomLevelMapper;
		private ITileCoordinatesProjection tileProjection;
		
		public HeatmapSettings() {
			
			this.tileSize = new TileSize(
					DEFAULT_TILE_WIDTH, 
					DEFAULT_TILE_HEIGHT);
			
			this.zoomLevelRange = new ZoomLevelRange(
					DEFAULT_MIN_ZOOM_LEVEL, 
					DEFAULT_MAX_ZOOM_LEVEL);
			
			this.zoomLevelMapper = new DefaultZoomLevelMapper();
			
			this.tileProjection = new IdentityTileCoordinatesProjection();
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class TileSize {
		private int width;
		private int height;
	}
	
	@Data
	@AllArgsConstructor
	public static class ZoomLevelRange {
		private int min;
		private int max;
	}

	@Data
	@AllArgsConstructor
	public static class ZoomLevelSize {
		private int width;
		private int height;
	}
	
	public static interface IZoomLevelMapper {
		public ZoomLevelSize getSize(int zoom);
	}
	
	public static class DefaultZoomLevelMapper 
	implements IZoomLevelMapper {

		@Override
		public ZoomLevelSize getSize(int zoom) {
			int xy = (1 << zoom); // log scale using base 2
			return new ZoomLevelSize(xy, xy);
		}
		
	}
}
