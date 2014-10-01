package de.uniwue.dmir.heatmap.heatmaps;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.DefaultTileSizeProvider;
import de.uniwue.dmir.heatmap.IHeatmap;
import de.uniwue.dmir.heatmap.ITileSizeProvider;

public abstract class AbstractHeatmap<TTile, TParameters> 
implements IHeatmap<TTile, TParameters> {
	
	@Getter
	@Setter
	protected ITileSizeProvider tileSizeProvider;


	public AbstractHeatmap() {
		this(new DefaultTileSizeProvider());
	}
	
	public AbstractHeatmap(ITileSizeProvider tileSizeProvider) {
		this.tileSizeProvider = tileSizeProvider;
	}
}
