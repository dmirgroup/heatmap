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
