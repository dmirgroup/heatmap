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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniwue.dmir.heatmap.ITileProcessor;
import de.uniwue.dmir.heatmap.processors.filestrategies.IFileStrategy;
import de.uniwue.dmir.heatmap.tiles.coordinates.TileCoordinates;

public abstract class AbstractFileWriterProcessor<TTile> 
extends AbstractProcessor<TTile> {

	public static final String GZIP_EXTENSION = ".gz";
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Setter
	private String parentFolder;
	protected IFileStrategy fileStrategy;
	protected String fileFormat;
	
	@Setter
	@Getter
	private boolean gzip;
	
	
	
	public AbstractFileWriterProcessor(
			String parentFolder, 
			IFileStrategy fileStrategy, 
			String fileFormat,
			boolean gzip) {
		
		this.parentFolder = parentFolder;
		this.fileStrategy = fileStrategy;
		this.fileFormat = fileFormat;
		this.gzip = gzip;
	}

	/**
	 * Gets the file and creates the necessary folders if need be.
	 * 
	 * <strong>Note:</strong> Don't forget to close the stream which you receive.
	 * 
	 * @param tileCoordinates coordinates of the tile to create a file for
	 * @return 
	 * 		the {@link OutputStream} associated with the file for the given tile coordinates; 
	 * 		<strong>Note:</strong> Don't forget to close the stream which you receive.
	 * @throws IOException 
	 */
	protected OutputStream getOutputStream(TileCoordinates tileCoordinates) 
	throws IOException {
		
		String extension = this.fileFormat + (this.gzip ? GZIP_EXTENSION : "");
		
		File parentFolder = this.getParentFolder();
		String fileName = this.fileStrategy.getFileName(tileCoordinates, extension);
		
		File file;
		if (parentFolder == null) {
			file = new File(fileName);
		} else {
			file = new File(parentFolder, fileName);
		}
		
		file.getParentFile().mkdirs();
		
		OutputStream outputStream = new FileOutputStream(file);
		if (this.gzip) {
			outputStream = new GZIPOutputStream(outputStream);
		}
		return outputStream;
		
	}
	
	protected File getParentFolder() {
		return this.parentFolder == null ? null : new File(this.parentFolder);
	}
	
	public void close() {
	}
	
	public interface IFileWriterProcessorFactory<TTile> {
		ITileProcessor<TTile> getInstance(String parentFolder);
	}
	
}
