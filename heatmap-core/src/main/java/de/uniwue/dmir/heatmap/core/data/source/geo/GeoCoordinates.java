package de.uniwue.dmir.heatmap.core.data.source.geo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A tuple consisting of longitude and latitude.
 * 
 * @author Martin Becker
 */
@Data
@AllArgsConstructor
public class GeoCoordinates {
	private double longitude;
	private double latitude;
}