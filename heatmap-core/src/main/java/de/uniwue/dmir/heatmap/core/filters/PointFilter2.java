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
import java.util.List;
import java.util.Map;

import lombok.Data;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.ApicPoint;
import de.uniwue.dmir.heatmap.core.data.type.GenericExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.filters.PointFilter2.ApicOverallTile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;

public class PointFilter2
extends AbstractConfigurableFilter<GenericExternalData<ApicPoint>, ApicOverallTile> {

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

		private ApicStatistics statistics;
		
		private Map<String, ApicGroupTile> cityTiles; 
		private Map<String, ApicGroupTile> groupTiles; 
		
	}
	
	@Data
	public static final class ApicGroupTile {
		
		private Map<RelativeCoordinates, PointSize> pixels;
		
	}
	
	private IMapper<ApicPoint, String> pointToGroupIdMapper;
	private IMapper<String, String> groupIdToCityMapper;
	
	/**
	 * Half an hour in milliseconds.
	 */
	public static final long HALF_AN_HOUR = 60 * 30 * 1000;
	
	public static final double POINTS = 1;
	
	@Override
	public void filter(
			GenericExternalData<ApicPoint> dataPoint, 
			ApicOverallTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		// overall
		tile.getStatistics().measurements ++;

		if (dataPoint.getData().getGeoProvider().toLowerCase().equals("none")) {
			tile.getStatistics().geoProviderNone ++;
		} else if (dataPoint.getData().getGeoProvider().toLowerCase().equals("ip")) {
			tile.getStatistics().geoProviderIp ++;
		} else if (dataPoint.getData().getGeoProvider().toLowerCase().equals("network")) {
			tile.getStatistics().geoProviderPhoneNetwork ++;
		} else if (dataPoint.getData().getGeoProvider().toLowerCase().equals("gps")) {
			tile.getStatistics().geoProviderPhoneGps ++;
		} else if (dataPoint.getData().getGeoProvider().toLowerCase().equals("sensorbox")) {
			tile.getStatistics().geoProviderSensorboxGps ++;
		}
		
		if (dataPoint.getData().getLongtiude() == 0 && dataPoint.getData().getLatitude() == 0) {
			tile.getStatistics().geoZeroCoordinates ++;
		}
		
		// getting group id and corresponding tile
		
		String group = this.pointToGroupIdMapper.map(dataPoint.getData()); // groupId may be NONE
		ApicGroupTile groupTile = tile.getGroupTiles().get(group);

		String city = this.groupIdToCityMapper.map(group);
		Path2D cityPath = null;
		
		// checking point
		
		// check if point is within city area
		boolean withinCityArea = cityPath.contains(dataPoint.getCoordinates(), get)
		
		// check if point is within game time
		
		// adding point to group tile
		
		PointSize groupTilePixel = groupTile.getPixels().get(dataPoint.getCoordinates());
		
		if (groupTilePixel == null) {
			
			// create new group tile pixel if no pixel exists yet
			
			groupTilePixel = new PointSize();
			groupTilePixel.setCoordinates(dataPoint.getCoordinates());
			groupTilePixel.setMaxDate(dataPoint.getData().getTimestampRecorded());
			groupTilePixel.setPoints(POINTS);
			groupTilePixel.setSize(1);

			groupTile.getPixels().put(
					dataPoint.getCoordinates(), 
					groupTilePixel);
			
		} else {
			
			// calculate time difference between current data point and last counted measurement
			long timeDifference = 
					dataPoint.getData().getTimestampRecorded().getTime() 
					- groupTilePixel.getMaxDate().getTime();
			
			// if last counted measurement is old enough, 
			// then update points and timestamp
			if (timeDifference > HALF_AN_HOUR) {
				
				double newPoints = groupTilePixel.getPoints() + POINTS;
				groupTilePixel.setPoints(newPoints);
				groupTilePixel.setMaxDate(dataPoint.getData().getTimestampRecorded());
			}

			// increase size
			groupTilePixel.setSize(groupTilePixel.getSize() + 1);
			
		}
		
	}
	
	

}
