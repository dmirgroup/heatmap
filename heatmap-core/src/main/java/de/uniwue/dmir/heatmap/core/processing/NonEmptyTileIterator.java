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
package de.uniwue.dmir.heatmap.core.processing;

import java.util.List;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.ZoomLevelRange;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.ExhaustiveTileIterator.DefaultAdditionalDataProvider;
import de.uniwue.dmir.heatmap.core.processing.ExhaustiveTileIterator.IAdditionalDataProvider;
import de.uniwue.dmir.heatmap.core.tile.ITile;

@AllArgsConstructor
public class NonEmptyTileIterator<E extends IExternalData, I> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IAdditionalDataProvider<I> dataProvider;

	public NonEmptyTileIterator(Class<I> dataClass) {
		this(new DefaultAdditionalDataProvider<I>(dataClass));
	}

	public void iterate(
			IHeatmap<E, I> heatmap, 
			ITileProcessor<E, I> processor) {
		
		ZoomLevelRange zoomLevelRange = heatmap.getZoomLevelRange();
		
		StopWatch outerWatch = new StopWatch();
		outerWatch.start();
		
		for (int z = zoomLevelRange.getMin(); z <= zoomLevelRange.getMax(); z++) {
			
			StopWatch innerWatch = new StopWatch();
			innerWatch.start();
			
			List<ITile<E, I>> tiles = 
					heatmap.getTiles(z, this.dataProvider, true);
			
			innerWatch.stop();
			this.logger.debug("getting tiles:    {}", innerWatch.toString());
			
			innerWatch.reset();
			innerWatch.start();
			
			for (ITile<E, I> tile : tiles) {
				processor.process(tile);
			}
			
			innerWatch.stop();
			this.logger.debug("processing {} tiles: {}", tiles.size(), innerWatch.toString());
			
		}
		
		outerWatch.stop();
		this.logger.debug("iteration done: {}", outerWatch.toString());
	}
	
	
}
