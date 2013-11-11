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
package de.uniwue.dmir.heatmap.core.processors;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.ITileProcessor;
import de.uniwue.dmir.heatmap.core.TileSize;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.processors.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.TileCoordinates;

/**
 * 
 * @author Martin Becker
 *
 * @param <TGroupData>
 * @param <TGroupContainer>
 */
public class ProxyGroupFileWriter<TGroupData, TGroupContainer> 
implements ITileProcessor<TGroupContainer> {

	@Getter
	@Setter
	private String nullGroup = "NULL";
	
	private IKeyValueIteratorFactory<TGroupContainer, String, TGroupData> groupIteratorFactory;
	private String parentFolder;
	private AbstractFileWriterProcessor<TGroupData> fileWriter;
	
	@Getter
	@Setter
	private IMapper<String, String> groupIdMapper;
	
	public ProxyGroupFileWriter(
			IKeyValueIteratorFactory<TGroupContainer, String, TGroupData> groupIteratorFactory,
			AbstractFileWriterProcessor<TGroupData> fileWriter,
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
		
		IKeyValueIterator<String, TGroupData> iterator = 
				this.groupIteratorFactory.iterator(tile);
		
		while (iterator.hasNext()) {
			
			iterator.next();
			
			String groupId = iterator.getKey();
			if (groupId == null) {
				groupId = this.nullGroup;
			}
			
			if (this.groupIdMapper != null) {
				groupId = this.groupIdMapper.map(groupId);
			}
			
			TGroupData groupData = iterator.getValue();
			
			File parentFolder = new File(this.parentFolder, groupId);
			
			this.fileWriter.setParentFolder(parentFolder.toString());
			this.fileWriter.process(groupData, tileSize, tileCoordinates);
		}
	}

}
