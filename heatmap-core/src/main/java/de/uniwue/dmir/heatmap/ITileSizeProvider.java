package de.uniwue.dmir.heatmap;

public interface ITileSizeProvider {

	TileSize getTileSize(int zoom);
	
	/**
	 * {@link ITileRangeProvider}s are equal when the return the same
	 * {@link TileSize} as this provider for each zoom level.
	 * As there are no zoom level ranges, this usually means, that
	 * they have to be of the same type and have the same parameters.
	 * 
	 * @param obj the {@link ITileRangeProvider} to compare to
	 * 
	 * @return <code>true</code> if the object is a {@link ITileRangeProvider}s
	 * 		and return the same {@link TileSize} as this provider for each zoom level
	 */
	@Override
	public boolean equals(Object obj);
}
