package de.uniwue.dmir.heatmap;

import org.junit.Assert;
import org.junit.Test;

import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.point.sources.geo.projections.MercatorMapProjection;

public class GeoTileRangeProviderTest {

	@Test
	public void fullRange() {
		
		GeoBoundingBox geoBoundingBox = new GeoBoundingBox(
				-180, -90, 180, 90);
		
		IMapProjection mapProjection = new MercatorMapProjection();
		
		GeoTileRangeProvider provider = 
				new GeoTileRangeProvider(geoBoundingBox, mapProjection);
	
		TileRange tileRange = provider.getTileRange(0);
		Assert.assertEquals(new TileRange(0,0,0,0), tileRange);

		tileRange = provider.getTileRange(1);
		Assert.assertEquals(new TileRange(0,1,0,1), tileRange);
		
		tileRange = provider.getTileRange(2);
		Assert.assertEquals(new TileRange(0,3,0,3), tileRange);
	}
	

	@Test
	public void topRight() {
		
		GeoBoundingBox geoBoundingBox = new GeoBoundingBox(
				0, 0, 180, 90);
		
		IMapProjection mapProjection = new MercatorMapProjection();
		
		GeoTileRangeProvider provider = 
				new GeoTileRangeProvider(geoBoundingBox, mapProjection);
	
		TileRange tileRange = provider.getTileRange(0);
		Assert.assertEquals(new TileRange(0,0,0,0), tileRange);

		tileRange = provider.getTileRange(1);
		Assert.assertEquals(new TileRange(1,1,0,1), tileRange);
		
		tileRange = provider.getTileRange(2);
		Assert.assertEquals(new TileRange(2,3,0,2), tileRange);
	}
	
	@Test
	public void bottomLeft() {
		
		GeoBoundingBox geoBoundingBox = new GeoBoundingBox(
				-180, -90, 0, 0);
		
		IMapProjection mapProjection = new MercatorMapProjection();
		
		GeoTileRangeProvider provider = 
				new GeoTileRangeProvider(geoBoundingBox, mapProjection);
	
		TileRange tileRange = provider.getTileRange(0);
		Assert.assertEquals(new TileRange(0,0,0,0), tileRange);

		tileRange = provider.getTileRange(1);
		Assert.assertEquals(new TileRange(0,1,1,1), tileRange);
		
		tileRange = provider.getTileRange(2);
		Assert.assertEquals(new TileRange(0,2,2,3), tileRange);
	}
	
}
