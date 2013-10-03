package de.uniwue.dmir.heatmap.core.processing;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@Data
@AllArgsConstructor
public class DefaultFileStrategy implements IFileStrategy {

	private String folder;
	
	public DefaultFileStrategy() {
		this(null);
	}
	
	@Override
	public File getFile(TileCoordinates coordinates, String extension) {
		String file = String.format("%d/%d/%d.%s",
				coordinates.getZoom(),
				coordinates.getX(),
				coordinates.getY(),
				extension);
		
		if (this.folder == null) {
			return new File(file);
		} else {
			return new File(this.folder, file);
		}
	}
	
}