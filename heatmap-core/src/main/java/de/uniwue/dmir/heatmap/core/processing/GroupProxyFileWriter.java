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
import java.security.NoSuchAlgorithmException;

import lombok.Getter;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;
import de.uniwue.dmir.heatmap.core.util.HashUtils;

/**
 * 
 * @author Martin Becker
 *
 * @param <TInner>
 * @param <TOuter>
 */
public class GroupProxyFileWriter<TInner, TOuter> 
implements ITileProcessor<TOuter> {

	@Getter
	@Setter
	private String nullGroup = "NULL";
	
	private IKeyValueIteratorFactory<TOuter, String, TInner> groupIteratorFactory;
	private String parentFolder;
	private AbstractFileWriter<TInner> fileWriter;
	
	@Getter
	@Setter
	private String hashAlgorithm;
	
	public GroupProxyFileWriter(
			IKeyValueIteratorFactory<TOuter, String, TInner> groupIteratorFactory,
			AbstractFileWriter<TInner> fileWriter,
			String parentFolder) {

		this.groupIteratorFactory = groupIteratorFactory;
		this.parentFolder = parentFolder;
		this.fileWriter = fileWriter;
	}

	
	@Override
	public void process(
			TOuter tile, 
			TileSize tileSize,
			TileCoordinates tileCoordinates) {
		
		IKeyValueIterator<String, TInner> iterator = 
				this.groupIteratorFactory.iterator(tile);
		
		while (iterator.hasNext()) {
			
			iterator.next();
			
			String groupId = iterator.getKey();
			if (groupId == null) {
				groupId = this.nullGroup;
			}
			
			if (this.hashAlgorithm != null) {
				try {
					groupId = HashUtils.digest(groupId, this.hashAlgorithm);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
			}
			
			TInner groupData = iterator.getValue();
			
			File parentFolder = new File(this.parentFolder, groupId);
			
			this.fileWriter.setParentFolder(parentFolder.toString());
			this.fileWriter.process(groupData, tileSize, tileCoordinates);
		}
	}

}
