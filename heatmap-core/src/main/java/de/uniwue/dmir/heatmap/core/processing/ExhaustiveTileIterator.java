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
