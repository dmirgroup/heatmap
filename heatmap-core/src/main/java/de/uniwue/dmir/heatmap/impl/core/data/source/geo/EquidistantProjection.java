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
package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.IDistanceFunction;

/**
 * @author Martin Becker
 */
public class EquidistantProjection implements IMapProjection {

	private GeoBoundingBox geoBoundingBox;
	private TileSize tileSize;
	
	private double diffLongitude;
	private double diffLatitude;
	
	
	public EquidistantProjection(GeoBoundingBox geoBoundingBox, TileSize tileSize) {
		
		this.geoBoundingBox = geoBoundingBox;
		this.tileSize = tileSize;
		
		double width = 
				(geoBoundingBox.getSouthEast().getLongitude() 
				- geoBoundingBox.getNorthWest().getLongitude());

		double height = 
				geoBoundingBox.getNorthWest().getLatitude() 
				- geoBoundingBox.getSouthEast().getLatitude();
		
		this.diffLongitude = width / tileSize.getWidth();
		this.diffLatitude = height / tileSize.getHeight();
		
	}
	
	public static TileSize getTileSize(
			int cellWidthMeters, 
			int cellHeightMeters,
			boolean northReference,
			GeoBoundingBox geoBoundingBox,
			IDistanceFunction<GeoCoordinates> distanceFunction) {

		GeoCoordinates northWest = geoBoundingBox.getNorthWest();
		GeoCoordinates southEast = geoBoundingBox.getSouthEast();
	
		GeoCoordinates southWest = new GeoCoordinates(northWest.getLongitude(), southEast.getLatitude());
		GeoCoordinates northEast = new GeoCoordinates(southEast.getLongitude(), northWest.getLatitude());
		
		double heightMeters = distanceFunction.distance(northWest, southWest);

		double widthMeters;
		if (northReference) {
			widthMeters = distanceFunction.distance(northWest, northEast);
		} else {
			widthMeters = distanceFunction.distance(southWest, southEast);
		}
		
		int width = (int) widthMeters / cellWidthMeters;
		int height = (int) heightMeters / cellHeightMeters;
		
		return new TileSize(width, height);
	}
	
	
	@Override
	public RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates, 
			TileCoordinates tileCoordinates) {
		
		double longitude =
				+ geoCoordinates.getLongitude()
				- this.geoBoundingBox.getNorthWest().getLongitude();
		
		double latitude = 
				- geoCoordinates.getLatitude()
				+ this.geoBoundingBox.getNorthWest().getLatitude();
		
//		System.out.println(longitude);
//		System.out.println(latitude);
//		System.out.println("---");
		
		RelativeCoordinates relativeCoordinates = new RelativeCoordinates(
				(int) Math.floor(longitude / this.diffLongitude), 
				(int) Math.floor(latitude / this.diffLatitude));

		return relativeCoordinates;
		
	}
	
	@Override
	public GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates, 
			IFilter<?, ?> filter) {
		
		if (
				tileCoordinates.getX() != 0 
				|| tileCoordinates.getY() != 0 
				|| tileCoordinates.getZoom() != 0) {
			
			throw new IllegalArgumentException(
					"Only coordinates (0,0,0) are allowed.");
		}
		
		return this.geoBoundingBox;
		
	}
	
	@Override
	public List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates,
			int zoom,
			IFilter<?, ?> filter) {
		
		if (zoom != 0) {
			throw new IllegalArgumentException("Only zoom level 0 allowed.");
		}
		
		List<TileCoordinates> tileCoordinates = new ArrayList<TileCoordinates>();

		RelativeCoordinates relativeCoordinates = 
				this.fromGeoToRelativeCoordinates(geoCoordinates, null);
		if (
				relativeCoordinates.getX() - filter.getCenterX() >= 0
				&& relativeCoordinates.getY() - filter.getCenterY() >= 0
				&& relativeCoordinates.getX() + filter.getWidth() - filter.getCenterX() <= tileSize.getWidth()
				&& relativeCoordinates.getY() + filter.getHeight() - filter.getCenterY() <= tileSize.getHeight()) {
			
			tileCoordinates.add(new TileCoordinates(0, 0, 0));
		}
		
		return tileCoordinates;
		
	}

}
