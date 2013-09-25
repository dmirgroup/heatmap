package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;

/**
 * Keeps the complete data in memory and allows to reload the data from the 
 * internal data source on demand.
 * 
 * @author Martin Becker
 *
 * @param <S> data type
 */
public abstract class AbstractCachedGeoDataSource<S> extends AbstractProxyGeoDataSource<S> {

	public AbstractCachedGeoDataSource(IGeoDataSource<S> dataSource) {
		super(dataSource);
	}

	/**
	 * Reloads the data from the internal data source.
	 */
	public abstract void reload();
	
}
