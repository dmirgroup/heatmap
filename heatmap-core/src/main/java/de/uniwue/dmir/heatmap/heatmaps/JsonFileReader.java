package de.uniwue.dmir.heatmap.heatmaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonFileReader<I> implements IFileReader<I> {

	private ObjectMapper objectMapper;
	
	private Class<I> clazz;
	private boolean gzip;
	private String extension;
	
	public JsonFileReader(Class<I> clazz, boolean gzip) {
		this.clazz = clazz;
		this.gzip = gzip;
		if (gzip) {
			this.extension = "json.gz";
		} else {
			this.extension = "json";
		}
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public String getExtension() {
		return this.extension;
	}

	@Override
	public I readFile(File file) throws IOException {
		InputStream in = null;
		if (this.gzip) {
			in = new GZIPInputStream(new FileInputStream(file));
		} else {
			in = new FileInputStream(file);
		}
		return this.objectMapper.readValue(in, this.clazz);
	}

}
