package de.uniwue.dmir.heatmap.impl.core.data.source.geo;

import de.uniwue.dmir.heatmap.core.data.type.IToInternalDataMapper;
import de.uniwue.dmir.heatmap.core.tile.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.impl.core.data.type.external.ValuePixel;

public class GeoPointToValuePixelMapper
implements IToInternalDataMapper<GeoPoint, ValuePixel> {

	public ValuePixel map(
			GeoPoint sourceObject,
			RelativeCoordinates relativeCoordinates) {
		
		return new ValuePixel(
				relativeCoordinates.getX(), 
				relativeCoordinates.getY(), 
				1);
	}
	
}