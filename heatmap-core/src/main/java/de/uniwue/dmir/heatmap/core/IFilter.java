package de.uniwue.dmir.heatmap.core;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;

/**
 * A filter merges a given data point into the given tile.
 * 
 * @author Martin Becker
 *
 * @param <E> data type (external) to merge into the tile 
 * @param <I> data type (internal) for merged data as stored in the tile
 */
public interface IFilter<E extends IExternalData, I> {

	/**
	 * Merges a given data point into the given tile.
	 * 
	 * @param dataPoint data point to merge into the tile
	 * @param tile tile to merge data into
	 */
	void filter(E dataPoint, ITile<E, I> tile);
	
	/**
	 * @return width
	 */
	int getWidth();
	
	/**
	 * @return height
	 */
	int getHeight();
	
	/**
	 * @return x coordinate (range from 0 to width-1) of filter center
	 */
	int getCenterX();
	
	/**
	 * @return y coordinate (range from 0 to height-1) of filter center
	 */
	int getCenterY();
	
}
