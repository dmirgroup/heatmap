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
package de.uniwue.dmir.heatmap.core.processors.visualizers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.processors.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.core.processors.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.WeightedSumToAverageMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.ImageColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.ReferencedData;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators.MaxRbfAggregator;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators.QuadraticRbfAggregator;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSum;
import de.uniwue.dmir.heatmap.core.util.IAggregator;
import de.uniwue.dmir.heatmap.core.util.MapKeyValueIteratorFactory;

/**
 * TODO: add r-tree and only consider points in a vicinity to calculate point 
 * value
 * 
 * @author Martin Becker
 *
 * @param <T>
 */
public class GenericSimpleRbfVisualizer<TTile, TPixel>
extends AbstractGenericVisualizer<TTile, TPixel> {
	
	private IAggregator<ReferencedData<TPixel>, Double> aggegator;
	
	private IColorScheme colorScheme;
	
	public GenericSimpleRbfVisualizer(
			IKeyValueIteratorFactory<TTile, RelativeCoordinates, TPixel> pixelIteratorFactory,
			IAggregator<ReferencedData<TPixel>, Double> aggregator,
			IColorScheme colorScheme) {
		
		super(pixelIteratorFactory);
		
		this.aggegator = aggregator;
		this.colorScheme = colorScheme;
	}
	
	public BufferedImage visualizeWithDebuggingInformation(
			TTile data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		super.logger.debug(
				"Visualizing tile of size {} x {}.", 
				tileSize.getWidth(),
				tileSize.getHeight());

		// create matrices
		
		int width = tileSize.getWidth();
		int height = tileSize.getHeight();
		
		// initialize buffered image 

		super.logger.debug("Initializing image.");
		
		BufferedImage image = new BufferedImage(
				width,
				height,
				BufferedImage.TYPE_INT_ARGB);
		
		// write image values

		super.logger.debug("Writing image values.");

		ReferencedData<TPixel> referencedData = new ReferencedData<TPixel>();
		referencedData.setReferenceCoordaintes(new RelativeCoordinates(0, 0));
		
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				
				this.aggegator.reset();
				
				referencedData.getReferenceCoordaintes().setXY(i, j);

				IKeyValueIterator<RelativeCoordinates, TPixel> iterator =
						this.pixelIteratorFactory.iterator(data);
				
				while (iterator.hasNext()) { 
					
					iterator.next();
					referencedData.setDataCoordinates(iterator.getKey());
					referencedData.setData(iterator.getValue());
					
					this.aggegator.addData(referencedData);
				}
				
				
				double value = this.aggegator.getAggregate();
				int color = this.colorScheme.getColor(value);
				image.setRGB(i, j, color);
			}
		}
		
		super.logger.debug("Returning image.");
		
		return image;
	}
	
	public static void main(String[] args) throws IOException {
		
		Map<RelativeCoordinates, WeightedSum> map = 
				new HashMap<RelativeCoordinates, WeightedSum>();
		
		WeightedSum s1 = new WeightedSum(9);
		s1.getCoordinates().setX(100);
		s1.getCoordinates().setY(80);
		map.put(s1.getCoordinates(), s1);
		
		WeightedSum s2 = new WeightedSum(5);
		s2.getCoordinates().setX(80);
		s2.getCoordinates().setY(90);
		map.put(s2.getCoordinates(), s2);
		

		WeightedSum s3 = new WeightedSum(1);
		s3.getCoordinates().setX(40);
		s3.getCoordinates().setY(40);
		map.put(s3.getCoordinates(), s3);
		
		BufferedImage colorImage = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = ImageColorScheme.equdistantRanges(0, 9, colorImage.getHeight());
		ImageColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSum>, WeightedSum> visualizerColor = 
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSum>, WeightedSum>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSum>(),
						new QuadraticRbfAggregator<WeightedSum>(
								new WeightedSumToAverageMapper(), 
								10),
						colorScheme);
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSum>, WeightedSum> visualizerAlpha = 
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSum>, WeightedSum>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSum>(),
						new MaxRbfAggregator<WeightedSum>(
								new IToDoubleMapper<WeightedSum>() {
									
									@Override
									public Double map(WeightedSum object) {
										if (object != null && object.getSize() > 0) {
											return 1.;
										} else {
											return 0.;
										}
									}
									
								},
								20),
						new IColorScheme() {
							
							@Override
							public int getColor(double value) {
								Color color = new Color(0,0,0, (int) (value * 255));
								return color.getRGB();
							}
							
						});
		
		AlphaMaskProxyVisualizer<Map<RelativeCoordinates, WeightedSum>> proxyVisualizer =
				new AlphaMaskProxyVisualizer<Map<RelativeCoordinates,WeightedSum>>(
						visualizerColor, 
						visualizerAlpha);
		
		BufferedImage result = proxyVisualizer.visualize(
				map, 
				new TileSize(128, 128),
				new TileCoordinates(0, 0, 0));
		
		ImageIO.write(result, "png", new File("out/test.png"));
		
	}

}

