package de.uniwue.dmir.heatmap.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.ITileCoordinatesProjection.IdentityTileCoordinatesProjection;

@Data
@AllArgsConstructor
public class HeatmapSettings {
	
	private TileSize tileSize;
	private ZoomLevelRange zoomLevelRange;
	private IZoomLevelMapper zoomLevelMapper;
	private ITileCoordinatesProjection tileProjection;
	
	public HeatmapSettings() {
		
		this(
				new TileSize(
					IHeatmap.DEFAULT_TILE_WIDTH, 
					IHeatmap.DEFAULT_TILE_HEIGHT),
		
				new ZoomLevelRange(
					IHeatmap.DEFAULT_MIN_ZOOM_LEVEL, 
					IHeatmap.DEFAULT_MAX_ZOOM_LEVEL),
		
				new DefaultZoomLevelMapper(),
		
				new IdentityTileCoordinatesProjection());
	}
}