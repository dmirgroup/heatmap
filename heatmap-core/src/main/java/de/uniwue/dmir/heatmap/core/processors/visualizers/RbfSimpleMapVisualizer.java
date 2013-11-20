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
import de.uniwue.dmir.heatmap.core.data.types.IDataWithRelativeCoordinates;
import de.uniwue.dmir.heatmap.core.data.types.ValuePixel;
import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.WeightedSumToAverageMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IAlphaScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.ImageColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.distances.EuclidianDistance;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.rbfs.GaussianRbf;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSum;

/**
 * TODO: add r-tree and only consider points in a vicinity to calculate point 
 * value
 * 
 * @author Martin Becker
 *
 * @param <T>
 */
public class RbfSimpleMapVisualizer<T extends IDataWithRelativeCoordinates>
extends AbstractDebuggingVisualizer<Map<RelativeCoordinates, T>> {
	
	public static final double EPSILON = 50;
	
	private IToDoubleMapper<T> toValueMapper;
	
	private IDistanceFunction<RelativeCoordinates> distanceFunction =
			new EuclidianDistance();
//
	private IRadialBasisFunction radialBasisFunction = 
			new GaussianRbf(EPSILON);
	
	private IColorScheme colorScheme;
	
	public RbfSimpleMapVisualizer(
			IToDoubleMapper<T> valueMapper,
			IToDoubleMapper<T> alphaMapper,
			IColorScheme colorScheme,
			IAlphaScheme alphaScheme) {
		
		this.toValueMapper = valueMapper;

		this.colorScheme = colorScheme;
	}
	
	public BufferedImage visualizeWithDebuggingInformation(
			Map<RelativeCoordinates, T> data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		super.logger.debug(
				"Visualizing tile of size {} x {}.", 
				tileSize.getWidth(),
				tileSize.getHeight());

		super.logger.debug(
				"Relevant points: {}.", 
				data.size());
		
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

		double max = Double.MIN_VALUE;
		ValuePixel tmp = new ValuePixel(0, 0, Double.NaN);
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				
				double sumOfValues = 0;
				double sumOfWeights = 0;
				
//				double sumOfSizes = 0;
//				double sumOfSizeWeights = 0;
				for (T p : data.values()) {
					
					tmp.setCoordinateValues(i, j);

					double distance = this.distanceFunction.distance(
							tmp.getCoordinates(), 
							p.getCoordinates());
//					System.out.println(tmp.getCoordinates());
//					System.out.println(p.getCoordinates());
//					System.out.println("distance:       " + distance);
					
					double distanceWeight = this.radialBasisFunction.value(distance);
//					System.out.println("weight:         " + weight);
					
					double value = this.toValueMapper.map(p);
//					System.out.println("value of point: " + value);
					
					double size = ((WeightedSum) p).getSize();

//					System.out.println("size:           " + size);
					sumOfValues += value * distanceWeight * size;
					sumOfWeights += distanceWeight * size;
					
//					sumOfSizes += distanceWeight * distanceWeight * size;
//					sumOfSizeWeights += distanceWeight;
				}
				
				double value = sumOfWeights == 0 ? 0 : sumOfValues / sumOfWeights;
//				double alpha = sumOfSizes / sumOfSizeWeights;
//				System.out.println(value);
//				System.out.println(alpha);
				
//				System.out.println(sumOfValues + "/" + sumOfWeights);
//				System.out.println(value);
//				System.out.println("---");
				max = Math.max(max, sumOfValues);
				
				int color = this.colorScheme.getColor(value);
				Color c = new Color(color);
				c = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) 255);
//				c = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.min(alpha * 255, 255));
				
				image.setRGB(i, j, c.getRGB());
			}
		}
		

		super.logger.debug("Done writing image values. Max Value: {}", max);
		
//		System.out.println(Arrays2d.toStringDouble(values, width, height));
		
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
		
		RbfSimpleMapVisualizer<WeightedSum> visualizer = new RbfSimpleMapVisualizer<WeightedSum>(
				new WeightedSumToAverageMapper(),
				null, 
				colorScheme, 
				null);
		BufferedImage result = visualizer.visualize(map, new TileSize(128, 128), new TileCoordinates(0, 0, 0));
		ImageIO.write(result, "png", new File("out/test.png"));
		
	}

}
