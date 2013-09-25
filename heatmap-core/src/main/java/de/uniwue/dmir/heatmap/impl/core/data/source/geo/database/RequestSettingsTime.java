package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestSettingsTime extends RequestSettingsBasic {

	public RequestSettingsTime(
			String table,
			String longitudeAttribute,
			String latitudeAttribute) {
		super(table, longitudeAttribute, latitudeAttribute);
	}
	
	private String timestampAttribute;
	private Date minimumTimestamp;
	private Date maximumTimestamp;
	
}