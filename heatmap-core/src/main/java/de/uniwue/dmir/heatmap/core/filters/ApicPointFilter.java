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
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.ApicPoint;
import de.uniwue.dmir.heatmap.core.filters.ApicPointFilter.ApicOverallTile;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;

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
	private IMapper<String, Path2D> cityToAreaMapper;

	@Override
	public void filter(
			ApicPoint dataPoint, 
			ApicOverallTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {

		// try to associate point with group and city; both may be null in the end
		
		String group = this.pointToGroupMapper.map(dataPoint);
		String city; 
		if (group != null) {
			city = this.groupToCityMapper.map(group);
		} else {
			city = this.pointToCityMapper.map(dataPoint);
		}
		
		// setting group to default group if city is given, but no group was found
		
		if (city != null) {
			group = DEFAULT_GROUP;
		}
		
		// check if timestamp and location are given
		
		boolean hasTimestamp = 
				dataPoint.getTimestampRecorded().getTime() > 0
				&& dataPoint.getTimestampRecorded().getTime() != DEFAULT_TIMESTAMP.getTime();
		
		boolean hasLocation = !dataPoint.getGeoProvider().toLowerCase().equals(GEO_PROVIDER_NONE);
				
		// check if point is in game time
		
		boolean inGameTime = hasTimestamp 
				&& dataPoint.getTimestampRecorded().getTime() > this.minimumTimestamp.getTime();
		
		// incorporate point into city and group statistics and tiles
		if (city != null) {

			// get city tile
			
			ApicCityTile cityTile = tile.cityTiles.get(city);
			
			// create city tile, if it does not exist
			if (cityTile == null) {
				cityTile = new ApicCityTile();
				tile.cityTiles.put(city, cityTile);
			}
			
			// get group tile
			
			ApicGroupTile groupTile = cityTile.groupTiles.get(group);
			
			// create group tile if it does not exist
			if (groupTile == null) {
				groupTile = new ApicGroupTile();
				cityTile.groupTiles.put(group, groupTile);
			}
			
			// check if point is in area
			
			Path2D cityPath = this.cityToAreaMapper.map(city);
			
			GeoCoordinates geoCoordinates = dataPoint.getGeoCoordinates();
			
			boolean inGameLocation = hasLocation 
					&& cityPath.contains(
							geoCoordinates.getLongitude(), 
							geoCoordinates.getLatitude());
			
			// adding point 
			
			if (inGameTime && inGameLocation) {
				
				IMapProjection cityProjection = 
						this.cityToMapProjectionMapper.map(city);
				
				RelativeCoordinates relativeCoordinates = 
						cityProjection.fromGeoToRelativeCoordinates(
								geoCoordinates, 
								null); // only one tile coordinate anyways

				// setting pixel for points grids (city and group)

				boolean countedForCity = 
						this.addPointToGrid(
								dataPoint, 
								relativeCoordinates, 
								cityTile.getPixels());
				
				boolean countedForGroup =
						this.addPointToGrid(
								dataPoint, 
								relativeCoordinates, 
								groupTile.getPixels());
				
				// update ingame measurement count

				
				if (countedForCity) {
					tile.measurementCountInGamePointsCities ++;
					cityTile.measurementCountInGamePoints ++;
				}
				
				if (countedForGroup) {
					tile.measurementCountInGamePointsGroups ++;
					groupTile.measurementCountInGamePoints ++;
				}
				
			} 
			
			// update city and group measurement count

			cityTile.measurementCount ++;
			groupTile.measurementCount ++;
			
			if (hasLocation && hasTimestamp) {
				if (inGameLocation && inGameTime) {
					tile.measurementCountInGame ++;
					cityTile.measurementCountInGame ++;
					groupTile.measurementCountInGame ++;
				} else {
					tile.measurementCountOutGame ++;
					cityTile.measurementCountOutGame ++;
					groupTile.measurementCountOutGame ++;
				}
			} else {
				cityTile.measurementCountError ++;
				groupTile.measurementCountError ++;
			}
		}
		
		// update overall measurement count
		tile.measurementCount ++;
		if (!hasLocation || !hasTimestamp) {
			tile.measurementCountError ++;
		}
		
		if (city != null) {
			tile.measurementCountCity ++;
			if (!hasLocation || !hasTimestamp) {
				tile.measurementCountCityError ++;
			}
		} else {
			tile.measurementCountNoCity ++;
			if (!hasLocation || !hasTimestamp) {
				tile.measurementCountNoCityError ++;
			}
		}
		
		// incorporate point into overall statistics

		
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
	
	public boolean addPointToGrid(
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
			
			return true;
			
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
				
				return true;
			} else {
				return false;
			}
		}
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
	public static final class ApicGroupTile {

		@JsonIgnore
		private Map<RelativeCoordinates, PointSize> pixels =
				new HashMap<RelativeCoordinates, PointSize>();

		private int measurementCount;

		private int measurementCountError;
		private int measurementCountOutGame;
		private int measurementCountInGame;

		private int measurementCountInGamePoints;
	}

	@Data
	public static final class ApicCityTile {
		
		private Map<RelativeCoordinates, PointSize> pixels =
				new HashMap<RelativeCoordinates, PointSize>();
		
		private Map<String, ApicGroupTile> groupTiles =
				new HashMap<String, ApicGroupTile>();
		
		private int measurementCount;
		
		private int measurementCountError;
		private int measurementCountOutGame;
		private int measurementCountInGame;
		
		private int measurementCountInGamePoints;
		
	}
	
}
