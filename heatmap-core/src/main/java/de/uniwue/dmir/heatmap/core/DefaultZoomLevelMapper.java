package de.uniwue.dmir.heatmap.core;

public class DefaultZoomLevelMapper 
implements IZoomLevelMapper {

	@Override
	public ZoomLevelSize getSize(int zoom) {
		int xy = (1 << zoom); // log scale using base 2
		return new ZoomLevelSize(xy, xy);
	}
	
}