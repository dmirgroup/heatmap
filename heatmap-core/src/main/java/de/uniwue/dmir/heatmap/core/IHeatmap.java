package de.uniwue.dmir.heatmap.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.processing.ExhaustiveTileIterator.IAdditionalDataProvider;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * <h1>Tiles</h1>
 * <p>Overlays of maps are commonly built separately for each zoom level (note 
 * that a map may also be a text document or a web page with only one zoom level).
 * On each zoom level, the overlay is divided into equally sized tiles to 
 * support partial access.
 * Thus, each tile is uniquely identified by its zoom level and its coordinates 
 * in the tile space (x, y, zoom).
 * 
 * <p>For example 
 * the top-left tile on zoom level z is identified by (0,0,z), 
 * the tile to the right of (0,0,z) is identified by (1,0,z).</p>
 * 
 * <p>Note that by using a {@link ITileCoordinatesProjection} the default
 * top-left oriented way of indexing tiles can be altered.</p>
 * 
 * <h1>Process</h1>
 * We need the following components are needed to define a heatmap:
 * <ul>
 * 	<li>
 * 		the heat map's dimensions are set via {@link IHeatmapDimensions},
 * 	</li>
 * 	<li>
 * 		an {@link IExternalDataSource}, to get the data according to tile coordinates
 * 	</li>
 * 	<li>
 * 		a {@link IFilter} to merge data points into a tile.
 * 	</li>
 * </ul>
 * 
 * Heat maps can be written to images using the {@link IHeatmapWriter} and a
 * matching {@link IVisualizer}.
 * 
 * 
 * @author Martin Becker
 */
public interface IHeatmap<E extends IExternalData, I> {
	
	/**
	 * 
	 * @param coordinates
	 * @param additionalData
	 * @return <code>null</code> if the tile does not contain data; 
	 * 		the tile corresponding to the coordinates otherwise
	 */
	ITile<E, I> getTile(TileCoordinates coordinates, I[] additionalData);
	
	List<ITile<E, I>> getTiles(
			int zoom,
			IAdditionalDataProvider<I> additionalDataProvider, 
			boolean excludeEmptyTiles);
	
	ZoomLevelRange getZoomLevelRange();
	IHeatmapDimensions getDimensions();
	
	TileCoordinates getMinTileCoordiantes(int zoom);
	TileCoordinates getMaxTileCoordiantes(int zoom);
	
	@Data
	@AllArgsConstructor
	public static class ZoomLevelRange {
		private int min;
		private int max;
	}
}
