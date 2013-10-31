package de.uniwue.dmir.heatmap;

import de.uniwue.dmir.heatmap.core.IHeatmap.DefaultZoomLevelMapper;
import de.uniwue.dmir.heatmap.core.IHeatmap.IZoomLevelMapper;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.MercatorMapProjection;
import de.uniwue.dmir.heatmap.impl.core.filter.PointFilter;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.GreatCircleDistance;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.GreatCircleDistance.EquidistantApproximation;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.GreatCircleDistance.Cosine;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.GreatCircleDistance.Haversine;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.IDistanceFunction;

public class TestMeters {
	public static void main(String[] args) {
		
		int zoomLevel = 3;
		TileSize tileSize = new TileSize(1, 1);
		
		IZoomLevelMapper zoomLevelMapper = new DefaultZoomLevelMapper();
		
		MercatorMapProjection projection = new MercatorMapProjection(
				tileSize, zoomLevelMapper);
		
		GeoBoundingBox bb = projection.fromTileCoordinatesToGeoBoundingBox(
				new TileCoordinates(
						(int) zoomLevelMapper.getSize(zoomLevel).getWidth() / 2, 
						(int) zoomLevelMapper.getSize(zoomLevel).getHeight() / 2 + 1, 
						zoomLevel), 
				new PointFilter<Object>(null));
		System.out.println(bb);
		
		GeoCoordinates northWest = bb.getNorthWest();
		GeoCoordinates southEast = bb.getSouthEast();
	
//		GeoCoordinates northEast = new GeoCoordinates(southEast.getLongitude(), northWest.getLatitude());
		GeoCoordinates southWest = new GeoCoordinates(northWest.getLongitude(), southEast.getLatitude());

		System.out.println(northWest);
		System.out.println(southWest);
		System.out.println(southEast);
		
		IDistanceFunction<GeoCoordinates> distance = 	new EquidistantApproximation();
		IDistanceFunction<GeoCoordinates> cosine = 		new Cosine();
		IDistanceFunction<GeoCoordinates> haversine = 	new Haversine();
		
		System.out.println(distance.distance(northWest, southWest)/ tileSize.getWidth());
		System.out.println(distance.distance(southWest, southEast)/ tileSize.getHeight());
		System.out.println("---");
		System.out.println(cosine.distance(northWest, southWest)/ tileSize.getWidth());
		System.out.println(cosine.distance(southWest, southEast)/ tileSize.getHeight());
		System.out.println("---");
		System.out.println(haversine.distance(northWest, southWest)/ tileSize.getWidth());
		System.out.println(haversine.distance(southWest, southEast)/ tileSize.getHeight());
		System.out.println("---");
	
		GeoCoordinates frankfurt = new GeoCoordinates(8.680506, 50.111511);
		GeoCoordinates munich = new GeoCoordinates(11.580186, 48.139126);

		System.out.println(distance.distance(frankfurt, munich));
		System.out.println(cosine.distance(frankfurt, munich));
		System.out.println(haversine.distance(frankfurt, munich));
		
		System.out.println(2 * Math.PI * GreatCircleDistance.EARTH_RADIUS / zoomLevelMapper.getSize(18).getWidth());
	}
	

}
