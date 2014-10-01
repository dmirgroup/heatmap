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
package de.uniwue.dmir.heatmap.processors;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniwue.dmir.heatmap.IFilter;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.IVisualizer;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.NoFilter;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoBoundingBox;
import de.uniwue.dmir.heatmap.point.sources.geo.GeoCoordinates;
import de.uniwue.dmir.heatmap.point.sources.geo.IMapProjection;
import de.uniwue.dmir.heatmap.processors.visualizers.IBackgroundVisualizer;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.ImageUtil;

public class SingleImageProcessor<TTile> implements ITileProcessor<TTile> {

	private GeoBoundingBox boundingBox;
	private IMapProjection mapProjection;
	
	private IBackgroundVisualizer<TTile> backgroundVisualizer;
	private IVisualizer<TTile> visualizer;
	
	private Map<Integer, Map<TileCoordinates, BufferedImage>> tiles;
	
	public SingleImageProcessor(
			GeoBoundingBox geoBoundingBox,
			IMapProjection mapProjection,
			IBackgroundVisualizer<TTile> backgroundVisualizer,
			IVisualizer<TTile> visualizer) {
		
		this.boundingBox = geoBoundingBox;
		this.mapProjection = mapProjection;
		this.backgroundVisualizer = backgroundVisualizer;
		this.visualizer = visualizer;
		
		this.tiles = new HashMap<Integer, Map<TileCoordinates,BufferedImage>>();
	}
	
	@Override
	public void process(
			TTile tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		Map<TileCoordinates, BufferedImage> tiles = this.tiles.get(tileCoordinates.getZoom());
		if (tiles == null) {
			tiles = new HashMap<TileCoordinates, BufferedImage>();
			this.tiles.put(tileCoordinates.getZoom(), tiles);
		}
		
		BufferedImage image = this.visualizer.visualize(tile, tileSize, tileCoordinates);
		
		tiles.put(tileCoordinates, image);
		
	}

	@Override
	public void close() {
		
		IFilter<?, ?> filter = new NoFilter<Object, Object>();
		
		for (int zoom : this.tiles.keySet()) {
			
			Map<TileCoordinates, BufferedImage> tiles = this.tiles.get(zoom);
			
			List<TileCoordinates> bottomLeftList = 
					this.mapProjection.overlappingTiles(this.boundingBox.getMin(), zoom, filter);

			List<TileCoordinates> topRightList = 
					this.mapProjection.overlappingTiles(this.boundingBox.getMax(), zoom, filter);

			TileCoordinates bottomLeft = bottomLeftList.get(0);
			TileCoordinates topRight = topRightList.get(0);
			
			// tile coordinates start from the top left TODO: is this always the case? see ITileCoordinatesProjection
			long diffX = topRight.getX() - bottomLeft.getX();
			long diffY = bottomLeft.getY() - topRight.getY();
			
			BufferedImage image = new BufferedImage(
						(int) (diffX + 1) * 256, 
						(int) (diffY + 1) * 256, 
						BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			
			
			for (long x = bottomLeft.getX(); x <= topRight.getX(); x ++) {
				for (long y = topRight.getY(); y <= bottomLeft.getY(); y ++) {
					
					TileCoordinates tileCoordinates = new TileCoordinates(x, y, zoom);
					
					BufferedImage tileBackground = 
							this.backgroundVisualizer.visualize(
									null, 
									null, 
									tileCoordinates);
					
					g.drawImage(
							tileBackground, 
							(int) (x - bottomLeft.getX()) * 256, 
							(int) (y - topRight.getY()) * 256, 
							null);
					
					BufferedImage overlay = null;
					if (tiles != null) {
						overlay = tiles.get(tileCoordinates);
						if (overlay != null) {
							g.drawImage(
									overlay, 
									(int) (x - bottomLeft.getX()) * 256, 
									(int) (y - topRight.getY()) * 256, 
									null);
						}
					}
				}
			}
			
			// crop image
			
			RelativeCoordinates topLeftRelative = 
					this.mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(this.boundingBox.getMin().getLongitude(), this.boundingBox.getMax().getLatitude()), 
							new TileCoordinates(bottomLeft.getX(), topRight.getY(), zoom));
//							bottomLeft);
			
			RelativeCoordinates bottomRightRelative = 
					this.mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(this.boundingBox.getMax().getLongitude(), this.boundingBox.getMin().getLatitude()),
							new TileCoordinates(topRight.getX(), bottomLeft.getY(), zoom));
//							topRight);
			
			System.out.println(topLeftRelative);
			System.out.println(bottomRightRelative);
			
			int topLeftX = topLeftRelative.getX();
			int topLeftY = topLeftRelative.getY();
			int width = image.getWidth() - topLeftRelative.getX() - (256 - bottomRightRelative.getX());
			int height = image.getHeight() - topLeftRelative.getY() - (256 - bottomRightRelative.getY());
			
			System.out.println(topLeftX);
			System.out.println(topLeftY);
			System.out.println(width);
			System.out.println(height);
			System.out.println(image.getWidth());
			System.out.println(image.getHeight());
			
			BufferedImage subImage = image.getSubimage(
					topLeftX, 
					topLeftY,
					width,
					height);
			
			try {
				ImageUtil.displayImage(subImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
