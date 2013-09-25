package de.uniwue.dmir.heatmap.core.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

@AllArgsConstructor
public class HeatmapFileWriter<E extends IExternalData, I> 
implements ITileProcessor<E, I> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IVisualizer<I> visualizer;
	private IFileStrategy fileStrategy;
	private String imageFormat;
	
	@Override
	public void process(ITile<E, I> tile) {
		if (tile == null) {
			return;
		}
		
		BufferedImage image = this.visualizer.visualize(tile);
		
		File file = this.fileStrategy.getFile(tile.getCoordinates(), this.imageFormat);
		file.getParentFile().mkdirs();
		
		try {
			ImageIO.write(image, this.imageFormat, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static interface IFileStrategy {
		File getFile(TileCoordinates coordinates, String extension);
	}
	
	@AllArgsConstructor
	public static class DefaultFileStrategy implements IFileStrategy {

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

}
