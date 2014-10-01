/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.point.sources.geo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GeoBoundingBox {
	
	private GeoCoordinates min; // south west
	private GeoCoordinates max; // north east
	
	public GeoBoundingBox(GeoCoordinates min, GeoCoordinates max) {
		super();
		this.min = min;
		this.max = max;
	}
	
	public GeoBoundingBox(double minLon, double minLat, double maxLon, double maxLat) {
		this.min = new GeoCoordinates(minLon, minLat);
		this.max = new GeoCoordinates(maxLon, maxLat);
	}

	public GeoBoundingBoxCorners getCorners() {
		return new GeoBoundingBoxCorners(
				this.min.copy(), 
				new GeoCoordinates(this.max.getLongitude(), this.min.getLatitude()),
				this.max.copy(), 
				new GeoCoordinates(this.min.getLongitude(), this.max.getLatitude()));
	}
	
	@Data
	@AllArgsConstructor
	public static class GeoBoundingBoxCorners {
		private GeoCoordinates bottomLeft;
		private GeoCoordinates bottomRight;
		private GeoCoordinates topRight;
		private GeoCoordinates topLeft;
	}
}