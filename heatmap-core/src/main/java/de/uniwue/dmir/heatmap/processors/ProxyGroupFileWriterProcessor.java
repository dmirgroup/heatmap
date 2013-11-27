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

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.processors.AbstractFileWriterProcessor.IFileWriterProcessorFactory;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory;
import de.uniwue.dmir.heatmap.util.iterator.IKeyValueIteratorFactory.IKeyValueIterator;

/**
 * 
 * @author Martin Becker
 *
 * @param <TGroupData>
 * @param <TGroupContainer>
 */
public class ProxyGroupFileWriterProcessor<TGroupData, TGroupContainer> 
implements ITileProcessor<TGroupContainer> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Getter
	@Setter
	private String nullGroup = "NULL";
	
	private IKeyValueIteratorFactory<TGroupContainer, String, TGroupData> groupIteratorFactory;
	private String parentFolder;
	private IFileWriterProcessorFactory<TGroupData> fileWriter;
	
	@Getter
	@Setter
	private IMapper<String, String> groupIdMapper;
	
	public ProxyGroupFileWriterProcessor(
			IKeyValueIteratorFactory<TGroupContainer, String, TGroupData> groupIteratorFactory,
			IFileWriterProcessorFactory<TGroupData> fileWriter,
			String parentFolder) {

		this.groupIteratorFactory = groupIteratorFactory;
		this.parentFolder = parentFolder;
		this.fileWriter = fileWriter;
	}

	
	@Override
	public void process(
			TGroupContainer tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		if (tile == null) {
			this.logger.warn("Tile data was null: {}", tileCoordinates);
			return;
		}
		
		IKeyValueIterator<String, TGroupData> iterator = 
				this.groupIteratorFactory.instance(tile);
		
		while (iterator.hasNext()) {
			
			iterator.next();
			
			String groupId = iterator.getKey();
			if (groupId == null) {
				groupId = this.nullGroup;
			}
			
			if (this.groupIdMapper != null) {
				groupId = this.groupIdMapper.map(groupId);
			}
			
			this.logger.debug("Writing file for group: {}", groupId);

			TGroupData groupData = iterator.getValue();
			
			File parentFolder = 
					new File(this.parentFolder, groupId);
			
			ITileProcessor<TGroupData> fileWriter = 
					this.fileWriter.getInstance(parentFolder.toString());
			
			fileWriter.process(groupData, tileSize, tileCoordinates);
			fileWriter.close();
		}
	}

	@Override
	public void close() {
	}

}
