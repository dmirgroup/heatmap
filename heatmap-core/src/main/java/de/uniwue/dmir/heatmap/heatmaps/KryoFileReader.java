package de.uniwue.dmir.heatmap.heatmaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class KryoFileReader<I> implements IFileReader<I> {

	private Kryo kryo;
	
	public KryoFileReader() {
		this.kryo = new Kryo();
	}
	
	@Override
	public String getExtension() {
		return "bin";
	}

	@Override
	public I readFile(File file) throws IOException {
		Input input = new Input(new FileInputStream(file));
		@SuppressWarnings("unchecked")
		I object = (I) this.kryo.readClassAndObject(input);
		return object;
	}
	
}
