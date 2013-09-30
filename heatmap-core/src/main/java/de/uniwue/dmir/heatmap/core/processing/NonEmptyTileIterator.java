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

import java.util.Iterator;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.ZoomLevelRange;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.IAdditionalDataProvider.DefaultAdditionalDataProvider;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class NonEmptyTileIterator<E extends IExternalData, I> 
implements ITileIterator<E, I>  {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IAdditionalDataProvider<I> dataProvider;

	public NonEmptyTileIterator(Class<I> dataClass) {
		this(new DefaultAdditionalDataProvider<I>(dataClass));
	}

	public void iterate(
			IHeatmap<E, I> heatmap, 
			ITileProcessor<E, I> processor) {
		
		ZoomLevelRange zoomLevelRange = heatmap.getSettings().getZoomLevelRange();
		
		StopWatch outerWatch = new StopWatch();
		outerWatch.start();
		
		for (int z = zoomLevelRange.getMin(); z <= zoomLevelRange.getMax(); z++) {
			
			StopWatch innerWatch = new StopWatch();
			innerWatch.start();
			
			Iterator<TileCoordinates> tileCoordinates =
					heatmap.getTileCoordinatesWithContent(z);
			
			innerWatch.stop();
			this.logger.debug("getting tile coordinate iterator:    {}", innerWatch.toString());
			
			innerWatch.reset();
			innerWatch.start();
			
			StopWatch tileCoordinateStopWatch = new StopWatch();
			StopWatch additionalDataStopWatch = new StopWatch();
			StopWatch tileRetrievalStopWatch = new StopWatch();
			StopWatch processingStopWatch = new StopWatch();
			
			int tileCount = 0;
			while (tileCoordinates.hasNext()) {
				
				tileCoordinateStopWatch.start();
				TileCoordinates coordinates = tileCoordinates.next();
				tileCoordinateStopWatch.stop();
				
				additionalDataStopWatch.start();
				I[] additionalData = this.dataProvider.getAdditionalData(coordinates);
				additionalDataStopWatch.stop();
				
				tileRetrievalStopWatch.start();
				ITile<E, I> tile = heatmap.getTile(coordinates, additionalData);
				tileRetrievalStopWatch.stop();
				
				processingStopWatch.start();
				processor.process(tile);
				processingStopWatch.stop();
				
				tileCount ++;
			}
			
			innerWatch.stop();
			this.logger.debug("processing {} tiles: {}", tileCount, innerWatch.toString());
			this.logger.debug("- getting tile coordinates: {}", tileCoordinateStopWatch.toString());
			this.logger.debug("- getting additional data:  {}", additionalDataStopWatch.toString());
			this.logger.debug("- getting tiles:            {}", tileRetrievalStopWatch.toString());
			this.logger.debug("- processing tiles:         {}", processingStopWatch.toString());
		}
		
		outerWatch.stop();
		this.logger.debug("iteration done: {}", outerWatch.toString());
	}
	
	
}
