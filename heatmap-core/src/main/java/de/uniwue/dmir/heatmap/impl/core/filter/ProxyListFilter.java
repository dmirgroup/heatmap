package de.uniwue.dmir.heatmap.impl.core.filter;

import java.util.List;

import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * Applies a set of filters.
 * 
 * TODO: finish
 * 
 * @author Martin Becker
 *
 * @param <E>
 * @param <I>
 */
public class ProxyListFilter<E extends IExternalData, I>
extends AbstractConfigurableFilter<E, I>
implements IFilter<E, I>{
	
	private List<IFilter<E, I>> filters;

	public void addFilter(IFilter<E, I> filter) {
		
		this.filters.add(filter);
		
		// set width, height, x and y
	}
	
	@Override
	public void filter(
			E dataPoint, 
			I tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {

		for (IFilter<E, I> filter : this.filters) {
			filter.filter(dataPoint, tile, tileSize, tileCoordinates);
		}
	}

}
