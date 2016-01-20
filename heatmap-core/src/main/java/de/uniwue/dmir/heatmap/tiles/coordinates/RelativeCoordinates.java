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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.IZoomLevelMapper;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.ZoomLevelSize;
import de.uniwue.dmir.heatmap.util.Arrays2d;

/**
 * <p>Each tile is represented by a grid of pixels.
 * Internal coordinates (x,y) define the position of a pixel within a tile.</p>
 * 
 * Examples:
 * <ul>
 * 	<li>the top-left pixel within a tile has the coordinates (0,0)</li>
 * 	<li>the pixel to the right of (0,0) has the coordinates (1, 0)</li>
 * 	<li>the pixel below (0,0) has the coordinates (0, 1)</li>
 * </ul>
 * 
 * @author Martin Becker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelativeCoordinates {
	
	private int x;
	private int y;
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public List<TileCoordinates> overlappingTiles(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter,
			TileSize tileSize,
			IZoomLevelMapper zoomLevelMapper) {
		
		List<TileCoordinates> coordinates = new ArrayList<TileCoordinates>();
		
		Boolean[] test = new Boolean[9];
		Arrays.fill(test, true);
		Arrays2d.set(this.getX() - filter.getCenterX() < 0, 
				-1 + 1,  0 + 1,  test, 3, 3);
		Arrays2d.set(this.getY() - filter.getCenterY() < 0, 
				 0 + 1, -1 + 1,  test, 3, 3);
		Arrays2d.set(
				this.getX() + filter.getWidth() - filter.getCenterX() 
					> tileSize.getWidth(), 
				 1 + 1,  0 + 1,  test, 3, 3);
		Arrays2d.set(
				this.getY() + filter.getHeight() - filter.getCenterY() 
					> tileSize.getHeight(), 
				 0 + 1,  1 + 1,  test, 3, 3);
		
		ZoomLevelSize gridDimensions = 
				zoomLevelMapper.getSize(tileCoordinates.getZoom());
		
		for (int x = -1; x <= 1; x ++) {

			for (int y = -1; y <= 1; y ++) {

				if (
						(x != 0 || y != 0)
						&& Arrays2d.get(x + 1, 0 + 1, test, 3, 3) 
						&& Arrays2d.get(0 + 1, y + 1, test, 3, 3)) {
					
					TileCoordinates newCoordinates = new TileCoordinates(
							(tileCoordinates.getX() + x) % gridDimensions.getWidth(), 
							(tileCoordinates.getY() + y) % gridDimensions.getHeight(), 
							tileCoordinates.getZoom());
					
					coordinates.add(newCoordinates);
					
				}
				
			}
		}
		
		
		return coordinates;
	}
}
