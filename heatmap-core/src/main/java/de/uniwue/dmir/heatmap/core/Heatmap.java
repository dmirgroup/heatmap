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
import java.util.List;

import lombok.Getter;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.Tile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class Heatmap<E extends IExternalData, I> 
implements IHeatmap<E, I>{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IExternalDataSource<E> dataSource;
	private IFilter<E, I> filter;
	
	@Getter
	private HeatmapSettings settings;
	
	public Heatmap(
			IExternalDataSource<E> dataSource,
			IFilter<E, I> filter,
			HeatmapSettings settings) {
		
		this.dataSource = dataSource;
		this.filter = filter;
		this.settings = settings;
	}
	
	@Override
	public ITile<E, I> getTile(TileCoordinates coordinates, I[] additionalData) {
		
		// tile coordinates to top-left reference system
		TileCoordinates projectedCoordinates =
				this.settings.getTileProjection().fromCustomToTopLeft(
						coordinates, 
						this.settings.getZoomLevelMapper());
		
		
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
				this.settings.getTileSize(), 
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
	public Iterator<TileCoordinates> getTileCoordinatesWithContent(int zoom) {
		return this.dataSource.getNonEmptyTiles(zoom, zoom, this.filter).get(zoom).iterator();
	}
}
