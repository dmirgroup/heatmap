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

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.types.ValuePixel;
import de.uniwue.dmir.heatmap.core.filters.access.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.PointSize;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;

public class PointFilter<T>
extends AbstractConfigurableFilter<ValuePixel, T> {

	/**
	 * Half an hour in milliseconds.
	 */
	public static final long HALF_AN_HOUR = 60 * 30 * 1000;
	
	public static final double POINTS = 1;
	
	private IPixelAccess<PointSize, T> pixelAccess;
	
	public PointFilter(	IPixelAccess<PointSize, T> pixelAccess) {
		this(pixelAccess, null, null);
	}
	
	public PointFilter(
			IPixelAccess<PointSize, T> pixelAccess,
			GeoPolygon geoPolygon,
			TileSize tileSize) {
		
		this.pixelAccess = pixelAccess;
		
	}
	
	@Override
	public void filter(
			ValuePixel dataPoint, 
			T tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		PointSize pointSize = this.pixelAccess.get(
				dataPoint.getCoordinates(), 
				tile, 
				tileSize);
		
		if (pointSize == null) {
		
			// create new point if not point has been created so far
			
			pointSize = new PointSize();
			pointSize.setCoordinates(dataPoint.getCoordinates());
			pointSize.setMaxDate(dataPoint.getTimestamp());
			pointSize.setPoints(POINTS);
			pointSize.setSize(1);
			
			// set new point
			this.pixelAccess.set(
					pointSize, 
					dataPoint.getCoordinates(), 
					tile, 
					tileSize);
			
		} else {
			
			// calculate time difference between current data point and last counted measurement
			long timeDifference = 
					dataPoint.getTimestamp().getTime() 
					- pointSize.getMaxDate().getTime();
			
			// if last counted measurement is old enough, 
			// then update points and timestamp
			if (timeDifference > HALF_AN_HOUR) {
			
				
				double newPoints = pointSize.getPoints() + POINTS;
				pointSize.setPoints(newPoints);
				pointSize.setMaxDate(dataPoint.getTimestamp());
			}

			// increase size
			pointSize.setSize(pointSize.getSize() + 1);
			
		}

	}
	

}
