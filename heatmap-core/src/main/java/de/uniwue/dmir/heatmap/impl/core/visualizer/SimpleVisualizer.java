package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.IVisualizer;
import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.impl.core.visualizer.colors.CombinedColorPipe;

@AllArgsConstructor
public class SimpleVisualizer<TTile, TPixel> 
extends AbstractDebuggingVisualizer<TTile>
implements IVisualizer<TTile> {

	private IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory;
	
	private CombinedColorPipe<TPixel> colorPipe;

	@Override
	public BufferedImage visualize(
			TTile tile, 
			TileSize tileSize,
			TileCoordinates coordinates) {

		BufferedImage bufferedImage = 
				new BufferedImage(
						tileSize.getWidth(), 
						tileSize.getHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		
		IKeyValueIterator<RelativeCoordinates, TPixel> iterator = 
				this.pixelIteratorFactory.iterator(tile);

		while (iterator.hasNext()) {
			
			iterator.next();
			
			RelativeCoordinates relativeCoordinates = iterator.getKey();
			TPixel pixel = iterator.getValue();
			
			Color color = this.colorPipe.getColor(pixel);
			
			bufferedImage.setRGB(
					relativeCoordinates.getX(), 
					relativeCoordinates.getY(), 
					color.getRGB());
		}
		
		// add debugging information
		super.addDebugInformation(tileSize, coordinates, bufferedImage);
		
		return bufferedImage;
	}

}
