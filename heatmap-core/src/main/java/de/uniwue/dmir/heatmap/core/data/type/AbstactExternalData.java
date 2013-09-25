package de.uniwue.dmir.heatmap.core.data.type;

import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import lombok.Data;

/**
 * Abstract implementation of a tile pixel.
 * 
 * @author Martin Becker
 */
@Data
public abstract class AbstactExternalData implements IExternalData {
	
	public AbstactExternalData(int x, int y) {
		this.coordinates = new RelativeCoordinates(x, y);
	}
	
	private RelativeCoordinates coordinates;
}
