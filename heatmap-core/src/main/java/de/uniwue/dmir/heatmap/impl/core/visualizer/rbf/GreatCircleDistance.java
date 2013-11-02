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
package de.uniwue.dmir.heatmap.impl.core.visualizer.rbf;

import lombok.Setter;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;

/**
 * Resources:
 * <ul>
 * <li>http://www.movable-type.co.uk/scripts/latlong.html</li>
 * <li>http://jsperf.com/haversine-vs-spherical-law-of-cosines-vs-equirectangula</li>
 * <li>http://introcs.cs.princeton.edu/java/12types/GreatCircle.java.html</li>
 * <li>http://gis.stackexchange.com/questions/4906/why-is-law-of-cosines-more-preferable-than-haversine-when-calculating-distance-b</li>
 * </ul>
 * @author Martin Becker
 *
 */
public class GreatCircleDistance {
	
	/**
	 * Earth radius in meters.
	 */
	public static final double EARTH_RADIUS = 6371 * 1000;
	
	public static class EquidistantApproximation
	implements IDistanceFunction<GeoCoordinates> {

		@Setter
		private double radius = GreatCircleDistance.EARTH_RADIUS;
		
		@Override
		public double distance(GeoCoordinates o1, GeoCoordinates o2) {

			double lon1 = Math.toRadians(o1.getLongitude());
			double lat1 = Math.toRadians(o1.getLatitude());

			double lon2 = Math.toRadians(o2.getLongitude());
			double lat2 = Math.toRadians(o2.getLatitude());

			double x = (lon2-lon1) * Math.cos((lat1+lat2)/2);
			double y = (lat2-lat1);
			
			// great circle distance in radians
			double c = Math.sqrt(x*x + y*y);
			
			return c * this.radius;
		}
	}
	
	public static class Cosine
	implements IDistanceFunction<GeoCoordinates> {

		@Setter
		private double radius = EARTH_RADIUS;
		
		@Override
		public double distance(GeoCoordinates o1, GeoCoordinates o2) {

			double lon1 = Math.toRadians(o1.getLongitude());
			double lat1 = Math.toRadians(o1.getLatitude());

			double lon2 = Math.toRadians(o2.getLongitude());
			double lat2 = Math.toRadians(o2.getLatitude());

			// great circle distance in radians
			double c = 
					Math.acos(
							Math.sin(lat1) * Math.sin(lat2)
							+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

			return c * this.radius;
		}
	}
	
	public static class Haversine
	implements IDistanceFunction<GeoCoordinates> {

		@Setter
		private double radius = EARTH_RADIUS;
		
		@Override
		public double distance(GeoCoordinates o1, GeoCoordinates o2) {

			double x1 = Math.toRadians(o1.getLatitude());
			double y1 = Math.toRadians(o1.getLongitude());

			double x2 = Math.toRadians(o2.getLatitude());
			double y2 = Math.toRadians(o2.getLongitude());

			double s1 = Math.sin((x2 - x1) / 2);
			double s2 = Math.sin((y2 - y1) / 2);
			
			double a = 	s1 * s1 + s2 * s2 * Math.cos(x1) * Math.cos(x2);

			// great circle distance in radians
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

			return c * this.radius;
		}
		
	}
}
