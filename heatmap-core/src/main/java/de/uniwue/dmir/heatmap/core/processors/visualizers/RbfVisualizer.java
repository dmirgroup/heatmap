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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.data.types.IDataWithRelativeCoordinates;
import de.uniwue.dmir.heatmap.core.data.types.ValuePixel;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IAlphaScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.IColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.color.ImageColorScheme;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.EuclidianDistance;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.GaussianRbf;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.tiles.pixels.WeightedSum;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

public class RbfVisualizer
extends AbstractDebuggingVisualizer<WeightedSum[]> {
	
	public static final double EPSILON = 0.1;
	
	private IDistanceFunction<IDataWithRelativeCoordinates> distanceFunction =
			new EuclidianDistance();

	private IRadialBasisFunction radialBasisFunction = 
			new GaussianRbf(EPSILON);
	
	private IColorScheme colorScheme;
	private IAlphaScheme alphaScheme;
	
	public RbfVisualizer(
			IColorScheme colorScheme,
			IAlphaScheme alphaScheme) {
		
		this.colorScheme = colorScheme;
		this.alphaScheme = alphaScheme;
	}
	
	public BufferedImage visualize(
			WeightedSum[] data,
			TileSize tileSize,
			TileCoordinates coordinates) {

		int width = tileSize.getWidth();
		int height = tileSize.getHeight();
		
		super.logger.debug("Visualizing a {} x {} tile.", width, height);
		
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

		super.logger.debug("Building matrices for linear equation.");
		
		int row = 0;
		for (ValuePixel rowPixel : pixels) {
			
			b.set(row, rowPixel.getValue());
			
			int col = 0;
			for (ValuePixel colPixel : pixels) {
				
				double distance = 
						this.distanceFunction.distance(rowPixel, colPixel);
				
				double value = 
						this.radialBasisFunction.value(distance);
				
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
		
		// initialize buffered image 
		
		super.logger.debug("Initializing image.");
		
		BufferedImage image = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_ARGB);
		
		// write image values
		
		super.logger.debug("Writing image values.");
		
		ValuePixel tmpValuePixel = new ValuePixel(0, 0, Double.NaN);
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
							this.radialBasisFunction.value(distance);
					
					sum += weight * value;
					
					index ++;
				}
				
				int color = this.colorScheme.getColor(sum);
				image.setRGB(i, j, color);
				
			}
		}
		
//		System.out.println(Arrays2d.toStringDouble(values, width, height));
		
		// debugging
		this.addDebugInformation(tileSize, coordinates, image);

		super.logger.debug("Returning image.");
		
		return image;
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
		
		BufferedImage colorImage = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
		double[] ranges = ImageColorScheme.ranges(0, 9, colorImage.getHeight());
		ImageColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
		
		RbfVisualizer visualizer = new RbfVisualizer(colorScheme, null);
		BufferedImage result = visualizer.visualize(weightedSums, new TileSize(9, 9), new TileCoordinates(0, 0, 0));
		ImageIO.write(result, "png", new File("out/test.png"));
	}

}

