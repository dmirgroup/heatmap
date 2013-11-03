package de.uniwue.dmir.heatmap.core.processing;

import java.awt.Polygon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.data.source.geo.IMapProjection;
import de.uniwue.dmir.heatmap.core.processing.FilteredProxyKeyValueIteratorFactory.IObjectFilter;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.GeoPolygon;

@AllArgsConstructor
public class PolygonRelativeCoordinatesFilter 
implements IObjectFilter<RelativeCoordinates> {

	@Getter
	private Polygon polygon;
	
	@Override
	public boolean isToBeDisregarded(RelativeCoordinates object) {
		return this.polygon.contains(object.getX(), object.getY());
	}
	
	public static final Polygon polygon(
		GeoPolygon geoPolygon,
		TileCoordinates tileCoordinates,
		IMapProjection mapProjection) {

		int length = geoPolygon.getP().length / 2;
		
		int[] x = new int[length];
		int[] y = new int[length];
		for (int i = 0; i < length; i++) {
			RelativeCoordinates relativeCoordinates =
					mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(
									geoPolygon.getP()[2 * i + 1], 
									geoPolygon.getP()[2 * i + 0]), 
							null);
			
			x[i] = relativeCoordinates.getX();
			y[i] = relativeCoordinates.getY();
		}
		
		return new Polygon(x, y, length);
	}
}
