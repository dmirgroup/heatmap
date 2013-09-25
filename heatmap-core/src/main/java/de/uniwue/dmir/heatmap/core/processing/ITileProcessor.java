package de.uniwue.dmir.heatmap.core.processing;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;

public interface ITileProcessor<E extends IExternalData, I> {
	void process(ITile<E, I> tile);
}