package de.uniwue.dmir.heatmap.filters.pointmappers;

/**
 * Pointmappers are used by {@link de.uniwue.dmir.heatmap.core.IFilter}s 
 * to map data points to pixels stored by tiles.
 * Those pixels are then combined with the existing pixels within the tile.
 * While this mapping step is not necessary in general and filters can 
 * combined the given data directly, it allows filters to operate on arbitrary
 * data types given the correct pointmapper.
 */
