package de.uniwue.dmir.heatmap;

import java.util.List;

import de.uniwue.dmir.heatmap.filters.NullFilter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox.GeoBoundingBoxCorners;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class GeoTileRangeProvider 
implements ITileRangeProvider {

	private IMapProjection mapProjection;
	
	private GeoBoundingBoxCorners corners;
	private NullFilter<?, ?> filter = new NullFilter<Object, Object>();
	
	public GeoTileRangeProvider(
			GeoBoundingBox geoBoundingBox,
			IMapProjection mapProjection) {
		
		this.corners = geoBoundingBox.getCorners();
		this.mapProjection = mapProjection;
	}
	
	@Override
	public TileRange getTileRange(int zoom) {
		
		List<TileCoordinates> topLeftList = 
				this.mapProjection.overlappingTiles(
						this.corners.getTopLeft(),
						zoom,
						this.filter);
		
		List<TileCoordinates> bottomRightList = 
				this.mapProjection.overlappingTiles(
						this.corners.getBottomRight(),
						zoom, 
						this.filter);
		
		TileCoordinates topLeft = topLeftList.get(0);
		TileCoordinates bottomRight = bottomRightList.get(0);
		
		TileRange range = new TileRange();
		range.setMinX(topLeft.getX());
		range.setMinY(topLeft.getY());
		range.setMaxX(bottomRight.getX());
		range.setMaxY(bottomRight.getY());
		
		return range;
	}

}
