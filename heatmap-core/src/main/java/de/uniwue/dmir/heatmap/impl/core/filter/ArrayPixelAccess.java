package de.uniwue.dmir.heatmap.impl.core.filter;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.filter.IPixelAccess;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

public class ArrayPixelAccess<I> 
implements IPixelAccess<I, I[]>{

	@Override
	public I get(
			RelativeCoordinates relativeCoordinates, 
			I[] tile,
			TileSize tileSize) {

		return Arrays2d.get(
				relativeCoordinates.getX(), 
				relativeCoordinates.getY(), 
				tile, 
				tileSize.getWidth(),
				tileSize.getHeight());
	}
	
	@Override
	public void set(
			I pixelValue, 
			RelativeCoordinates relativeCoordinates,
			I[] tile, 
			TileSize tileSize) {
		
		Arrays2d.set(
				pixelValue,
				relativeCoordinates.getX(), 
				relativeCoordinates.getY(), 
				tile, 
				tileSize.getWidth(),
				tileSize.getHeight());
	}

}
