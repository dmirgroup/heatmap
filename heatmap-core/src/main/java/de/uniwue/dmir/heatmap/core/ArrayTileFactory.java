package de.uniwue.dmir.heatmap.core;

import java.lang.reflect.Array;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;


@AllArgsConstructor
public class ArrayTileFactory<I>
implements ITileFactory<I[]> {

	private Class<I> clazz;
	
	@Override
	@SuppressWarnings("unchecked")
	public I[] newInstance(TileSize size, TileCoordinates coordinates) {
		return (I[]) Array.newInstance(
				this.clazz, 
				size.getWidth() * size.getHeight());
	}

}
