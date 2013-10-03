package de.uniwue.dmir.heatmap.core.processing;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.core.IHeatmap.TileSize;
import de.uniwue.dmir.heatmap.core.tile.coordinates.TileCoordinates;

public class DataFileWriter<I> 
extends AbstractFileWriter<I> {

	private ObjectMapper mapper;
	
	public DataFileWriter(IFileStrategy fileStrategy) {
		super(fileStrategy, "json");
		this.mapper = new ObjectMapper();
	}

	@Override
	public void process(I tile, TileSize tileSize, TileCoordinates coordinates) {
		
		File file = super.getFile(coordinates);
		
		try {
			this.mapper.writeValue(file, tile);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException(e);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
