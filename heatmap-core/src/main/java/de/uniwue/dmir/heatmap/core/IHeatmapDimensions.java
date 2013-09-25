package de.uniwue.dmir.heatmap.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public interface IHeatmapDimensions {
	
	public static final int DEFAULT_TILE_WIDTH = 256;
	public static final int DEFAULT_TILE_HEIGHT = 256;
	
	TileDimensions getTileDimensions();
	GridDimensions getGridDimensions(int zoom);
	
	@Data
	@AllArgsConstructor
	public class TileDimensions {
		private int width;
		private int height;
	}
	
	@Data
	@AllArgsConstructor
	public class GridDimensions {
		private long width;
		private long height;
	}
	
	public class DefaultHeatmapDimensions 
	implements IHeatmapDimensions {

		@Getter
		private TileDimensions tileDimensions =
				new TileDimensions(
						DEFAULT_TILE_WIDTH, 
						DEFAULT_TILE_HEIGHT);
		
		public GridDimensions getGridDimensions(int zoom) {
			int xy = (1 << zoom); // log scale using base 2
			return new GridDimensions(xy, xy);
		}

	}
}
