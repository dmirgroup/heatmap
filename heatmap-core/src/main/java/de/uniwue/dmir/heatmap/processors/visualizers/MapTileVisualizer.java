/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.processors.visualizers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileSizeProvider;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class MapTileVisualizer<TTile> 
extends AbstractDebuggingVisualizer<TTile> 
implements IBackgroundVisualizer<TTile> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String url;
	
	public MapTileVisualizer(ITileSizeProvider tileSizeProvider, String url) {
		super(tileSizeProvider);
		this.url = url;
	}
	
	@Override
	public BufferedImage visualizeWithDebuggingInformation(
			TTile tile,
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
