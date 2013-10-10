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
package de.uniwue.dmir.heatmap.impl.core.visualizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.WeightedSum;

public class RbfVisualizer
extends AbstractDebuggingVisualizer<WeightedSum[]> {
	
	public static final double EPSILON = 0.5;
	
	private IDistanceFunction<IExternalData> distanceFunction =
			new IDistanceFunction<IExternalData>() {

				@Override
				public double distance(IExternalData o1, IExternalData o2) {
					return Math.sqrt(
							 Math.pow(o1.getCoordinates().getX() - o2.getCoordinates().getX(), 2)
							+ Math.pow(o1.getCoordinates().getY() - o2.getCoordinates().getY(), 2));
				}
				
			};

	private IRadialBasicFunction radialBasicFunction = new IRadialBasicFunction() {
		
		@Override
		public double value(double value, double epsilon) {
			return Math.exp(-(value * epsilon) * (value * epsilon));
		}
	}; 
	
	private BufferedImage colorScheme;
	private double[] ranges;
	
	public RbfVisualizer(
			BufferedImage colorScheme,
			double ranges[]) {
		this.colorScheme = colorScheme;
		this.ranges = ranges;
	}
	
	private int colorIndex(double value) {
		for (int i = 0; i < this.ranges.length; i ++) {
			if (value < this.ranges[i]) {
				return i;
			}
		}
		return this.colorScheme.getHeight() - 1;
	}
	
	public BufferedImage visualize(
			WeightedSum[] data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		int width = tileSize.getWidth();
		int height = tileSize.getHeight();
		
		// get pixels with content
		
		//TODO: maybe we should use a list of ValuePixels as tiles to we can skip this part
		List<ValuePixel> pixels = new ArrayList<ValuePixel>();
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				WeightedSum sum = Arrays2d.get(i, j, data, width, height);
				if (sum != null) {
					double value = sum.getSumOfValues() / sum.getSize();
					ValuePixel pixel = new ValuePixel(i, j, value);
					pixels.add(pixel);
				}
			}
		}
		
		// create matrices
		
		DenseMatrix64F A = new DenseMatrix64F(pixels.size(), pixels.size());
		DenseMatrix64F x = new DenseMatrix64F(pixels.size(), 1);
		DenseMatrix64F b = new DenseMatrix64F(pixels.size(), 1);
		
		// setting A
		int row = 0;
		for (ValuePixel rowPixel : pixels) {
			
			b.set(row, rowPixel.getValue());
			
			int col = 0;
			for (ValuePixel colPixel : pixels) {
				
				double distance = 
						this.distanceFunction.distance(rowPixel, colPixel);
				
				System.out.println(rowPixel);
				System.out.println(colPixel);
				System.out.println(distance);
				
				double value = 
						this.radialBasicFunction.value(distance, EPSILON);
				
				A.set(row, col, value);
				
				col ++;
			}
			
			row ++;
		}
		
		// solve linear equation system
		
		if( !CommonOps.solve(A,b,x) ) {
			throw new IllegalArgumentException("A was a singular matrix.");
		}
		

		System.out.println(A);
		System.out.println(x);
		System.out.println(b);
		
		// initialize buffered image 
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);
		
		// write image values
		
		ValuePixel tmpValuePixel = new ValuePixel(0, 0, Double.NaN);
		double[] values = new double[width * height];
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				
				double sum = 0;
				int index = 0;
				for (ValuePixel p : pixels) {
					
					tmpValuePixel.getCoordinates().setX(i);
					tmpValuePixel.getCoordinates().setY(j);
					
					double distance = 
							this.distanceFunction.distance(tmpValuePixel, p);
					
					double weight = 
							x.get(index);
					
					double value =
							this.radialBasicFunction.value(distance, EPSILON);
					
					sum += weight * value;
					
					int colorIndex = colorIndex(sum);
					int color = this.colorScheme.getRGB(0, colorIndex);
					image.setRGB(i, j, color);
					
					index ++;
				}
				
				Arrays2d.setDouble(sum, i, j, values, width, height);
			}
		}
		
		System.out.println(Arrays2d.toStringDouble(values, width, height));
		
		// debugging
		this.addDebugInformation(tileSize, coordinates, image);
		
		return image;
	}
	
	public static interface IDistanceFunction<T> {
		double distance(T o1, T o2);
	}
	
	public static interface IRadialBasicFunction {
		double value(double value, double epsilon);
	}
	
	public static void main(String[] args) throws IOException {
		
		WeightedSum[] weightedSums = new WeightedSum[] {
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, new WeightedSum(9), null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				null, new WeightedSum(3), null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
		};
		
		BufferedImage colorScheme = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = SumAlphaVisualizer.ranges(0, 5, colorScheme.getHeight());
		RbfVisualizer visualizer = new RbfVisualizer(colorScheme, ranges);
		BufferedImage result = visualizer.visualize(weightedSums, new TileSize(9, 9), new TileCoordinates(0, 0, 0));
		ImageIO.write(result, "png", new File("out/test.png"));
	}

}

