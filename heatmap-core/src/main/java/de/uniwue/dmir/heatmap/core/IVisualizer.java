package de.uniwue.dmir.heatmap.core;

import java.awt.image.BufferedImage;

import de.uniwue.dmir.heatmap.core.tile.ITile;

public interface IVisualizer<I> {
	BufferedImage visualize(ITile<?, I> tile);
}
