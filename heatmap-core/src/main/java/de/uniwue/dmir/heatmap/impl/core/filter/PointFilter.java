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
package de.uniwue.dmir.heatmap.impl.core.filter;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.filter.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.EquidistantProjection;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.GroupValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.PointSize;

public class PointFilter<T>
extends AbstractConfigurableFilter<GroupValuePixel, T> {

	/**
	 * Half an hour in milliseconds.
	 */
	public static final long HALF_AN_HOUR = 60 * 30 * 1000;
	
	public static final double POINTS = 1;
	
	private IPixelAccess<PointSize, T> pixelAccess;
	
	private GeoPolygon geoPolygon;
	private TileSize tileSize;
	
	private Polygon polygon;
	
	public PointFilter(	IPixelAccess<PointSize, T> pixelAccess) {
		this(pixelAccess, null, null);
	}
	
	public PointFilter(
			IPixelAccess<PointSize, T> pixelAccess,
			GeoPolygon geoPolygon,
			TileSize tileSize) {
		
		this.pixelAccess = pixelAccess;
		
		this.geoPolygon = geoPolygon;
		this.tileSize = tileSize;
		
		if (this.geoPolygon != null && this.tileSize != null) {
			this.preparePolygon();
		} else if (this.geoPolygon == null && this.tileSize == null) {
			// this is OK
		} else {
			throw new IllegalArgumentException(
					"Either both, polygon and tileSize, must be given, or none.");
		}
	}
	
	public void preparePolygon() {

		super.logger.debug("Preparing polygon.");
		
		EquidistantProjection equidistantProjection =
				new EquidistantProjection(
						this.geoPolygon.getGeoBoundingBox(), 
						this.tileSize);

		int length = this.geoPolygon.getP().length / 2;
		
		int[] x = new int[length];
		int[] y = new int[length];
		for (int i = 0; i < length; i++) {
			RelativeCoordinates relativeCoordinates =
					equidistantProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(
									this.geoPolygon.getP()[2 * i + 1], 
									this.geoPolygon.getP()[2 * i + 0]), 
							null);
			
			x[i] = relativeCoordinates.getX();
			y[i] = relativeCoordinates.getY();
		}
		
		this.polygon = new Polygon(x, y, length);
		
		BufferedImage bufferedImage = new BufferedImage(tileSize.getWidth(), tileSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawPolygon(this.polygon);
		try {
			ImageIO.write(bufferedImage, "png", new File("out/test.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.logger.debug("Done preparing polygon.");
	}
	
	@Override
	public void filter(
			GroupValuePixel dataPoint, 
			T tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		// filter polygon
		if (this.polygon != null && !this.polygon.contains(
				dataPoint.getCoordinates().getX(),
				dataPoint.getCoordinates().getY())) {
			return;
		}
		
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
