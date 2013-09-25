package de.uniwue.dmir.heatmap.core.tile;

import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.TileDimensions;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public interface ITile<E extends IExternalData, I> {
	
	public TileDimensions getDimensions();
	
	public TileCoordinates getCoordinates();
	
	public I[] getData();

}
