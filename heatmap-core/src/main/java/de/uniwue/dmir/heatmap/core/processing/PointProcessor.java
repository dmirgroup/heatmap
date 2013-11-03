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
package de.uniwue.dmir.heatmap.core.processing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.HashUtils;
import de.uniwue.dmir.heatmap.impl.core.data.type.internal.PointSize;

public class PointProcessor<TTile, TGroupTile>
implements ITileProcessor<TTile> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private IKeyValueIteratorFactory<TTile, String, TGroupTile> groupIteratorFactory;
	private IKeyValueIteratorFactory<TGroupTile, RelativeCoordinates, PointSize> pixelIteratorFactory;
	
	private String file;
	private String hashAlgorithm;

	@Getter
	@Setter
	private String nullGroup = "NULL";
	
	public PointProcessor(
			String file, 
			String hashAlgorithm,
			IKeyValueIteratorFactory<TTile, String, TGroupTile> groupIteratorFactory,
			IKeyValueIteratorFactory<TGroupTile, RelativeCoordinates, PointSize> pixelIteratorFactory) {
		
		this.file = file;
		this.hashAlgorithm = hashAlgorithm;
		
		this.groupIteratorFactory = groupIteratorFactory;
		this.pixelIteratorFactory = pixelIteratorFactory;
	}
	
	@Override
	public void process(
			TTile tile,
			TileSize tileSize, 
			TileCoordinates coordinates) {
		
		Map<String, PointContainer> points = new HashMap<String, PointContainer>();
		
		IKeyValueIterator<String, TGroupTile> groupIterator = 
				this.groupIteratorFactory.iterator(tile);
		
		while(groupIterator.hasNext()) {
		
			groupIterator.next();
			
			String groupId = groupIterator.getKey();
			if (this.hashAlgorithm != null && groupId != null) {
				try {
					groupId = HashUtils.digest(groupId, this.hashAlgorithm);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
			} else if (groupId == null) {
				groupId = this.nullGroup;
			}
			
			PointContainer pointContainer = points.get(groupId);
			if (pointContainer == null) {
				pointContainer = new PointContainer();
				points.put(groupId, pointContainer);
			}
			
			TGroupTile groupTile = groupIterator.getValue();
			IKeyValueIterator<RelativeCoordinates, PointSize> pixelIterator = 
					this.pixelIteratorFactory.iterator(groupTile);
			
			while(pixelIterator.hasNext()) {
				
				pixelIterator.next();
				
				PointSize pointSize = pixelIterator.getValue();
			
				pointContainer.setPoints(pointContainer.getPoints() + pointSize.getPoints());
				pointContainer.setSize(pointContainer.getSize() + pointSize.getSize());
				pointContainer.setPixels(pointContainer.getPixels() + 1);
				
			}
		}
		
		
		File file = new File(this.file);
		file.getParentFile().mkdirs();

		try {
			FileWriter fileWriter = new FileWriter(file);
			this.objectMapper.writeValue(fileWriter, points);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Data
	private static class PointContainer {
		private double points;
		private double size;
		private int pixels;
	}

}
