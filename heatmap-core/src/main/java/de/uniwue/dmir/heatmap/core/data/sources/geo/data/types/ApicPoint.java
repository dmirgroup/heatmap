package de.uniwue.dmir.heatmap.core.data.sources.geo.data.types;

import java.util.Date;

import de.uniwue.dmir.heatmap.core.data.sources.geo.GeoCoordinates;
import lombok.Data;

@Data
public class ApicPoint implements IGeoPoint {
	
	private GeoCoordinates geoCoordinates;
	private String geoProvider;
	
	private Date timestampReceived;
	private Date timestampRecorded;
	
	private String mac;
	private String deviceId;
	private String userId;
}
