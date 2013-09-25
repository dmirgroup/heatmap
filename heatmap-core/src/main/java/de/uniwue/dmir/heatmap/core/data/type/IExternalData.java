package de.uniwue.dmir.heatmap.core.data.type;

import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;

/**
 * <p>Data being added to a tile always needs to know the coordinates of the 
 * "pixel" where it is located with in the tile.</p>
 * 
 * <p>The top-left pixel of a tile has the coordinates (0,0).</p>
 * 
 * @author Martin Becker
 */
public interface IExternalData {
	RelativeCoordinates getCoordinates();
}
