package de.uniwue.dmir.heatmap.core.tile.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.ITileCoordinatesProjection;

/**
 * <p>Coordinates of a tile consisting of zoom level, and (x,y) coordinates.</p>
 * 
 * Examples:
 * <ul>
 * <li>the coordinates of the top-left tile on zoom level z are (0,0,z)</li>
 * <li>the coordinates of tile to the right of (0,0,z) are (1,0,z)</li>
 * </ul>
 * 
 * <p><strong>Note</strong>, that the (0, 0) coordinates do not necessarily 
 * define the top-left corner of the tile grid; in some cases (0, 0) might be 
 * the bottom-left corner or in the middle of the tile grid depending on the 
 * given {@link ITileCoordinatesProjection}.</p>
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class TileCoordinates {
	
	private long x;
	private long y;
	
	private int zoom;
}
