package de.uniwue.dmir.heatmap.heatmaps;

import java.io.File;
import java.io.IOException;

public interface IFileReader<T> {
	String getExtension();
	T readFile(File file) throws IOException;
}
