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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.types.IDataWithRelativeCoordinates;
import de.uniwue.dmir.heatmap.core.data.types.ValuePixel;
import de.uniwue.dmir.heatmap.core.processors.IToDoubleMapper;
import de.uniwue.dmir.heatmap.core.processors.WeightedSumToAverageMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IAlphaScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.EuclidianDistance;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.GaussianRbf;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSum;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

public class RbfMapVisualizer<T extends IDataWithRelativeCoordinates>
extends AbstractDebuggingVisualizer<Map<RelativeCoordinates, T>> {
	
	public static final double EPSILON = 0.1;
	
	private IToDoubleMapper<T> toValueMapper;
	private IToDoubleMapper<T> toAlphaMapper;
	
	private IDistanceFunction<IDataWithRelativeCoordinates> distanceFunction =
			new EuclidianDistance();
//
	private IRadialBasisFunction radialBasisFunction = 
			new GaussianRbf(EPSILON);
	
	private IColorScheme colorScheme;
	private IAlphaScheme alphaScheme;
	
	public RbfMapVisualizer(
			IToDoubleMapper<T> valueMapper,
			IToDoubleMapper<T> alphaMapper,
			IColorScheme colorScheme,
			IAlphaScheme alphaScheme) {
		
		this.toValueMapper = valueMapper;
		this.toAlphaMapper = alphaMapper;

		this.colorScheme = colorScheme;
		this.alphaScheme = alphaScheme;
	}
	
	public BufferedImage visualize(
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
		
		DenseMatrix64F A = new DenseMatrix64F(data.size(), data.size());
		DenseMatrix64F x = new DenseMatrix64F(data.size(), 1);
		DenseMatrix64F b = new DenseMatrix64F(data.size(), 1);
		
		// setting A and b
		
		super.logger.debug("Building matrices for linear equation.");
		
		int row = 0;
		for (T rowPixel : data.values()) {
			
			double bValue = this.toValueMapper.map(rowPixel);
			b.set(row, bValue);
			System.out.println(bValue);
			
			int col = 0;
			for (T colPixel : data.values()) {
				
				double distance = this.distanceFunction.distance(rowPixel, colPixel);
				
				double value = this.radialBasisFunction.value(distance);
				
				A.set(row, col, value);
				
				col ++;
			}
			
			row ++;
		}
		
		// solve linear equation system
		
		super.logger.debug("Solving linear equation.");
		
		if( !CommonOps.solve(A,b,x) ) {
			throw new IllegalArgumentException("A was a singular matrix.");
		}
		
		// width and height shortcuts
		
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
				
				double sum = 0;
				int index = 0;
				for (T p : data.values()) {
					
					tmp.setCoordinateValues(i, j);
					
					double distance = this.distanceFunction.distance(tmp, p);
					double value = this.radialBasisFunction.value(distance);

					double weight = x.get(index);
					
					sum += weight * value;
					
					index ++;
				}
				
				max = Math.max(max, sum);
				int color = this.colorScheme.getColor(sum);
				image.setRGB(i, j, color);
			}
		}
		

		super.logger.debug("Done writing image values. Max Value: {}", max);
		
//		System.out.println(Arrays2d.toStringDouble(values, width, height));
		
		// debugging
		this.addDebugInformation(tileSize, coordinates, image);

		super.logger.debug("Returning image.");
		
		return image;
	}
	
	public static void main(String[] args) throws IOException {
		
		Map<RelativeCoordinates, WeightedSum> map = 
				new HashMap<RelativeCoordinates, WeightedSum>();
		
		WeightedSum s1 = new WeightedSum(9);
		s1.getCoordinates().setX(3);
		s1.getCoordinates().setY(3);
		map.put(s1.getCoordinates(), s1);
		
		WeightedSum s2 = new WeightedSum(5);
		s2.getCoordinates().setX(5);
		s2.getCoordinates().setY(7);
		map.put(s2.getCoordinates(), s2);
		
		BufferedImage colorImage = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = ImageColorScheme.ranges(0, 9, colorImage.getHeight());
		ImageColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
		
		RbfMapVisualizer<WeightedSum> visualizer = new RbfMapVisualizer<WeightedSum>(
				new WeightedSumToAverageMapper(),
				null, 
				colorScheme, 
				null);
		BufferedImage result = visualizer.visualize(map, new TileSize(9, 9), new TileCoordinates(0, 0, 0));
		ImageIO.write(result, "png", new File("out/test.png"));
	}

}

