package de.uniwue.dmir.heatmap.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.ITileProcessor;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class EmptyHeatmap<E extends IExternalData, I> 
implements IHeatmap<E, I>{

	@Getter
	private HeatmapSettings settings;

	@Override
	public I getTile(TileCoordinates coordinates) {
		return null;
	}

	@Override
	public void processTiles(ITileProcessor<I> processor) {
		// nothing to do
	}

}
