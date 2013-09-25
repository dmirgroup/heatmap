package de.uniwue.dmir.heatmap.core.tile;

import java.lang.reflect.Array;

import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.TileDimensions;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Tile<E extends IExternalData, I> implements ITile<E, I>{

	@Getter
	private TileDimensions dimensions;
	
	@Getter
	private TileCoordinates coordinates;
	
	private IFilter<E, I> filter;

	private I[] tileData;
	
	@SuppressWarnings("unchecked")
	public Tile(
			TileDimensions tileDimensions,
			TileCoordinates coordinates,
			IFilter<E, I> filter, 
			I[] initialData) {
		
		if (tileDimensions == null) {
			throw new IllegalArgumentException("Tile dimensions must not be null.");
		}
		
		if (coordinates == null) {
			throw new IllegalArgumentException("Coordinates must not be null.");
		}
		
		if (filter == null) {
			throw new IllegalArgumentException("Filter must not be null.");
		}
		
		if (initialData == null) {
			throw new IllegalArgumentException(
					"Initial data must not be null, but may be empty.");
		}
		
		this.dimensions = tileDimensions;
		this.coordinates = coordinates;
		this.filter = filter;
		
		int length = this.dimensions.getWidth() * this.dimensions.getHeight();
		if (initialData.length != 0 && initialData.length != length) {
			throw new IllegalArgumentException(
					"Initial data must have a certain size: " + length);
		}
		
		if (initialData.length > 0) {
			this.tileData = initialData;
		} else {
			this.tileData = (I[]) Array.newInstance(
					initialData.getClass().getComponentType(), 
					length);
		}

	}
	
	public void add(E dataPoint) {
		this.filter.filter(dataPoint, this);
	}
	
	public I[] getData() {
		return this.tileData;
	}

}
