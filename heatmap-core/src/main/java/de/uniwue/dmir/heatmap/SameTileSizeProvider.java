package de.uniwue.dmir.heatmap;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SameTileSizeProvider implements ITileSizeProvider {

	public static final int DEFAULT_WIDTH = 256;
	public static final int DEFAULT_HEIGHT = 256;
	
	private int width;
	private int height;
	
	public SameTileSizeProvider() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public SameTileSizeProvider(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public TileSize getTileSize(int zoom) {
		return new TileSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
}
