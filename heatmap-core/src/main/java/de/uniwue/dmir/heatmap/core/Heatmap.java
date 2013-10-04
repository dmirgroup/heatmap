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

import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.ITileProcessor;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Heatmap<E extends IExternalData, I> 
implements IHeatmap<I> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ITileFactory<I> tileFactory;

	private IExternalDataSource<E> dataSource;
	private IFilter<E, I> filter;
	
	@Getter
	@Setter
	private IHeatmap<I> seed;
	
	@Getter
	private HeatmapSettings settings;
	
	public Heatmap(
			ITileFactory<I> tileFactory,
			IExternalDataSource<E> dataSource,
			IFilter<E, I> filter,
			HeatmapSettings settings) {
		
		this(tileFactory, dataSource, filter, settings, null);
	}
	
	public Heatmap(
			ITileFactory<I> tileFactory,
			IExternalDataSource<E> dataSource,
			IFilter<E, I> filter,
			HeatmapSettings settings,
			IHeatmap<I> seed) {
		
		if (seed == null) {
			this.seed = new EmptyHeatmap<I>(settings);
		} else {
			this.seed = seed;
		}
		
		this.tileFactory = tileFactory;
		this.dataSource = dataSource;
		this.filter = filter;
		this.settings = settings;
	}
	
	@Override
	public I getTile(TileCoordinates coordinates) {
		
		// tile coordinates to top-left reference system
		TileCoordinates projectedCoordinates =
				this.settings.getTileProjection().fromCustomToTopLeft(
						coordinates,
						this.settings.getZoomLevelMapper());
		
		// get data
		
		this.logger.debug("Getting data points for tile.");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Iterator<E> externalData = this.dataSource.getData(
				projectedCoordinates, 
				this.filter);
		
		stopWatch.stop();
		this.logger.debug(
				"Done getting data points for tile: {}", 
				stopWatch.toString());
		
		// return null if no data was found
		if (externalData == null || !externalData.hasNext()) {
			this.logger.debug("Adding data to tile: no data.");
			return null;
		}
		
		// initialize tile
		
		this.logger.debug("Loading data seed.");
		I tile = this.seed.getTile(projectedCoordinates);
		
		if (tile == null) {
			this.logger.debug("No data seed available; initializing empty tile.");
			tile = this.tileFactory.newInstance(
					this.settings.getTileSize(), 
					projectedCoordinates);
		}

		this.logger.debug("Done loading data seed.");
		
		// add external data to tile


		this.logger.debug("Adding data points to tile: {}", coordinates);
		
		stopWatch.reset();
		stopWatch.start();
		
		int externalDataPointCount = 0;
		while(externalData.hasNext()) {
			E externalDataPoint = externalData.next();
			this.filter.filter(externalDataPoint, tile, this.settings.getTileSize());
			externalDataPointCount ++;
		}
		
		stopWatch.stop();
		this.logger.debug(
				"Done adding {} data points to tile: {}" , 
				externalDataPointCount, 
				stopWatch.toString());
			
		return tile;
	}

	@Override
	public void processTiles(ITileProcessor<I> processor) {
		
		this.logger.debug("Processing tiles.");
		
		int min = this.settings.getZoomLevelRange().getMin();
		int max = this.settings.getZoomLevelRange().getMax();
		
		for (int zoomLevel = min; zoomLevel <= max; zoomLevel ++) {

			this.logger.debug(
					"Getting tile coordinates with content for zoom level {}.",
					zoomLevel);
			
			Iterator<TileCoordinates> iterator =
					this.dataSource.getTileCoordinatesWithContent(
							zoomLevel, this.filter);
			
			this.logger.debug("Done getting tile coordinates with content.");
			
			this.logger.debug("Processing tiles on zoom level {}.", zoomLevel);
			
			int tileCount = 0;
			while (iterator.hasNext()) {
				TileCoordinates coordinates = iterator.next();
				I tile = this.getTile(coordinates);
				processor.process(tile, this.settings.getTileSize(), coordinates);
				tileCount ++;
			}

			this.logger.debug(
					"Done processing {} tiles on zoom level: {}", 
					tileCount, 
					zoomLevel);
		}

		this.logger.debug("Done processing tiles.");
		
	}
}
