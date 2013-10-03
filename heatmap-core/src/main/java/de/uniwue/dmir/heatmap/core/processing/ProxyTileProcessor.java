package de.uniwue.dmir.heatmap.core.processing;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@Data
@AllArgsConstructor
public class ProxyTileProcessor<I> implements ITileProcessor<I> {

	private List<ITileProcessor<I>> processors;
	
	@Override
	public void process(I tile, TileSize tileSize, TileCoordinates coordinates) {
		for (ITileProcessor<I> p : this.processors) {
			p.process(tile, tileSize, coordinates);
		}
	}

}
