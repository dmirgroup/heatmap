package de.uniwue.dmir.heatmap.core.tile.coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.GridDimensions;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

/**
 * <p>Each tile is represented by a grid of pixels.
 * Internal coordinates (x,y) define the position of a pixel within a tile.</p>
 * 
 * Examples:
 * <ul>
 * 	<li>the top-left pixel within a tile has the coordinates (0,0)</li>
 * 	<li>the pixel to the right of (0,0) has the coordinates (1, 0)</li>
 * </ul>
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class RelativeCoordinates {
	
	private int x;
	private int y;
	
	public List<TileCoordinates> overlappingTiles(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter,
			IHeatmapDimensions dimensions) {
		
		List<TileCoordinates> coordinates = new ArrayList<TileCoordinates>();
		
		Boolean[] test = new Boolean[9];
		Arrays.fill(test, true);
		Arrays2d.set(this.getX() - filter.getCenterX() < 0, 
				-1 + 1,  0 + 1,  test, 3, 3);
		Arrays2d.set(this.getY() - filter.getCenterY() < 0, 
				 0 + 1, -1 + 1,  test, 3, 3);
		Arrays2d.set(
				this.getX() + filter.getWidth() - filter.getCenterX() 
					> dimensions.getTileDimensions().getWidth(), 
				 1 + 1,  0 + 1,  test, 3, 3);
		Arrays2d.set(
				this.getY() + filter.getHeight() - filter.getCenterY() 
					> dimensions.getTileDimensions().getHeight(), 
				 0 + 1,  1 + 1,  test, 3, 3);
		
		GridDimensions gridDimensions = 
				dimensions.getGridDimensions(tileCoordinates.getZoom());
		
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
