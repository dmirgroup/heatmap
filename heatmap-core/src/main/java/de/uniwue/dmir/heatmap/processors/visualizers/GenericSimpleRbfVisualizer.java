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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.newbrightidea.util.RTree;

import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.processors.pixelmappers.WeightedSumToAverageMapper;
import de.uniwue.dmir.heatmap.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.processors.visualizers.color.ImageColorScheme;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.ReferencedData;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.aggregators.DefaultRbfAggregator;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.aggregators.MaxRbfAggregator;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.tiles.pixels.WeightedSumPixel;
import de.uniwue.dmir.heatmap.util.IAggregator;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.iterator.MapKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory.IKeyValueIterator;

/**
 * TODO: test r-tree
 * 
 * @author Martin Becker
 *
 * @param <T>
 */
public class GenericSimpleRbfVisualizer<TTile, TPixel>
extends AbstractGenericVisualizer<TTile, TPixel> {
	
	private IAggregator<ReferencedData<TPixel>, Double> aggegator;
	
	private IColorScheme colorScheme;
	
	private boolean useRtree;
	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
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
		
		// prepare R-Tree
		RTree<ReferencedData<TPixel>> rtree = null;
		
		if (this.useRtree) {
			rtree = new RTree<ReferencedData<TPixel>>();
			
			IKeyValueIterator<RelativeCoordinates, TPixel> iterator =
					this.pixelIteratorFactory.instance(data);
			
			while (iterator.hasNext()) { 
	
				iterator.next();
				
				ReferencedData<TPixel> referencedData = new ReferencedData<TPixel>();
				referencedData.setReferenceCoordinates(new RelativeCoordinates(0, 0));
				referencedData.setDataCoordinates(iterator.getKey());
				referencedData.setData(iterator.getValue());
				
				rtree.insert(
						new float[] {
							referencedData.getDataCoordinates().getX(),
							referencedData.getDataCoordinates().getY()
						}, 
						referencedData);
			}

		}
		
		// write image values

		super.logger.debug("Writing image values.");

		ReferencedData<TPixel> referencedData = new ReferencedData<TPixel>();
		referencedData.setReferenceCoordinates(new RelativeCoordinates(0, 0));
		
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				
				this.aggegator.reset();
				
				if (this.useRtree) {
					List<ReferencedData<TPixel>> neighbours = rtree.search(
							new float[] {
								0 - this.centerX,
								0 - this.centerY
							}, 
							new float[] {
								this.width,
								this.height
							});
					
					for (ReferencedData<TPixel> r : neighbours) {
						r.getReferenceCoordinates().setXY(i, j);
						this.aggegator.addData(referencedData);
					}
					
				} else {
					referencedData.getReferenceCoordinates().setXY(i, j);
	
					IKeyValueIterator<RelativeCoordinates, TPixel> iterator =
							this.pixelIteratorFactory.instance(data);
					
					while (iterator.hasNext()) { 
						
						iterator.next();
						referencedData.setDataCoordinates(iterator.getKey());
						referencedData.setData(iterator.getValue());
						
						this.aggegator.addData(referencedData);
					}
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
		
		Map<RelativeCoordinates, WeightedSumPixel> map = 
				new HashMap<RelativeCoordinates, WeightedSumPixel>();
		
		WeightedSumPixel s1 = new WeightedSumPixel(9);
		map.put(new RelativeCoordinates(100, 80), s1);
		
		WeightedSumPixel s2 = new WeightedSumPixel(5);
		map.put(new RelativeCoordinates(80, 90), s2);

		WeightedSumPixel s3 = new WeightedSumPixel(1);
		map.put(new RelativeCoordinates(40, 40), s3);
		
		BufferedImage colorImage = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = ImageColorScheme.equdistantRanges(0, 9, colorImage.getHeight());
		ImageColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel> visualizerColor = 
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSumPixel>(),
						new DefaultRbfAggregator<WeightedSumPixel>(
								new WeightedSumToAverageMapper(), 
								30),
						colorScheme);
		
		GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel> visualizerAlpha = 
				new GenericSimpleRbfVisualizer<Map<RelativeCoordinates, WeightedSumPixel>, WeightedSumPixel>(
						new MapKeyValueIteratorFactory<RelativeCoordinates, WeightedSumPixel>(),
						new MaxRbfAggregator<WeightedSumPixel>(
								new IMapper<WeightedSumPixel, Double>() {
									
									@Override
									public Double map(WeightedSumPixel object) {
										if (object != null && object.getSize() > 0) {
											return 1.;
										} else {
											return 0.;
										}
									}
									
								},
								10),
						new IColorScheme() {
							
							@Override
							public int getColor(double value) {
								Color color = new Color(0,0,0, (int) (value * 255));
								return color.getRGB();
							}
							
						});
		
		AlphaMaskProxyVisualizer<Map<RelativeCoordinates, WeightedSumPixel>> proxyVisualizer =
				new AlphaMaskProxyVisualizer<Map<RelativeCoordinates,WeightedSumPixel>>(
						visualizerColor, 
						visualizerAlpha);
		
		BufferedImage result = proxyVisualizer.visualize(
				map, 
				new TileSize(128, 128),
				new TileCoordinates(0, 0, 0));
		
		ImageIO.write(result, "png", new File("out/test.png"));
		
	}

}

