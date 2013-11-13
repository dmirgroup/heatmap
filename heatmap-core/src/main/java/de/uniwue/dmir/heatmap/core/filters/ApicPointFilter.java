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
package de.uniwue.dmir.heatmap.core.filters;

import java.awt.geom.Path2D;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.ApicPoint;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;

@AllArgsConstructor
public class ApicPointFilter
extends AbstractConfigurableFilter<ApicPoint, ApicOverallTile> {

	/**
	 * Half an hour in milliseconds.
	 */
	public static final long HALF_AN_HOUR = 60 * 30 * 1000;
	
	public static final double POINTS = 1;
	
	public static final Date DEFAULT_TIMESTAMP = new Date(95601600000L);
	public static final String DEFAULT_GROUP = "NOT ASSIGNED";
	
	public static final String GEO_PROVIDER_NONE = "none";
	public static final String GEO_PROVIDER_IP = "ip";
	public static final String GEO_PROVIDER_PHONE_GPS = "gps";
	public static final String GEO_PROVIDER_PHONE_NETWORK = "network";
	public static final String GEO_PROVIDER_SENSORBOX_GPS = "sensorbox";

	private Date minimumTimestamp;

	private IMapper<ApicPoint, String> pointToGroupMapper;

	// to city mappers

	private IMapper<String, String> groupToCityMapper;
	private IMapper<ApicPoint, String> pointToCityMapper;

	// city relevant data

	private IMapper<String, IMapProjection> cityToMapProjectionMapper;
	private IMapper<String, GeoPolygon> cityToGeoPolygonMapper;

	@Override
	public void filter(
			ApicPoint dataPoint, 
			ApicOverallTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		FilterResult r = new FilterResult();
		
		// try to associate point with group and city; both are either null or set at the same time
		
		this.setCityAndGroup(dataPoint, r);
		
		// check if timestamp and location are given
		
		r.hasTimestamp =
				dataPoint.getTimestampRecorded().getTime() > 0
				&& dataPoint.getTimestampRecorded().getTime() != DEFAULT_TIMESTAMP.getTime();
		
		r.hasLocation =
				!dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_NONE);
				
		// check if point is in game time
		
		r.inGameTimestamp =
				r.isHasTimestamp()
				&& dataPoint.getTimestampRecorded().getTime() > this.minimumTimestamp.getTime();
		
		// incorporate point into city and group statistics and tiles
		if (r.city != null) {

			// get city tile
			
			r.cityTile = tile.cityTiles.get(r.city);
			
			// create city tile, if it does not exist
			if (r.cityTile == null) {
				r.cityTile = new ApicCityTile();
				r.cityTile.geoBoundingBox = 
						this.cityToGeoPolygonMapper.map(r.city).getGeoBoundingBox();
				tile.cityTiles.put(r.city, r.cityTile);
			}
			
			// get group tile
			
			r.groupTile = r.cityTile.groupTiles.get(r.group);
			
			// create group tile if it does not exist
			if (r.groupTile == null) {
				r.groupTile = new ApicGroupTile();
				r.cityTile.groupTiles.put(r.group, r.groupTile);
			}
			
			// check if point is in area
			
			Path2D cityPath = this.cityToGeoPolygonMapper.map(r.city).getPath2DLonLat();
			
			GeoCoordinates geoCoordinates = dataPoint.getGeoCoordinates();
			
			r.inGameLocation = r.hasLocation 
					&& cityPath.contains(
							geoCoordinates.getLongitude(), 
							geoCoordinates.getLatitude());
			
			// adding in game point 
			
			if (r.inGameTimestamp && r.inGameLocation) {
				
				IMapProjection cityProjection = 
						this.cityToMapProjectionMapper.map(r.city);
				
				RelativeCoordinates relativeCoordinates = 
						cityProjection.fromGeoToRelativeCoordinates(
								geoCoordinates, 
								null); // only one tile coordinate anyways

				// setting pixel for points grids (city and group)

				r.pointResultCity = this.addPointToGrid(
								dataPoint, 
								relativeCoordinates, 
								r.cityTile.getPixels());
				
				r.pointResultGroup = this.addPointToGrid(
								dataPoint, 
								relativeCoordinates, 
								r.groupTile.getPixels());
				
				
			} 
			

		}
		updateStatistics(dataPoint, tile, r);
		
	}

	private void updateStatistics(
			ApicPoint dataPoint, 
			ApicOverallTile tile,
			FilterResult r) {

		tile.measurementCount ++;
		
		// data point has an error
		if (!r.hasLocation || !r.hasTimestamp) {
			tile.measurementCountError ++;
		}
		
		
		// city given (including group)
		if (r.city != null) { 

			// measurements
			
			tile
				.measurementCountCity ++;
			r.cityTile
				.measurementCount ++;
			r.groupTile
				.measurementCount ++;
			
			// time

			if (r.cityTile.lastMeasurement.getTime() < dataPoint.getTimestampRecorded().getTime()) {
				r.cityTile.lastMeasurement = dataPoint.getTimestampRecorded();
			}
			
			if (r.groupTile.lastMeasurement.getTime() < dataPoint.getTimestampRecorded().getTime()) {
				r.groupTile.lastMeasurement = dataPoint.getTimestampRecorded();
			}
			
			// data point is valid
			if (r.hasLocation && r.hasTimestamp) {
				
				// data point is "in game"
				if (r.inGameLocation && r.inGameTimestamp) {
					
					// measurements
					
					tile
						.measurementCountInGame ++;
					r.cityTile
						.measurementCountInGame ++;
					r.groupTile
						.measurementCountInGame ++;
					
					// points
					
					if (r.pointResultCity.pointReceived) {
						tile.measurementCountInGamePointsCities ++;
						r.cityTile.measurementCountInGamePoints ++;

						if (r.cityTile.lastMeasurementInGamePoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.cityTile.lastMeasurementInGamePoint = dataPoint.getTimestampRecorded();
						}
					}
					
					if (r.pointResultGroup.pointReceived) {
						tile.measurementCountInGamePointsGroups ++;
						r.groupTile.measurementCountInGamePoints ++;

						if (r.groupTile.lastMeasurementInGamePoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.groupTile.lastMeasurementInGamePoint = dataPoint.getTimestampRecorded();
						}
					}
					
					// pixels
					
					if (r.pointResultCity.isNewPixel()) {
						r.cityTile.pixelCountInGame ++;

						if (r.cityTile.lastMeasurementInGamePixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.cityTile.lastMeasurementInGamePixel = dataPoint.getTimestampRecorded();
						}
					}
					
					if (r.pointResultGroup.isNewPixel()) {
						r.groupTile.pixelCountInGame ++;
						
						if (r.groupTile.lastMeasurementInGamePixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.groupTile.lastMeasurementInGamePixel = dataPoint.getTimestampRecorded();
						}
					}
					
					// time
					
					if (r.cityTile.lastMeasurementInGame.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementInGame = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementInGame.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementInGame = dataPoint.getTimestampRecorded();
					}
					
					
				// data point is "out game"
				} else {
					
					// measurements
					
					tile
						.measurementCountOutGame ++;
					r.cityTile
						.measurementCountOutGame ++;
					r.groupTile
						.measurementCountOutGame ++;

					// time
					
					if (r.cityTile.lastMeasurementOutGame.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementOutGame = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementOutGame.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementOutGame = dataPoint.getTimestampRecorded();
					}
				}
				
			// data point has an error
			} else {
				
				// measurements
				
				tile
					.measurementCountCityError ++;
				r.cityTile
					.measurementCountError ++;
				r.groupTile
					.measurementCountError ++;
				
				// time
				
				if (r.cityTile.lastMeasurementError.getTime() < dataPoint.getTimestampRecorded().getTime()) {
					r.cityTile.lastMeasurementError = dataPoint.getTimestampRecorded();
				}
				
				if (r.groupTile.lastMeasurementError.getTime() < dataPoint.getTimestampRecorded().getTime()) {
					r.groupTile.lastMeasurementError = dataPoint.getTimestampRecorded();
				}
			}
			
		// no city is given
		} else {
			
			tile.measurementCountNoCity ++;
			
			// data point is valid
			if (r.hasLocation && r.hasTimestamp) {
				
				// nothing to do at the moment
				
				
				
			// data point has an error
			} else {
				tile.measurementCountNoCityError ++;
			}
		}
		
		
//		// overall
//		tile.getStatistics().measurements ++;
//
//		if (dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_NONE)) {
//			tile.getStatistics().geoProviderNone ++;
//		} else if (dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_IP)) {
//			tile.getStatistics().geoProviderIp ++;
//		} else if (dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_PHONE_NETWORK)) {
//			tile.getStatistics().geoProviderPhoneNetwork ++;
//		} else if (dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_PHONE_GPS)) {
//			tile.getStatistics().geoProviderPhoneGps ++;
//		} else if (dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_SENSORBOX_GPS)) {
//			tile.getStatistics().geoProviderSensorboxGps ++;
//		}
//		
//		if (dataPoint.getGeoCoordinates().getLongitude() == 0 
//				&& dataPoint.getGeoCoordinates().getLatitude() == 0) {
//			tile.getStatistics().geoZeroCoordinates ++;
//		}
	}
	
	public void setCityAndGroup(ApicPoint dataPoint, FilterResult filterResult) {
		
		// try to get group from data point
		String group = this.pointToGroupMapper.map(dataPoint);

		// If a group is given, we infer the city from the group.
		// If no group has given we try to infer the city from the data point.
		String city; 
		if (group != null) { 
			city = this.groupToCityMapper.map(group);
		} else { // try to extract city from 
			city = this.pointToCityMapper.map(dataPoint);
		}
		
		// setting group to default group if city is given, but no group was found
		if (city != null && group == null) {
			group = DEFAULT_GROUP;
		}
		
		filterResult.setCity(city);
		filterResult.setGroup(group);
		
	}
	
	public PointResult addPointToGrid(
			ApicPoint dataPoint,
			RelativeCoordinates relativeCoordinates,
			Map<RelativeCoordinates, PointSize> pixels) {

		PointSize groupTilePixel = pixels.get(relativeCoordinates);
		
		if (groupTilePixel == null) {
			
			// create new group tile pixel if no pixel exists yet
			
			groupTilePixel = new PointSize();
			groupTilePixel.setCoordinates(relativeCoordinates);
			groupTilePixel.setMaxDate(dataPoint.getTimestampRecorded());
			groupTilePixel.setPoints(POINTS);
			groupTilePixel.setSize(1);

			pixels.put(
					relativeCoordinates, 
					groupTilePixel);
			
			return new PointResult(true, true);
			
		} else {

			// increase size
			groupTilePixel.setSize(groupTilePixel.getSize() + 1);
			
			// calculate time difference between current data point and last counted measurement
			long timeDifference = 
					dataPoint.getTimestampRecorded().getTime() 
					- groupTilePixel.getMaxDate().getTime();
			
			// if last counted measurement is old enough, 
			// then update points and timestamp
			if (timeDifference > HALF_AN_HOUR) {
				
				double newPoints = groupTilePixel.getPoints() + POINTS;
				groupTilePixel.setPoints(newPoints);
				groupTilePixel.setMaxDate(dataPoint.getTimestampRecorded());
				
				return new PointResult(false, true);
			} else {
				return new PointResult(false, false);
			}
		}
	}

	@Data
	public static class FilterResult {
		private String city;
		private String group;
		private boolean hasTimestamp;
		private boolean hasLocation;
		private boolean inGameTimestamp;
		private boolean inGameLocation;
		private ApicCityTile cityTile;
		private ApicGroupTile groupTile;
		private PointResult pointResultCity;
		private PointResult pointResultGroup;
	}
	
	@Data
	@AllArgsConstructor
	public static class PointResult {
		private boolean newPixel;
		private boolean pointReceived;
	}
	
	@Data
	public static final class ApicStatistics {
		
		private int measurements = 0;

		private int missingTimestamp = 0;
		
		private int geoProviderNone = 0;
		private int geoProviderIp = 0;
		private int geoProviderPhoneGps = 0;
		private int geoProviderPhoneNetwork = 0;
		private int geoProviderSensorboxGps = 0;
		
		private int geoZeroCoordinates = 0;
		
	}

	@Data
	public static final class ApicOverallTile {

//		private ApicStatistics statistics;

		private Map<String, ApicCityTile> cityTiles = 
				new HashMap<String, ApicCityTile>();
		
		private int measurementCount;
		private int measurementCountError;

		private int measurementCountCity;
		private int measurementCountCityError;
		private int measurementCountOutGame;
		private int measurementCountInGame;

		private int measurementCountInGamePointsCities;
		private int measurementCountInGamePointsGroups;

		private int measurementCountNoCity;
		private int measurementCountNoCityError;
		
	}

	@Data
	public static class ApicGroupTile {

		@JsonIgnore
		private Map<RelativeCoordinates, PointSize> pixels =
				new HashMap<RelativeCoordinates, PointSize>();

		// measurements
		
		protected int measurementCount;

		protected int measurementCountError;
		protected int measurementCountOutGame;
		protected int measurementCountInGame;

		protected int measurementCountInGamePoints;
		
		protected int pixelCountInGame;
		
		// time
		
		protected Date lastMeasurement = new Date(0);
		
		protected Date lastMeasurementError = new Date(0);
		protected Date lastMeasurementOutGame = new Date(0);
		protected Date lastMeasurementInGame = new Date(0);

		protected Date lastMeasurementInGamePoint = new Date(0);
		protected Date lastMeasurementInGamePixel = new Date(0);
		
//		// relative coordinates
//
//		private long minRelativeX = Long.MAX_VALUE;
//		private long minRelativeY = Long.MAX_VALUE;
//		private long maxRelativeX = Long.MIN_VALUE;
//		private long maxRelativeY = Long.MIN_VALUE;
		
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class ApicCityTile extends ApicGroupTile {
		
		private GeoBoundingBox geoBoundingBox;
		
		private Map<String, ApicGroupTile> groupTiles =
				new HashMap<String, ApicGroupTile>();
		
	}
	
}
