/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.DefaultHeatmapDimensions;
import de.uniwue.dmir.heatmap.core.IHeatmapDimensions.GridDimensions;
import de.uniwue.dmir.heatmap.core.ITileCoordinatesProjection.DefaultTileCoordinatesProjection;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.ExhaustiveTileIterator.IAdditionalDataProvider;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.Tile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Heatmap<E extends IExternalData, I> 
implements IHeatmap<E, I>{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IExternalDataSource<E> dataSource;
	private IFilter<E, I> filter;
	
	@Getter
	private ZoomLevelRange zoomLevelRange;
	@Getter
	private IHeatmapDimensions dimensions;
	
	private ITileCoordinatesProjection projection;
	
	public Heatmap(
			IExternalDataSource<E> dataSource,
			IFilter<E, I> filter,
			ZoomLevelRange zoomLevelRange,
			IHeatmapDimensions dimensions, 
			ITileCoordinatesProjection projection) {
		
		this.dataSource = dataSource;
		this.filter = filter;
		this.zoomLevelRange = zoomLevelRange;
		this.dimensions = dimensions;
		this.projection = projection;
	}
	
	public Heatmap(
			IExternalDataSource<E> dataSource,
			IFilter<E, I> filter,
			ZoomLevelRange zoomLevelRange) {

		this(
				dataSource, 
				filter, 
				zoomLevelRange, 
				new DefaultHeatmapDimensions(), 
				new DefaultTileCoordinatesProjection());
	}
	
	@Override
	public ITile<E, I> getTile(TileCoordinates coordinates, I[] additionalData) {
		
		// tile coordinates to top-left reference system
		TileCoordinates projectedCoordinates =
				this.projection.fromCustomToTopLeft(
						coordinates, 
						this.dimensions);
		
		
		// get data
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		List<E> externalData = this.dataSource.getData(
				projectedCoordinates, 
				this.filter);
		
		stopWatch.stop();
		this.logger.debug(
				"getting {} data points for tile: {}", 
				externalData.size(),
				stopWatch.toString());
		
		// return null if no data was found
		if (externalData == null || externalData.isEmpty()) {
			this.logger.debug("adding data to tile: no data");
			return null;
		}
		
		Tile<E, I> tile = new Tile<E, I>(
				this.dimensions.getTileDimensions(), 
				projectedCoordinates, 
				this.filter, 
				additionalData);
		
		stopWatch.reset();
		stopWatch.start();
		
		for (E e : externalData) {
			tile.add(e);
		}
		
		stopWatch.stop();
		this.logger.debug(
				"adding {} data points to tile: {}" , 
				externalData.size(), 
				stopWatch.toString());
			
		return tile;
	}
	
	@Override
	public List<ITile<E, I>> getTiles(
			int zoom,
			IAdditionalDataProvider<I> additionalDataProvider,
			boolean excludeEmptyTiles) {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Map<Integer, Set<TileCoordinates>> tileCoordinates = 
				this.dataSource.getNonEmptyTiles(zoom, zoom, filter);
		
		stopWatch.stop();
		this.logger.debug(
				"getting {} non-empty tile coordinates: {}", 
				tileCoordinates.size(),
				stopWatch.toString());
		
		stopWatch.reset();
		stopWatch.start();

		List<ITile<E, I>> tiles = new ArrayList<ITile<E,I>>();
		for (TileCoordinates c : tileCoordinates.get(zoom)) {
			ITile<E, I> tile = 
					this.getTile(c, additionalDataProvider.getAdditionalData(c));
			tiles.add(tile);
		}
		
		stopWatch.stop();
		this.logger.debug(
				"getting {} tiles from heatmap: {}", 
				tiles.size(), 
				stopWatch.toString());
		
		return tiles;
	}

	@Override
	public TileCoordinates getMinTileCoordiantes(int zoom) {
		
		TileCoordinates minCoordinates = new TileCoordinates(0, 0, zoom);
		TileCoordinates projectedMinTileCoordinates = 
				this.projection.fromTopLeftToCustom(
						minCoordinates, 
						this.dimensions);
		
		return projectedMinTileCoordinates;
	}

	@Override
	public TileCoordinates getMaxTileCoordiantes(int zoom) {
		
		GridDimensions gridDimensions = this.dimensions.getGridDimensions(zoom);
		TileCoordinates maxCoordinates = new TileCoordinates(
				gridDimensions.getWidth() - 1, 
				gridDimensions.getHeight() - 1, 
				zoom);
		
		TileCoordinates projectedMaxTileCoordinates = 
				this.projection.fromTopLeftToCustom(
						maxCoordinates, 
						this.dimensions);
		
		return projectedMaxTileCoordinates;
	}


}
