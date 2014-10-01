package de.uniwue.dmir.heatmap.filters;

import java.util.Collection;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class NoFilter<TPoint, TTile> implements IFilter<TPoint, TTile> {

	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	public NoFilter() {
		this(1,1,0,0);
	}
	
	public NoFilter(int width, int height, int centerX, int centerY) {
		super();
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	@Override
	public <TDerived extends TPoint> void filter(
			TDerived dataPoint,
			TTile tile, 
			TileSize tileSize, 
			TileCoordinates tileCoordinates) {
	}

	@Override
	public <TDerived extends TPoint> void filter(
			Collection<TDerived> dataPoints, 
			TTile tile, 
			TileSize 
			tileSize,
			TileCoordinates tileCoordinates) {
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

}
