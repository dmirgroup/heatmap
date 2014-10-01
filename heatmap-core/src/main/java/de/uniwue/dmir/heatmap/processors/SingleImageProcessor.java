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
import de.uniwue.dmir.heatmap.ITileSizeProvider;
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

public class SingleImageProcessor<TTile> 
extends AbstractProcessor<TTile> {

	private GeoBoundingBox boundingBox;
	private IMapProjection mapProjection;
	
	private IBackgroundVisualizer<TTile> backgroundVisualizer;
	private IVisualizer<TTile> visualizer;
	
	private Map<Integer, Map<TileCoordinates, BufferedImage>> tiles;
	
	public SingleImageProcessor(
			ITileSizeProvider tileSizeProvider,
			GeoBoundingBox geoBoundingBox,
			IMapProjection mapProjection,
			IBackgroundVisualizer<TTile> backgroundVisualizer,
			IVisualizer<TTile> visualizer) {
		
		super(tileSizeProvider);
		
		this.boundingBox = geoBoundingBox;
		this.mapProjection = mapProjection;
		this.backgroundVisualizer = backgroundVisualizer;
		this.visualizer = visualizer;
		
		this.tiles = new HashMap<Integer, Map<TileCoordinates,BufferedImage>>();
	}
	
	@Override
	public void process(
			TTile tile, 
			TileCoordinates tileCoordinates) {
		
		Map<TileCoordinates, BufferedImage> tiles = this.tiles.get(tileCoordinates.getZoom());
		if (tiles == null) {
			tiles = new HashMap<TileCoordinates, BufferedImage>();
			this.tiles.put(tileCoordinates.getZoom(), tiles);
		}
		
		BufferedImage image = this.visualizer.visualize(tile, tileCoordinates);
		
		tiles.put(tileCoordinates, image);
		
	}

	@Override
	public void close() {
		
		IFilter<?, ?> filter = new NoFilter<Object, Object>(this.tileSizeProvider);
		
		for (int zoom : this.tiles.keySet()) {
			
			TileSize tileSize =
					super.tileSizeProvider.getTileSize(zoom);
			
			Map<TileCoordinates, BufferedImage> tiles = this.tiles.get(zoom);
			
			List<TileCoordinates> bottomLeftList = 
					this.mapProjection.overlappingTiles(this.boundingBox.getMin(), zoom, filter);

			List<TileCoordinates> topRightList = 
					this.mapProjection.overlappingTiles(this.boundingBox.getMax(), zoom, filter);

			TileCoordinates bottomLeft = bottomLeftList.get(0);
			TileCoordinates topRight = topRightList.get(0);
			
			// tile coordinates start from the top left 
			long diffX = topRight.getX() - bottomLeft.getX();
			long diffY = bottomLeft.getY() - topRight.getY();
			
			BufferedImage image = new BufferedImage(
						(int) (diffX + 1) * tileSize.getWidth(), 
						(int) (diffY + 1) * tileSize.getHeight(), 
						BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			
			
			for (long x = bottomLeft.getX(); x <= topRight.getX(); x ++) {
				for (long y = topRight.getY(); y <= bottomLeft.getY(); y ++) {
					
					TileCoordinates tileCoordinates = new TileCoordinates(x, y, zoom);
					
					BufferedImage tileBackground = 
							this.backgroundVisualizer.visualize(
									null, 
									tileCoordinates);
					
					g.drawImage(
							tileBackground, 
							(int) (x - bottomLeft.getX()) * tileSize.getWidth(), 
							(int) (y - topRight.getY()) * tileSize.getHeight(), 
							null);
					
					BufferedImage overlay = null;
					if (tiles != null) {
						
						overlay = tiles.get(tileCoordinates);
						
						int pixelX = (int) (x - bottomLeft.getX()) * tileSize.getWidth();
						int pixelY = (int) (y - topRight.getY()) * tileSize.getHeight();
						
						if (overlay != null) {
							g.drawImage(
									overlay, 
									pixelX, 
									pixelY, 
									null);
						}
					}
				}
			}
			
			// crop image
			
			RelativeCoordinates topLeftRelative = 
					this.mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(
									this.boundingBox.getMin().getLongitude(), 
									this.boundingBox.getMax().getLatitude()), 
							new TileCoordinates(bottomLeft.getX(), topRight.getY(), zoom));
//							bottomLeft);
			
			RelativeCoordinates bottomRightRelative = 
					this.mapProjection.fromGeoToRelativeCoordinates(
							new GeoCoordinates(
									this.boundingBox.getMax().getLongitude(), 
									this.boundingBox.getMin().getLatitude()),
							new TileCoordinates(topRight.getX(), bottomLeft.getY(), zoom));
//							topRight);
			
//			System.out.println(topLeftRelative);
//			System.out.println(bottomRightRelative);
			
			int topLeftX = topLeftRelative.getX();
			int topLeftY = topLeftRelative.getY();
			int width = 
					image.getWidth() 
					- topLeftRelative.getX() 
					- (tileSize.getWidth() - bottomRightRelative.getX());
			int height = 
					image.getHeight() 
					- topLeftRelative.getY() 
					- (tileSize.getHeight() - bottomRightRelative.getY());
			
//			System.out.println(topLeftX);
//			System.out.println(topLeftY);
//			System.out.println(width);
//			System.out.println(height);
//			System.out.println(image.getWidth());
//			System.out.println(image.getHeight());
			
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
