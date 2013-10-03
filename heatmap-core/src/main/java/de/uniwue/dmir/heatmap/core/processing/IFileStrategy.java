package de.uniwue.dmir.heatmap.core.processing;

import java.io.File;

import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public interface IFileStrategy {
	File getFile(TileCoordinates coordinates, String extension);
}