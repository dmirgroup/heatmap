package de.uniwue.dmir.heatmap.core.data.source.geo;

import java.util.List;

/**
 * A data source allowing to retrieve data within a given bounding box.
 * 
 * @author Martin Becker
 *
 * @param <S> type of the data to retrieve
 */
public interface IGeoDataSource<S> {
	
	/**
	 * @param westLon western longitude
	 * @param northLat northern latitude
	 * @param eastLon eastern longitude
	 * @param southLat southern latitude
	 * 
	 * @return data within the given bounding box
	 */
	List<S> getData(
			double westLon,
			double northLat,
			
			double eastLon,
			double southLat);
	
}
