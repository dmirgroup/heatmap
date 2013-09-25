package de.uniwue.dmir.heatmap.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * Data source to retrieve data relevant for a tile.
 * 
 * @author Martin Becker
 *
 * @param <T> data type to retrieve
 */
public interface IExternalDataSource<T extends IExternalData> {

	/**
	 * @param tileCoordinates coordinates of tile to retrieve data for
	 * @param filter the filter used for the tiles (dependent on the size of 
	 * 		the filter, the data relevant for the tile might change)
	 * 
	 * @return all data relevant for the tile with the given coordinates
	 */
	List<T> getData(
			TileCoordinates tileCoordinates,
			IFilter<?, ?> filter);
	
	/**
	 * @param zoomStart
	 * @param zoomStop
	 * @param filter
	 * 
	 * @return tile which have data for the given zoom level
	 */
	Map<Integer, Set<TileCoordinates>> getNonEmptyTiles(
			int zoomStart, 
			int zoomStop,
			IFilter<?, ?> filter);

}
