package de.uniwue.dmir.heatmap.core.data.sources.geo.data.sources.database;

import lombok.Data;
import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoBoundingBox;

@Data
public class GeoRequest<TSettings> {

	private GeoBoundingBox geoBoundingBox;
	private TSettings settings;
	
}
