package de.uniwue.dmir.heatmap.core.data.source.geo;


/**
 * Maps a given object to geo coordinates.
 * 
 * @author Martin Becker
 *
 * @param <S> type of the mappable data
 */
public interface IToGeoCoordinatesMapper<S> {
	
	/**
	 * @param object object to map
	 * @return geo coordiantes of the object
	 */
	GeoCoordinates map(S object);
}
