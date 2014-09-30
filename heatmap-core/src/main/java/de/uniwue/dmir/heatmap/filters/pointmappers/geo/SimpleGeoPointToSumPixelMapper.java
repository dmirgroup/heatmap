package de.uniwue.dmir.heatmap.filters.pointmappers.geo;

import de.uniwue.dmir.heatmap.point.types.geo.SimpleGeoPoint;
import de.uniwue.dmir.heatmap.tiles.pixels.SumPixel;
import de.uniwue.dmir.heatmap.util.mapper.IMapper;

public class SimpleGeoPointToSumPixelMapper<TGroupDescription> 
implements IMapper<SimpleGeoPoint<TGroupDescription>, SumPixel> {

	@Override
	public <TDerived extends SimpleGeoPoint<TGroupDescription>> SumPixel map(
			TDerived object) {
		return new SumPixel(1, object.getValue());
	}
	
}
