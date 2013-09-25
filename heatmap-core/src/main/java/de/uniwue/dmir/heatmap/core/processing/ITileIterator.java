package de.uniwue.dmir.heatmap.core.processing;

import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;

public interface ITileIterator<E extends IExternalData, I> {
	public void iterate(IHeatmap<E, I> heatmap, ITileProcessor<E, I> processor);
}
