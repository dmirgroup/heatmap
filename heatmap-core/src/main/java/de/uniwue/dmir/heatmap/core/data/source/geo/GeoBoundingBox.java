package de.uniwue.dmir.heatmap.core.data.source.geo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoBoundingBox {
	private GeoCoordinates northWest;
	private GeoCoordinates southEast;
}