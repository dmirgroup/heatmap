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


//public class GenericRbfVisualizer<T>
//extends AbstractDebuggingVisualizer<Map<RelativeCoordinates, T>> {
//	
//	public static final double EPSILON = 10;
//	
//	private IToDoubleMapper<T> toValueMapper;
//	
//	private IDistanceFunction<RelativeCoordinates> distanceFunction =
//			new EuclidianDistance();
//
//	private IRadialBasisFunction radialBasisFunction = 
//			new GaussianRbf(EPSILON);
//	
//	private IColorScheme colorScheme;
//	
//	public GenericRbfVisualizer(
//			IToDoubleMapper<T> valueMapper,
//			IToDoubleMapper<T> alphaMapper,
//			IColorScheme colorScheme,
//			IAlphaScheme alphaScheme) {
//		
//		this.toValueMapper = valueMapper;
//		this.colorScheme = colorScheme;
//	}
//	
//	public BufferedImage visualizeWithDebuggingInformation(
//			Map<RelativeCoordinates, T> data,
//			TileSize tileSize,
//			TileCoordinates coordinates) {
//
//		super.logger.debug(
//				"Visualizing tile of size {} x {}.", 
//				tileSize.getWidth(),
//				tileSize.getHeight());
//
//		super.logger.debug(
//				"Relevant points: {}.", 
//				data.size());
//		
//		// create matrices
//		
//		DenseMatrix64F A = new DenseMatrix64F(data.size(), data.size());
//		DenseMatrix64F x = new DenseMatrix64F(data.size(), 1);
//		DenseMatrix64F b = new DenseMatrix64F(data.size(), 1);
//		
//		// setting A and b
//		
//		super.logger.debug("Building matrices for linear equation.");
//		
//		int row = 0;
//		for (T rowPixel : data.values()) {
//			
//			double bValue = this.toValueMapper.map(rowPixel);
//			b.set(row, bValue);
//			
//			int col = 0;
//			for (T colPixel : data.values()) {
//				
//				double distance = this.distanceFunction.distance(
//						rowPixel.getCoordinates(), 
//						colPixel.getCoordinates());
//				
//				double value = this.radialBasisFunction.value(distance);
//				
//				A.set(row, col, value);
//				
//				col ++;
//			}
//			
//			row ++;
//		}
//		
//		// solve linear equation system
//		
//		super.logger.debug("Solving linear equation.");
//		
//		if( !CommonOps.solve(A,b,x) ) {
//			throw new IllegalArgumentException("A was a singular matrix.");
//		}
//		
//		// width and height shortcuts
//		
//		int width = tileSize.getWidth();
//		int height = tileSize.getHeight();
//		
//		// initialize buffered image 
//
//		super.logger.debug("Initializing image.");
//		
//		BufferedImage image = new BufferedImage(
//				width,
//				height,
//				BufferedImage.TYPE_INT_ARGB);
//		
//		// write image values
//
//		super.logger.debug("Writing image values.");
//
//		double max = Double.MIN_VALUE;
//		ValuePixel tmp = new ValuePixel(0, 0, Double.NaN);
//		for (int i = 0; i < width; i ++) {
//			for (int j = 0; j < height; j ++) {
//				
//				double sum = 0;
//				int index = 0;
//				for (T p : data.values()) {
//					
//					tmp.setCoordinateValues(i, j);
//					
//					double distance = this.distanceFunction.distance(
//							tmp.getCoordinates(), 
//							p.getCoordinates());
//					double value = this.radialBasisFunction.value(distance);
//
//					double weight = x.get(index);
//					
//					sum += weight * value;
//					
//					index ++;
//				}
//				
//				max = Math.max(max, sum);
//				int color = this.colorScheme.getColor(sum);
//				image.setRGB(i, j, color);
//			}
//		}
//		
//
//		super.logger.debug("Done writing image values. Max Value: {}", max);
//		
////		System.out.println(Arrays2d.toStringDouble(values, width, height));
//		
//		super.logger.debug("Returning image.");
//		
//		return image;
//	}
//	
//	public static void main(String[] args) throws IOException {
//		
//		Map<RelativeCoordinates, WeightedSumPixel> map = 
//				new HashMap<RelativeCoordinates, WeightedSumPixel>();
//		
//		WeightedSumPixel s1 = new WeightedSumPixel(9);
//		map.put(new RelativeCoordinates(100, 80), s1);
//		
//		WeightedSumPixel s2 = new WeightedSumPixel(5);
//		map.put(new RelativeCoordinates(80, 90), s2);
//
//		WeightedSumPixel s3 = new WeightedSumPixel(1);
//		map.put(new RelativeCoordinates(40, 40), s3);
//		
//		BufferedImage colorImage = ImageIO.read(new File("src/main/resources/color-schemes/classic.png"));
//		double[] ranges = ImageColorScheme.equdistantRanges(0, 9, colorImage.getHeight());
//		ImageColorScheme colorScheme = new ImageColorScheme(colorImage, ranges);
//		
//		GenericRbfVisualizer<WeightedSumPixel> visualizer = new GenericRbfVisualizer<WeightedSumPixel>(
//				new WeightedSumToAverageMapper(),
//				null, 
//				colorScheme, 
//				null);
//		BufferedImage result = visualizer.visualize(map, new TileSize(128, 128), new TileCoordinates(0, 0, 0));
//		ImageIO.write(result, "png", new File("out/test.png"));
//	}
//
//}

