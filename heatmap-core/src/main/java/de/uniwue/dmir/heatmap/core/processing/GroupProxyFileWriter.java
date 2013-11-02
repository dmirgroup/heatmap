package de.uniwue.dmir.heatmap.core.processing;

import java.io.File;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.processing.IKeyValueIteratorFactory.IKeyValueIterator;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

/**
 * 
 * @author Martin Becker
 *
 * @param <TInner>
 * @param <TOuter>
 */
public class GroupProxyFileWriter<TInner, TOuter> 
implements ITileProcessor<TOuter> {

	private IKeyValueIteratorFactory<TOuter, String, TInner> groupIteratorFactory;
	private String parentFolder;
	private AbstractFileWriter<TInner> fileWriter;
	
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
			TInner groupData = iterator.getValue();
			
			File parentFolder = new File(this.parentFolder, groupId);
			
			this.fileWriter.setParentFolder(parentFolder.toString());
			this.fileWriter.process(groupData, tileSize, tileCoordinates);
		}
	}

}
