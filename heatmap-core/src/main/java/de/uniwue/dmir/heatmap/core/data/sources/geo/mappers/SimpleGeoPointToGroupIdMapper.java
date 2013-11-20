package de.uniwue.dmir.heatmap.core.data.sources.geo.mappers;

import java.util.List;

import de.uniwue.dmir.heatmap.core.data.sources.geo.data.types.SimpleGeoPoint;
import de.uniwue.dmir.heatmap.core.filters.access.mappers.AbstractGroupIdMapper;

public class SimpleGeoPointToGroupIdMapper
extends AbstractGroupIdMapper<SimpleGeoPoint<String>> {

	@Override
	protected void add(SimpleGeoPoint<String> object, List<String> ids) {
		ids.add(object.getGroupDescription());
	}

}
