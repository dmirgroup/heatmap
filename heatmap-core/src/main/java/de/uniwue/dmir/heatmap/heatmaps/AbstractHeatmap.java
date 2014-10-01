package de.uniwue.dmir.heatmap.heatmaps;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.IZoomLevelSizeProvider;

public abstract class AbstractHeatmap<TTile, TParameters> 
implements IHeatmap<TTile, TParameters> {
	
	@Getter
	@Setter
	protected IZoomLevelSizeProvider zoomLevelSizeProvider;
	
	@Getter
	@Setter
	protected ITileSizeProvider tileSizeProvider;

	public AbstractHeatmap(
			IZoomLevelSizeProvider zoomLevelSizeProvider,
			ITileSizeProvider tileSizeProvider) {
		
		this.zoomLevelSizeProvider = zoomLevelSizeProvider;
		this.tileSizeProvider = tileSizeProvider;
	}
}
