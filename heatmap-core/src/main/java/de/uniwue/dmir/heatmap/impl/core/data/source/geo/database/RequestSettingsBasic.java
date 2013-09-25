package de.uniwue.dmir.heatmap.impl.core.data.source.geo.database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestSettingsBasic {
	
	private String table;
	private String longitudeAttribute;
	private String latitudeAttribute;
	
}