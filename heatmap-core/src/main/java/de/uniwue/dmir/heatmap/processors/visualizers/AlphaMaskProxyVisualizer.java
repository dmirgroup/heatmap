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

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

/**
 * Visualizer which takes the replaces the alpha channel of one visualization 
 * with an arbitrary channel from another visualization.
 * 
 * @author Martin Becker
 *
 */
public class AlphaMaskProxyVisualizer<TTile> 
extends AbstractDebuggingVisualizer<TTile> {

	public static final Channel DEFAULT_ALPHA_CHANNEL = Channel.ALPHA;
	
	private IVisualizer<TTile> color;
	private IVisualizer<TTile> alpha;
	
	@Getter
	@Setter
	private Channel alphaChannel = DEFAULT_ALPHA_CHANNEL;
	
	public AlphaMaskProxyVisualizer(
			IVisualizer<TTile> color,
			IVisualizer<TTile> alpha) {
		
		this.color = color;
		this.alpha = alpha;
	}

	@Override
	public BufferedImage visualizeWithDebuggingInformation(
			TTile tile,
			TileSize tileSize, 
			TileCoordinates tileCoordinates) {
		
		BufferedImage colorImage = this.color.visualize(tile, tileSize, tileCoordinates);
		BufferedImage alphaImage = this.alpha.visualize(tile, tileSize, tileCoordinates);
		
		applyGrayscaleMaskToAlpha(colorImage, alphaImage, this.alphaChannel);
		
		return colorImage;
	}

	/**
	 * Source: <a href="http://stackoverflow.com/questions/221830/set-bufferedimage-alpha-mask-in-java">http://stackoverflow.com/questions/221830/set-bufferedimage-alpha-mask-in-java</a>
	 * @param image
	 * @param mask
	 */
	public static void applyGrayscaleMaskToAlpha(
			BufferedImage image,
			BufferedImage mask,
			Channel channel) {

		int width = image.getWidth();
		int height = image.getHeight();

		int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

		int selector;
		int shift;
		switch (channel) {
		case RED:
			selector = 0x00ff0000;
			shift = 8;
			break;
		case GREEN:
			selector = 0x0000ff00;
			shift = 16;
			break;
		case BLUE:
			selector = 0x000000ff;
			shift = 24;
			break;
		default:
			selector = 0xff000000;
			shift = 0;
			break;
		}
		
		for (int i = 0; i < imagePixels.length; i++) {

			int color = imagePixels[i] & 0x00ffffff; // mask pre-existing alpha
			int alpha = (maskPixels[i] & selector) << shift; // shift color to alpha
			imagePixels[i] = color | alpha;
		}

		image.setRGB(0, 0, width, height, imagePixels, 0, width);
	}
	
	public enum Channel {
		RED,
		GREEN,
		BLUE,
		ALPHA
	}
	
	
}
