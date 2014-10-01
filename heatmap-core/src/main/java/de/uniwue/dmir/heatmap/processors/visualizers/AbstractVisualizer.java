package de.uniwue.dmir.heatmap.processors.visualizers;

import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.IVisualizer;

public abstract class AbstractVisualizer<TTile> implements IVisualizer<TTile> {

	protected ITileSizeProvider tileSizeProvider;
	
	public AbstractVisualizer(ITileSizeProvider tileSizeProvider) {
		super();
		this.tileSizeProvider = tileSizeProvider;
	}
	
	@Override
	public ITileSizeProvider getTileSizeProvider() {
		return this.tileSizeProvider;
	}
	
}
