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

import java.lang.reflect.Array;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IHeatmap;
import de.uniwue.dmir.heatmap.core.IHeatmap.ZoomLevelRange;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class ExhaustiveTileIterator<E extends IExternalData, I> {

	private IAdditionalDataProvider<I> dataProvider;

	public ExhaustiveTileIterator(Class<I> dataClass) {
		this(new DefaultAdditionalDataProvider<I>(dataClass));
	}

	public void iterate(
			IHeatmap<E, I> heatmap, 
			ITileProcessor<E, I> processor) {
		
		ZoomLevelRange zoomLevelRange = heatmap.getZoomLevelRange();
		
		for (int z = zoomLevelRange.getMin(); z <= zoomLevelRange.getMax(); z++) {
			
			TileCoordinates minTileCoordinates = heatmap.getMinTileCoordiantes(z);
			TileCoordinates maxTileCoordinates = heatmap.getMaxTileCoordiantes(z);
			
			for (long x = minTileCoordinates.getX(); x <= maxTileCoordinates.getX(); x ++) {
				for (long y = minTileCoordinates.getY(); y <= maxTileCoordinates.getY(); y ++) {
					TileCoordinates tileCoordinates = new TileCoordinates(x, y, z);
					ITile<E, I> tile = heatmap.getTile(
							tileCoordinates, 
							this.dataProvider.getAdditionalData(tileCoordinates));
					processor.process(tile);
				}
			}
		}
	}
	
	public interface IAdditionalDataProvider<I> {
		I[] getAdditionalData(TileCoordinates coordinates);
	}
	
	@AllArgsConstructor
	public static class DefaultAdditionalDataProvider<I>
	implements IAdditionalDataProvider<I> {

		private Class<I> dataClass;
		
		@Override
		@SuppressWarnings("unchecked")
		public I[] getAdditionalData(TileCoordinates coordinates) {
			return (I[]) Array.newInstance(this.dataClass, 0);
		}
		
	}
}
