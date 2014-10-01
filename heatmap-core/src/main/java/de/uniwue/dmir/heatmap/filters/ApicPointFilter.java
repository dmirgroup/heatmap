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
package de.uniwue.dmir.heatmap.filters;

import java.awt.geom.Path2D;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.point.types.geo.ApicGeoPoint;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.tiles.pixels.PointSizePixel;
import de.uniwue.dmir.heatmap.util.GeoPolygon;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

public class ApicPointFilter
extends AbstractConfigurableFilter<ApicGeoPoint, ApicOverallTile> {

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

	private boolean limitToCityBounds;
	
	private Date minimumTimestampRecorded;
	private Date maximumTimestampRecorded;

	private IMapper<ApicGeoPoint, String> pointToGroupMapper;

	// to city mappers

	private IMapper<String, String> groupToCityMapper;
	private IMapper<ApicGeoPoint, String> pointToCityMapper;

	// city relevant data

	private IMapper<String, IMapProjection> cityToMapProjectionMapper;
	private IMapper<String, GeoPolygon> cityToGeoPolygonMapper;

	public ApicPointFilter(
			ITileSizeProvider tileSizeProvider,
			boolean limitToCityBounds, Date minimumTimestampRecorded,
			Date maximumTimestampRecorded,
			IMapper<ApicGeoPoint, String> pointToGroupMapper,
			IMapper<String, String> groupToCityMapper,
			IMapper<ApicGeoPoint, String> pointToCityMapper,
			IMapper<String, IMapProjection> cityToMapProjectionMapper,
			IMapper<String, GeoPolygon> cityToGeoPolygonMapper) {
		
		super(tileSizeProvider);
		
		this.limitToCityBounds = limitToCityBounds;
		this.minimumTimestampRecorded = minimumTimestampRecorded;
		this.maximumTimestampRecorded = maximumTimestampRecorded;
		this.pointToGroupMapper = pointToGroupMapper;
		this.groupToCityMapper = groupToCityMapper;
		this.pointToCityMapper = pointToCityMapper;
		this.cityToMapProjectionMapper = cityToMapProjectionMapper;
		this.cityToGeoPolygonMapper = cityToGeoPolygonMapper;
	}
	
	@Override
	public void filter(
			ApicGeoPoint dataPoint, 
			ApicOverallTile tile, 
			TileCoordinates tileCoordinates) {
		
		FilterResult r = new FilterResult();
		
		// try to associate point with group and city; both are either null or set at the same time
		
		this.setCityAndGroup(dataPoint, r);
		
		// check if timestamp and location are given
		
		r.hasTimestamp =
				dataPoint.getTimestampRecorded().getTime() > 0
				&& dataPoint.getTimestampRecorded().getTime() != DEFAULT_TIMESTAMP.getTime();
		
		r.hasLocation =
				dataPoint.getGeoCoordinates() != null 
				&& !dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_NONE);
				
		// check if point is in game time
		
		r.inGameTimestamp =
				r.isHasTimestamp()
				&& dataPoint.getTimestampRecorded().getTime() >= this.minimumTimestampRecorded.getTime()
				&& dataPoint.getTimestampRecorded().getTime() <= this.maximumTimestampRecorded.getTime();
		
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
			
			r.inAreaLocation = r.hasLocation 
					&& cityPath.contains(
							geoCoordinates.getLongitude(), 
							geoCoordinates.getLatitude());
			
			// adding in game point
			
			if (r.inGameTimestamp && (!this.limitToCityBounds || r.inAreaLocation)) {
				
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
			ApicGeoPoint dataPoint, 
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
				
				// within game time
				if (r.inGameTimestamp) {
					
					r.cityTile.measurementCountInTime ++;
					r.groupTile.measurementCountInTime ++;
					
					if (r.cityTile.lastMeasurementInTime.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementInTime = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementInTime.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementInTime = dataPoint.getTimestampRecorded();
					}
					
				}
				
				// within in area
				if (r.inAreaLocation) {
					
					r.cityTile.measurementCountInArea ++;
					r.groupTile.measurementCountInArea ++;
					
					if (r.cityTile.lastMeasurementInArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementInArea = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementInArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementInArea = dataPoint.getTimestampRecorded();
					}
				}
				
				// within time and area (this may be equal to "in game" if points outside area are not part of the game)
				if (r.inGameTimestamp && r.inAreaLocation) {
					
					r.cityTile.measurementCountInTimeInArea ++;
					r.groupTile.measurementCountInTimeInArea ++;
					
					if (r.cityTile.lastMeasurementInTimeInArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementInTimeInArea = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementInTimeInArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementInTimeInArea = dataPoint.getTimestampRecorded();
					}
				}
				
				// within time but outside area 
				if (r.inGameTimestamp && !r.inAreaLocation) {
					
					r.cityTile.measurementCountInTimeOutArea ++;
					r.groupTile.measurementCountInTimeOutArea ++;
					
					if (r.cityTile.lastMeasurementInTimeOutArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.cityTile.lastMeasurementInTimeOutArea = dataPoint.getTimestampRecorded();
					}
					
					if (r.groupTile.lastMeasurementInTimeOutArea.getTime() < dataPoint.getTimestampRecorded().getTime()) {
						r.groupTile.lastMeasurementInTimeOutArea = dataPoint.getTimestampRecorded();
					}
				}
				
				// data point is "in game"
				if (r.inGameTimestamp && (!this.limitToCityBounds || r.inAreaLocation)) {
					
					// measurements
					
					tile
						.measurementCountInGame ++;
					r.cityTile
						.measurementCountInGame ++;
					r.groupTile
						.measurementCountInGame ++;
					
					// points
					
					if (r.pointResultCity.pointReceived) {
						
						// points
						
						tile.measurementCountInGamePointsCities ++;
						r.cityTile.measurementCountInGamePoints ++;
						
						if (r.inAreaLocation) {
							r.cityTile.measurementCountInAreaPoints ++;
						} else {
							r.cityTile.measurementCountOutAreaPoints ++;
						}

						// time
						
						if (r.cityTile.lastMeasurementInGamePoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.cityTile.lastMeasurementInGamePoint = dataPoint.getTimestampRecorded();
						}
						
						if (r.inAreaLocation) {
							if (r.cityTile.lastMeasurementInAreaPoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.cityTile.lastMeasurementInAreaPoint = dataPoint.getTimestampRecorded();
							}
						} else {
							if (r.cityTile.lastMeasurementOutAreaPoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.cityTile.lastMeasurementOutAreaPoint = dataPoint.getTimestampRecorded();
							}
						}
					}
					
					if (r.pointResultGroup.pointReceived) {
						
						// points
						
						tile.measurementCountInGamePointsGroups ++;
						r.groupTile.measurementCountInGamePoints ++;

						if (r.inAreaLocation) {
							r.groupTile.measurementCountInAreaPoints ++;
						} else {
							r.groupTile.measurementCountOutAreaPoints ++;
						}
						
						// time
						
						if (r.groupTile.lastMeasurementInGamePoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.groupTile.lastMeasurementInGamePoint = dataPoint.getTimestampRecorded();
						}
						
						if (r.inAreaLocation) {
							if (r.groupTile.lastMeasurementInAreaPoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.groupTile.lastMeasurementInAreaPoint = dataPoint.getTimestampRecorded();
							}
						} else {
							if (r.groupTile.lastMeasurementOutAreaPoint.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.groupTile.lastMeasurementOutAreaPoint = dataPoint.getTimestampRecorded();
							}
						}
					}
					
					// pixels
					
					if (r.pointResultCity.isNewPixel()) {
						
						// pixels
						
						r.cityTile.pixelCountInGame ++;
						
						if (r.inAreaLocation) {
							r.cityTile.pixelCountInArea ++;
						} else {
							r.cityTile.pixelCountOutArea ++;
						}
						
						// time

						if (r.cityTile.lastMeasurementInGamePixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.cityTile.lastMeasurementInGamePixel = dataPoint.getTimestampRecorded();
						}
						
						if (r.inAreaLocation) {
							if (r.cityTile.lastMeasurementInAreaPixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.cityTile.lastMeasurementInAreaPixel = dataPoint.getTimestampRecorded();
							}
						} else {
							if (r.cityTile.lastMeasurementOutAreaPixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.cityTile.lastMeasurementOutAreaPixel = dataPoint.getTimestampRecorded();
							}
						}
						
						
					}
					
					if (r.pointResultGroup.isNewPixel()) {
						
						// pixel
						
						r.groupTile.pixelCountInGame ++;
						
						if (r.inAreaLocation) {
							r.groupTile.pixelCountInArea ++;
						} else {
							r.groupTile.pixelCountOutArea ++;
						}
						
						// time
						
						if (r.groupTile.lastMeasurementInGamePixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
							r.groupTile.lastMeasurementInGamePixel = dataPoint.getTimestampRecorded();
						}
						
						if (r.inAreaLocation) {
							if (r.groupTile.lastMeasurementInAreaPixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.groupTile.lastMeasurementInAreaPixel = dataPoint.getTimestampRecorded();
							}
						} else {
							if (r.groupTile.lastMeasurementOutAreaPixel.getTime() < dataPoint.getTimestampRecorded().getTime()) {
								r.groupTile.lastMeasurementOutAreaPixel = dataPoint.getTimestampRecorded();
							}
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
	
	public void setCityAndGroup(ApicGeoPoint dataPoint, FilterResult filterResult) {
		
		// try to get group from data point
		String group = this.pointToGroupMapper.map(dataPoint);

		// If a group is given, we infer the city from the group.
		// If no group has given we try to infer the city from the data point.
		String city = null; 
		if (group != null) { 
			city = this.groupToCityMapper.map(group);
		} 
		
		if (city == null) { // try to extract city from point
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
			ApicGeoPoint dataPoint,
			RelativeCoordinates relativeCoordinates,
			Map<RelativeCoordinates, PointSizePixel> pixels) {

		PointSizePixel groupTilePixel = pixels.get(relativeCoordinates);
		
		if (groupTilePixel == null) {
			
			// create new group tile pixel if no pixel exists yet
			
			groupTilePixel = new PointSizePixel();
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
		private boolean inAreaLocation;
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
		private Map<RelativeCoordinates, PointSizePixel> pixels =
				new HashMap<RelativeCoordinates, PointSizePixel>();

		// measurements
		
		protected int measurementCount;

		protected int measurementCountError;
		protected int measurementCountOutGame;
		protected int measurementCountInGame;

		protected int measurementCountInTime;
		protected int measurementCountOutTime;
		protected int measurementCountInArea;
		protected int measurementCountOutArea;
		protected int measurementCountInTimeInArea;
		protected int measurementCountInTimeOutArea;

		protected int measurementCountInAreaPoints;
		protected int measurementCountOutAreaPoints;
		protected int measurementCountInGamePoints;

		protected int pixelCountInArea;
		protected int pixelCountOutArea;
		protected int pixelCountInGame;
		
		// time
		
		protected Date lastMeasurement = new Date(0);
		
		protected Date lastMeasurementError = new Date(0);
		protected Date lastMeasurementOutGame = new Date(0);
		protected Date lastMeasurementInGame = new Date(0);

		protected Date lastMeasurementInTime = new Date(0);
		protected Date lastMeasurementOutTime = new Date(0);
		protected Date lastMeasurementInArea = new Date(0);
		protected Date lastMeasurementOutArea = new Date(0);
		protected Date lastMeasurementInTimeInArea = new Date(0);
		protected Date lastMeasurementInTimeOutArea = new Date(0);
		
		protected Date lastMeasurementInAreaPoint = new Date(0);
		protected Date lastMeasurementOutAreaPoint = new Date(0);
		protected Date lastMeasurementInGamePoint = new Date(0);

		protected Date lastMeasurementInAreaPixel = new Date(0);
		protected Date lastMeasurementOutAreaPixel = new Date(0);
		protected Date lastMeasurementInGamePixel = new Date(0);
		
//		// relative coordinates
//
//		protected RelativeCoordinates min = 
//				new RelativeCoordinates(Integer.MAX_VALUE, Integer.MAX_VALUE);
//		
//		protected RelativeCoordinates max =
//			new RelativeCoordinates(Integer.MIN_VALUE, Integer.MIN_VALUE);
		
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class ApicCityTile extends ApicGroupTile {
		
		private GeoBoundingBox geoBoundingBox;
		
		private Map<String, ApicGroupTile> groupTiles =
				new HashMap<String, ApicGroupTile>();
		
	}
	
}
