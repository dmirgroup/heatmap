package de.uniwue.dmir.heatmap;

import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TileRange {
	
	private long minX;
	private long maxX;
	
	private long minY;
	private long maxY;
	
	public boolean isInRange(TileCoordinates coordinates) {
		return 
				this.minX <= coordinates.getX() 
				&& this.maxX >= coordinates.getX()
				&& this.minY <= coordinates.getY()
				&& this.maxY >= coordinates.getY();
	}
}
