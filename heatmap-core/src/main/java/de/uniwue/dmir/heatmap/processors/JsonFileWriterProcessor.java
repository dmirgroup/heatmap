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

import java.io.IOException;
import java.io.OutputStream;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.TileSize;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public class JsonFileWriterProcessor<TTile> 
extends AbstractFileWriterProcessor<TTile> {

	private ObjectMapper mapper;
	
	public JsonFileWriterProcessor(
			String parentFolder,
			IFileStrategy fileStrategy, 
			boolean gzip) {
		super(parentFolder, fileStrategy, "json", gzip);
		this.mapper = new ObjectMapper();
	}

	@Override
	public void process(TTile tile, TileSize tileSize, TileCoordinates coordinates) {
		
		try {
			
			OutputStream outputStream = super.getOutputStream(coordinates);
			
			this.mapper.writeValue(outputStream, tile);
			
			outputStream.close();
			
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException(e);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static class Factory<TTile>
	implements IFileWriterProcessorFactory<TTile> {

		protected IFileStrategy fileStrategy;

		public Factory(IFileStrategy fileStrategy) {
			this.fileStrategy = fileStrategy;
		}
		
		@Setter
		@Getter
		private boolean gzip;
		
		@Override
		public ITileProcessor<TTile> getInstance(String parentFolder) {
			return new JsonFileWriterProcessor<TTile>(
					parentFolder, 
					this.fileStrategy, 
					this.gzip);
		}
		
	}

}
