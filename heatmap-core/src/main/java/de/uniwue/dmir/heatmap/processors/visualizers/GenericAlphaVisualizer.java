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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import lombok.Setter;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory.IKeyValueIterator;

public class GenericAlphaVisualizer<TTile, TPixel>
extends AbstractGenericVisualizer<TTile, TPixel> {

	public static final double DEFAULT_SCALE_FACTOR = 100;
	public static final float DEFAULT_ALPHA_VALUE = 0.7f;
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 0);

	private IMapper<TPixel, Double> pixelToSumMapper;

	@Setter
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
	
	@Setter
	private float alphaValue = DEFAULT_ALPHA_VALUE;
	
	@Setter
	private boolean forceAlphaValue = false;
	
	private double scalingFactor = DEFAULT_SCALE_FACTOR;
	
	private IColorScheme colorScheme;
	
	public GenericAlphaVisualizer(
			IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory,
			IColorScheme colorScheme) {
		
		super(pixelIteratorFactory);
		this.colorScheme = colorScheme;
	}
	
	public GenericAlphaVisualizer(
			IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory,
			double scaleFactor) {

		super(pixelIteratorFactory);
		this.scalingFactor = scaleFactor;
	}
	
	public GenericAlphaVisualizer(
			IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory) {
		
		this(pixelIteratorFactory, DEFAULT_SCALE_FACTOR);
	}
	
	
	public BufferedImage visualizeWithDebuggingInformation(
			TTile data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		int width = tileSize.getWidth();
		int height = tileSize.getHeight();
		
		// initialize buffered image 
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		g.setColor(this.backgroundColor);
		image.getGraphics().fillRect(
				0, 0, 
				image.getWidth() - 1, image.getHeight() - 1);
		
		// run through pixels
		
		IKeyValueIterator<RelativeCoordinates, TPixel> iterator = 
				this.pixelIteratorFactory.instance(data);
		
		while (iterator.hasNext()) {
			
			iterator.next();
			RelativeCoordinates relativeCoordinates = iterator.getKey();
			TPixel pixel = iterator.getValue();
			
			Double sum = this.pixelToSumMapper.map(pixel);
			
			if (this.colorScheme == null) {
				
				// we are using gray scale now
				
				if (sum != null && sum > 0) {
					
					// scale the given value by the configured scaling factor 
					// the corresponding value may not exceed 0
					float value = (float) Math.min(sum / this.scalingFactor, 1);
					
					// draw the pixel
					
					Color color = new Color(value, value, value, this.alphaValue);
					image.setRGB(
							relativeCoordinates.getX(), 
							relativeCoordinates.getY(), 
							color.getRGB());
					
				}
			} else {
				
				// we are using the given color scheme
				
				if (sum != null) {
					
					// get color index
					int color = this.colorScheme.getColor(sum);
					
					// we may want to force a configured alpha value
					if (this.forceAlphaValue) {
						Color alphaColor = new Color(color);
						alphaColor = new Color(
								alphaColor.getRed(), 
								alphaColor.getGreen(), 
								alphaColor.getBlue(), 
								(int) (this.alphaValue * 255));
						color = alphaColor.getRGB();
					}
					
					// draw the pixel
					image.setRGB(
							relativeCoordinates.getX(), 
							relativeCoordinates.getY(),
							color);
					
				} 
			}
			
		}
		
		return image;
	}

}
