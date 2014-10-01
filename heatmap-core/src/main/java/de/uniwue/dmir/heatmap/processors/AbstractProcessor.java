package de.uniwue.dmir.heatmap.processors;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.ITileSizeProvider;

public abstract class AbstractProcessor<TTile> implements ITileProcessor<TTile> {

	protected ITileSizeProvider tileSizeProvider;
	
	public AbstractProcessor(ITileSizeProvider tileSizeProvider) {
		this.tileSizeProvider = tileSizeProvider;
	}
	
	@Override
	public ITileSizeProvider getTileSizeProvider() {
		// TODO Auto-generated method stub
		return null;
	}


}
