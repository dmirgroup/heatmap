package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf;

import lombok.Data;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;

@Data
public class ReferencedData<TData> {
	private RelativeCoordinates referenceCoordaintes;
	private RelativeCoordinates dataCoordinates;
	private TData data;
}
