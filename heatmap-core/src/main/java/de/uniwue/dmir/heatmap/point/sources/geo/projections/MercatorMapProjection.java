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
package de.uniwue.dmir.heatmap.point.sources.geo.projections;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.IZoomLevelMapper;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.ZoomLevelSize;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.tiles.coordinates.PixelCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * 
 * Strongly inspired by <a href="https://github.com/varunpant/GHEAT-JAVA/blob/master/JavaHeatMaps/gheat/src/main/java/gheat/MercatorProjection.java">
 * MercatorProjection class</a> from <a href="http://code.google.com/p/gheat/">GHeat</a>.
 * 
 * @author Martin Becker
 *
 */
@AllArgsConstructor
public class MercatorMapProjection implements IMapProjection {

	public static final double MIN_LAT = -85.05112878;
	public static final double MAX_LAT = +85.05112878;
	public static final double MIN_LON = -180;
	public static final double MAX_LON = +180;
	
	private TileSize tileSize;
	private IZoomLevelMapper zoomLevelMapper;
	
	@Override
	public RelativeCoordinates fromGeoToRelativeCoordinates(
			GeoCoordinates geoCoordinates, 
			TileCoordinates tileCoordinates) {
		
		PixelCoordinates pixelCoordinates = fromGeoToPixelCoordinates(
				geoCoordinates.getLongitude(), 
				geoCoordinates.getLatitude(), 
				tileCoordinates.getZoom());
		
		RelativeCoordinates relativeCoordinates = fromPixelToRelativeCoordinates(
				pixelCoordinates, 
				tileCoordinates);
		
		return relativeCoordinates;
	}
	
	@Override
	public GeoBoundingBox fromTileCoordinatesToGeoBoundingBox(
			TileCoordinates tileCoordinates, 
			IFilter<?, ?> filter) {
		
		// top-left and bottom-right corners
		
		
		PixelCoordinates leftTop = fromTileToPixelCoordinates(tileCoordinates);
		
		long left = leftTop.getX();
		long top = leftTop.getY();
		long right = left + this.tileSize.getWidth();
		long bottom = top + this.tileSize.getHeight();
		
		// padding
		
		left -= filter.getWidth()  - 1 - filter.getCenterX();
		top  -= filter.getHeight() - 1 - filter.getCenterY();
		
		right  += filter.getCenterX();
		bottom += filter.getCenterY();
		
		// convert to longitude and latitude
		
		GeoCoordinates min = fromPixelToGeoCoordinates(
				new PixelCoordinates(left, bottom),
				tileCoordinates.getZoom());
		
		GeoCoordinates max = fromPixelToGeoCoordinates(
				new PixelCoordinates(right, top),
				tileCoordinates.getZoom());
		
		// return
		
		return new GeoBoundingBox(min, max);
	}
	
	@Override
	public List<TileCoordinates> overlappingTiles(
			GeoCoordinates geoCoordinates,
			int zoom,
			IFilter<?, ?> filter) {
		
		List<TileCoordinates> coordinates = new ArrayList<TileCoordinates>();
		
		TileCoordinates tileCoordinates = 
				fromGeoToTileCoordinates(geoCoordinates, zoom);
		
		coordinates.add(tileCoordinates);
		
		RelativeCoordinates relativeCoordinates = 
				fromGeoToRelativeCoordinates(geoCoordinates, tileCoordinates);

		List<TileCoordinates> overlappingCoordinates =
				relativeCoordinates.overlappingTiles(
						tileCoordinates, 
						filter, 
						this.tileSize,
						this.zoomLevelMapper);
		
		coordinates.addAll(overlappingCoordinates);
		
		return coordinates;
	}

	private TileCoordinates fromGeoToTileCoordinates(
			GeoCoordinates geoCoordinates, 
			int zoom) {
		
		PixelCoordinates pixelCoordinates = fromGeoToPixelCoordinates(
				geoCoordinates.getLongitude(), 
				geoCoordinates.getLatitude(), 
				zoom);
		
		return fromPixelToTileCoordinates(pixelCoordinates, zoom);
	}
	
	private TileCoordinates fromPixelToTileCoordinates(
			PixelCoordinates pixelCoordinates, 
			int zoom) {
		
		return new TileCoordinates(
				(int) (pixelCoordinates.getX() / this.tileSize.getWidth()), 
				(int) (pixelCoordinates.getY() / this.tileSize.getHeight()),
				zoom);
	}
	
	private PixelCoordinates fromTileToPixelCoordinates(TileCoordinates tileCoordinates) {
		return new PixelCoordinates(
				tileCoordinates.getX() * this.tileSize.getWidth(),
				tileCoordinates.getY() * this.tileSize.getHeight());
	}
	
	private GeoCoordinates fromPixelToGeoCoordinates(
			PixelCoordinates pixelCoordinates, 
			int zoom) {
		
		PixelDimensions pixelDimensions = getPixelDimensions(zoom);
		double mapSizeX = pixelDimensions.getWidth();
		double mapSizeY = pixelDimensions.getHeight();

		double xClip = clip(pixelCoordinates.getX(), 0, mapSizeX - 1);
		double yClip = clip(pixelCoordinates.getY(), 0, mapSizeY - 1);
		
		double xx = (xClip / mapSizeX) - 0.5;
		double yy = 0.5 - (yClip / mapSizeY);

		double latitude = 
			90 
			- 360 * Math.atan(Math.exp(-yy * 2 * Math.PI)) / Math.PI;
		double longitude = 360 * xx;
				
		return new GeoCoordinates(longitude, latitude);
	}
	
	private static double clip(
			double value, 
			double minValue, 
			double maxValue) {
		
		return 
				Math.min(
					Math.max(
							value, 
							minValue), 
					maxValue);
	}
	
	/**
	 * @param zoom zoom level
	 * @return how many pixels there are in each dimensions given the zoom level
	 * 		based on the number of possible tiles 
	 */
	private PixelDimensions getPixelDimensions(int zoom) {
		
		ZoomLevelSize zoomLevelSize = this.zoomLevelMapper.getSize(zoom);
		
		return new PixelDimensions(
				zoomLevelSize.getWidth() * this.tileSize.getWidth(),
				zoomLevelSize.getHeight() * this.tileSize.getHeight());
	}

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @param zoom zoom level
	 * 
	 * @return the pixel coordinates of the given (lon, lat) pair
	 */
	private PixelCoordinates fromGeoToPixelCoordinates(
			double longitude,
			double latitude,
			int zoom) {

		latitude = clip(latitude, MIN_LAT, MAX_LAT);
		longitude = clip(longitude, MIN_LON, MAX_LON);

		double x = (longitude + 180) / 360;
		double sinLatitude = Math.sin(latitude * Math.PI / 180);
		double y = 
				0.5 - 
				Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

		PixelDimensions pixelDimensions = getPixelDimensions(zoom);
		long mapSizeX = pixelDimensions.getWidth();
		long mapSizeY = pixelDimensions.getHeight();

		PixelCoordinates pixelCoordinates = new PixelCoordinates(
				(long) clip(x * mapSizeX + 0.5, 0, mapSizeX - 1), 
				(long) clip(y * mapSizeY + 0.5, 0, mapSizeY - 1));

		return pixelCoordinates;
	}

	/**
	 * @param pixelCoordinates pixel coordinates
	 * @param tileCoordinates coordinates of the tile
	 * @return pixel coordinates relative to the top-left tile corner
	 */
	private RelativeCoordinates fromPixelToRelativeCoordinates(
			PixelCoordinates pixelCoordinates,
			TileCoordinates tileCoordinates) {
		
		int x = (int) (pixelCoordinates.getX() - (tileCoordinates.getX() * this.tileSize.getWidth()));
		int y = (int) (pixelCoordinates.getY() - (tileCoordinates.getY() * this.tileSize.getHeight()));
		
		return new RelativeCoordinates(x, y);
	}
	
	@Data
	@AllArgsConstructor
	private static class PixelDimensions {
		private long width;
		private long height;
	}
}
