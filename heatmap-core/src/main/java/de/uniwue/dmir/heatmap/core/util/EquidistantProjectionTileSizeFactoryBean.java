package de.uniwue.dmir.heatmap.core.util;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.FactoryBean;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.core.data.source.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.source.geo.EquidistantProjection;
import de.uniwue.dmir.heatmap.impl.core.visualizer.rbf.IDistanceFunction;

@AllArgsConstructor
public class EquidistantProjectionTileSizeFactoryBean 
implements FactoryBean<TileSize> {
	
	private double widthMeters;
	private double heightMeters;
	private boolean northReference;
	private GeoBoundingBox geoBoundingBox;
	private IDistanceFunction<GeoCoordinates> distanceFunction;
	
	@Override
	public TileSize getObject() throws Exception {
		return EquidistantProjection.getTileSize(
				this.widthMeters, 
				this.heightMeters, 
				this.northReference, 
				this.geoBoundingBox, 
				this.distanceFunction);
	}
	@Override
	public Class<TileSize> getObjectType() {
		return TileSize.class;
	}
	@Override
	public boolean isSingleton() {
		return false;
	}

}
