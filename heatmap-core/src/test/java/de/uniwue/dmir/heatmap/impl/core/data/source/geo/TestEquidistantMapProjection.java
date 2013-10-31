package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.filter.AbstractConfigurableFilter;

public class TestEquidistantMapProjection {
	
	@Test
	public void testRelativeCoordinates() {
		
		double offset = 5;
		
		EquidistantProjection equidistantProjection = new EquidistantProjection(
				new GeoBoundingBox(
						new GeoCoordinates(0 + offset, 10 + offset), 
						new GeoCoordinates(10 + offset, 0 + offset)), 
				new TileSize(10, 10));
		
		RelativeCoordinates r1 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0.5 + offset, 9.5 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 0), r1);
		
		RelativeCoordinates r2 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(-0.5 + offset, 10.5 + offset), 
						null);

		Assert.assertEquals(new RelativeCoordinates(-1, -1), r2);
		
		RelativeCoordinates r3 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0 + offset, 0.5 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 9), r3);
		
		// points on the south east border are not considered as part of the tile
		
		RelativeCoordinates r4 = 
				equidistantProjection.fromGeoToRelativeCoordinates(
						new GeoCoordinates(0 + offset, 0 + offset), 
						null);
		
		Assert.assertEquals(new RelativeCoordinates(0, 10), r4);
		
	}
	
	@Test
	public void test() {

		double offset = 5;
		
		EquidistantProjection equidistantProjection = new EquidistantProjection(
				new GeoBoundingBox(
						new GeoCoordinates(0 + offset, 10 + offset), 
						new GeoCoordinates(10 + offset, 0 + offset)), 
				new TileSize(10, 10));
		
		IFilter<?, ?> filter = new AbstractConfigurableFilter<ValuePixel, Object>() {
			@Override
			public void filter(
					ValuePixel dataPoint, 
					Object tile,
					TileSize tileSize,
					TileCoordinates tileCoordinates) {
			}
		};
		
		List<TileCoordinates> tileCoordinates = 
				equidistantProjection.overlappingTiles(
						new GeoCoordinates(offset, offset + .5), 
						0, 
						filter);

		Assert.assertEquals(tileCoordinates.size(), 1);
		
		List<TileCoordinates> tileCoordinates2 = 
				equidistantProjection.overlappingTiles(
						new GeoCoordinates(offset, offset), 
						0, 
						filter);

		Assert.assertEquals(tileCoordinates2.size(), 0);
	}
}
