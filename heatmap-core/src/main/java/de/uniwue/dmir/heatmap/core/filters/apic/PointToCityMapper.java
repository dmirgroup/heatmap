package de.uniwue.dmir.heatmap.core.filters.apic;

import java.awt.geom.Path2D;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.ApicPoint;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

@AllArgsConstructor
public class PointToCityMapper 
implements IMapper<ApicPoint, String> {

	private Map<String, Path2D> areas;

	@Override
	public String map(ApicPoint object) {
		
		for (Entry<String, Path2D> e : this.areas.entrySet()) {
			double x = object.getGeoCoordinates().getLongitude();
			double y = object.getGeoCoordinates().getLatitude();
			
			if (e.getValue().contains(x, y)) {
				return e.getKey();
			}
		}
		
		return null;
	}
}
