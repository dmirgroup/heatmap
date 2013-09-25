package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.GridDimensions;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.tile.coordinates.PixelCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class MercatorMapProjection implements IMapProjection {

	public static final double MIN_LAT = -85.05112878;
	public static final double MAX_LAT = +85.05112878;
	public static final double MIN_LON = -180;
	public static final double MAX_LON = +180;
	
	private IHeatmapDimensions dimensions;
	
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
		
		PixelCoordinates topLeft = fromTileToPixelCoordinates(tileCoordinates);
		
		PixelCoordinates bottomRight = new PixelCoordinates(
				topLeft.getX() + this.dimensions.getTileDimensions().getWidth(), 
				topLeft.getY() + this.dimensions.getTileDimensions().getHeight());
		
		// padding
		
		topLeft.setX(topLeft.getX() - (filter.getWidth() - 1 - filter.getCenterX()));
		topLeft.setY(topLeft.getY() - (filter.getHeight() - 1 - filter.getCenterY()));
		
		bottomRight.setX(bottomRight.getX() + filter.getCenterX());
		bottomRight.setY(bottomRight.getY() + filter.getCenterY());
		
		// convert to longitude and latitude
		
		GeoCoordinates northWest = fromPixelToGeoCoordinates(
				topLeft, 
				tileCoordinates.getZoom());
		
		GeoCoordinates southEast = fromPixelToGeoCoordinates(
				bottomRight,
				tileCoordinates.getZoom());
		
		// return
		
		return new GeoBoundingBox(northWest, southEast);
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
						this.dimensions);
		
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
				(int) (pixelCoordinates.getX() / this.dimensions.getTileDimensions().getWidth()), 
				(int) (pixelCoordinates.getY() / this.dimensions.getTileDimensions().getHeight()),
				zoom);
	}
	
	private PixelCoordinates fromTileToPixelCoordinates(TileCoordinates tileCoordinates) {
		return new PixelCoordinates(
				tileCoordinates.getX() * this.dimensions.getTileDimensions().getWidth(),
				tileCoordinates.getY() * this.dimensions.getTileDimensions().getHeight());
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
		
		GridDimensions gridDimensions = this.dimensions.getGridDimensions(zoom);
		
		return new PixelDimensions(
				gridDimensions.getWidth() 
					* this.dimensions.getTileDimensions().getWidth(),
				gridDimensions.getHeight() 
					* this.dimensions.getTileDimensions().getHeight());
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
		
		int x = (int) (pixelCoordinates.getX() - (tileCoordinates.getX() * this.dimensions.getTileDimensions().getWidth()));
		int y = (int) (pixelCoordinates.getY() - (tileCoordinates.getY() * this.dimensions.getTileDimensions().getHeight()));
		
		return new RelativeCoordinates(x, y);
	}
	
	@Data
	@AllArgsConstructor
	private static class PixelDimensions {
		private long width;
		private long height;
	}
}
