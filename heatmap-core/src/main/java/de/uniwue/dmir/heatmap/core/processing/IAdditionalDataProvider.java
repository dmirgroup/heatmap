package de.uniwue.dmir.heatmap.core.processing;

import java.lang.reflect.Array;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public interface IAdditionalDataProvider<I> {
	I[] getAdditionalData(TileCoordinates coordinates);
	
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


