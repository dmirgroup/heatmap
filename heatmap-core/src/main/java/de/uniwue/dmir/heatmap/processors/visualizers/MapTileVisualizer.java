package de.uniwue.dmir.heatmap.processors.visualizers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapTileVisualizer<TTile> 
extends AbstractDebuggingVisualizer<TTile> 
implements IBackgroundVisualizer<TTile> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String url;
	
	public MapTileVisualizer(String url) {
		this.url = url;
	}
	
	@Override
	public BufferedImage visualizeWithDebuggingInformation(
			TTile tile,
			TileSize tileSize, 
			TileCoordinates tileCoordinates) {
		
		try {
			
			URL url = new URL(format(this.url, tileCoordinates));
			
			this.logger.debug("Reading from URL: {}", url);
			
			BufferedImage baseLayer = ImageIO.read(url);
			
			return baseLayer;
			
		} catch (MalformedURLException e) {
			this.logger.error("Malformed URL", e);
		} catch (IOException e) {
			this.logger.error("Error reading tile.", e);
		}
		
		return null;
	}
	
	public static String format(String url, TileCoordinates tileCoordinates) {
		String formatted = url;
		formatted = formatted.replaceAll("\\{x\\}", Long.toString(tileCoordinates.getX()));
		formatted = formatted.replaceAll("\\{y\\}", Long.toString(tileCoordinates.getY()));
		formatted = formatted.replaceAll("\\{z\\}", Long.toString(tileCoordinates.getZoom()));
		return formatted;
	}
}
