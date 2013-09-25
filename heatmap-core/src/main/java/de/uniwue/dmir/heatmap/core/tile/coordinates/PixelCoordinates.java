package de.uniwue.dmir.heatmap.core.tile.coordinates;

import lombok.AllArgsConstructor;

import lombok.Data;

/**
 * <p>Each tile has a number of pixels. 
 * By putting all tiles together a large grid of pixels is formed.
 * Pixel coordinates (x, y) represent to position of a single pixel in this grid.</p>
 * 
 * Examples:
 * <ul>
 * 	<li>the top-left pixel has coordinates (0, 0)</li>
 * 	<li>the pixel to the right of (0,0) has the coordinates (1, 0)</li>
 * </ul>
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class PixelCoordinates {
	private long x;
	private long y;
}
