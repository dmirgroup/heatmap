package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestGeo extends RequestSettingsTime {

	private double lonWest;
	private double lonEast;
	private double latNorth;
	private double latSouth;
	
	public RequestGeo(
			String table, 
			String longitudeAttribute, 
			String latitudeAttribute) {
		super(table, longitudeAttribute, latitudeAttribute);
	}

}
