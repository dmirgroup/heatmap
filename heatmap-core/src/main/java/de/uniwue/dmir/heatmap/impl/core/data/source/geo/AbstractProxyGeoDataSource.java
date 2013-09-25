package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.data.source.geo.IGeoDataSource;


public abstract class AbstractProxyGeoDataSource<S> implements IGeoDataSource<S> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected IGeoDataSource<S> dataSource;
	
	public AbstractProxyGeoDataSource(IGeoDataSource<S> dataSource) {
		this.dataSource = dataSource;
	}
	
}
