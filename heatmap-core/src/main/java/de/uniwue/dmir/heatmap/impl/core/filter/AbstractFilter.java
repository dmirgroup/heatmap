package de.uniwue.dmir.heatmap.impl.core.filter;

import java.util.Collection;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;

public abstract class AbstractFilter<E extends IExternalData, I> 
implements IFilter<E, I> {

	@Override
	public void filter(Collection<E> dataPoints, I tile, TileSize tileSize) {
		for (E dataPoint : dataPoints) {
			this.filter(dataPoint, tile, tileSize);
		}
	}
	
}
