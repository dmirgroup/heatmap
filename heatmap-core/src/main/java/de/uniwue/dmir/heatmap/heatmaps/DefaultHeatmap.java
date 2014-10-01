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
package de.uniwue.dmir.heatmap.heatmaps;

import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import de.uniwue.dmir.heatmap.HeatmapSettings;
import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.IPointsource;
import de.uniwue.dmir.heatmap.ITileFactory;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class DefaultHeatmap<TPoint, TTile> 
implements IHeatmap<TTile> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Factory to create empty tiles.
	 */
	private ITileFactory<TTile> tileFactory;

	/**
	 * Data source providing the data points for the tiles.
	 */
	private IPointsource<TPoint> pointsource;
	
	/**
	 * Filter used to add points to tiles.
	 */
	private IFilter<TPoint, TTile> filter;
	
	/**
	 * Heatmap providing tile seeds.
	 */
	@Getter
	@Setter
	private IHeatmap<TTile> seed;
	
	/**
	 * Heatmap settings.
	 */
	@Getter
	private HeatmapSettings settings;
	
	@Getter
	@Setter
	private boolean returnSeedTilesWithNoExternalData;
	
	public DefaultHeatmap(
			ITileFactory<TTile> tileFactory,
			IPointsource<TPoint> pointsource,
			IFilter<TPoint, TTile> filter,
			HeatmapSettings settings) {
		
		this(tileFactory, pointsource, filter, settings, null);
	}
	
	public DefaultHeatmap(
			ITileFactory<TTile> tileFactory,
			IPointsource<TPoint> pointsource,
			IFilter<TPoint, TTile> filter,
			HeatmapSettings settings,
			IHeatmap<TTile> seed) {
		
		if (seed == null) {
			this.seed = new EmptyHeatmap<TTile>(settings);
		} else {
			this.seed = seed;
		}
		
		this.tileFactory = tileFactory;
		this.pointsource = pointsource;
		this.filter = filter;
		this.settings = settings;
		this.returnSeedTilesWithNoExternalData = false;
	}
	
	@Override
	public TTile getTile(TileCoordinates tileCoordinates) {
		
		// initializing stop watch
		StopWatch stopWatch = new StopWatch();
		
		// tile coordinates to top-left reference system
		TileCoordinates projectedTileCoordinates =
				this.settings.getTileProjection().fromCustomToTopLeft(
						tileCoordinates,
						this.settings.getZoomLevelMapper());
		
		// loading data seed 
		
		this.logger.debug("Loading data seed.");
		
		stopWatch.start("loading data seed");
		
		TTile tile = this.seed.getTile(projectedTileCoordinates);
		
		stopWatch.stop();
		if (tile == null) {
			this.logger.debug("No data seed available: {}", stopWatch.toString());
		} else {
			this.logger.debug("Done loading data seed: {}", stopWatch.toString());
		}
		
		// get data
		
		this.logger.debug("Loading data points.");
		
		stopWatch.start("loading data points");
		
		Iterator<TPoint> externalData = this.pointsource.getPoints(
				projectedTileCoordinates, 
				this.filter);
		
		stopWatch.stop();
		this.logger.debug(
				"Done loading data points: {}", 
				stopWatch.toString());
		
		// return null if no data was found
		if (externalData == null || !externalData.hasNext()) {

			this.logger.debug("No external data found for this tile: {}", tileCoordinates);
			
			if (tile == null) {
				
				this.logger.debug("No data seed available for his tile: returnung null.");
				return null;
				
			} else if (!this.returnSeedTilesWithNoExternalData) {
				
				this.logger.debug("Data seed available, but no external data found: returning null.");
				return null;
			}

		}
		
		// initializing tile if no seed is available
		if (tile == null) {
			this.logger.debug("No data seed available; initializing empty tile.");
			tile = this.tileFactory.newInstance(
					this.settings.getTileSize(), 
					projectedTileCoordinates);
		}

		// add external data to tile

		this.logger.debug("Adding data points to tile: {}", tileCoordinates);
		
		stopWatch.start("adding data points to tile");
		
		int externalDataPointCount = 0;
		while(externalData.hasNext()) {
			TPoint externalDataPoint = externalData.next();
			this.filter.filter(
					externalDataPoint, 
					tile, 
					this.settings.getTileSize(),
					tileCoordinates);
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
	public void processTiles(ITileProcessor<TTile> processor) {
		
		this.logger.debug("Processing tiles.");
		
		int min = this.settings.getZoomLevelRange().getMin();
		int max = this.settings.getZoomLevelRange().getMax();
		
		for (int zoomLevel = min; zoomLevel <= max; zoomLevel ++) {

			this.logger.debug(
					"Getting tile coordinates with content for zoom level {}.",
					zoomLevel);
			
			Iterator<TileCoordinates> iterator =
					this.pointsource.getTileCoordinatesWithContent(
							zoomLevel, 
							this.filter);
			
			this.logger.debug("Done getting tile coordinates with content.");
			
			this.logger.debug("Processing tiles on zoom level {}.", zoomLevel);
			
			int tileCount = 0;
			while (iterator.hasNext()) {
				TileCoordinates coordinates = iterator.next();
				TTile tile = this.getTile(coordinates);
				processor.process(tile, this.settings.getTileSize(), coordinates);
				tileCount ++;
			}

			this.logger.debug(
					"Done processing {} tiles on zoom level: {}", 
					tileCount, 
					zoomLevel);
		}

		processor.close();
		this.logger.debug("Done processing tiles.");
		
	}
}
