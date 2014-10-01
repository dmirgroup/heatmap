package de.uniwue.dmir.heatmap.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileProcessor;

public abstract class AbstractProcessor<TTile> implements ITileProcessor<TTile> {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
}
