package de.uniwue.dmir.heatmap.core.data.sources.geo.data.types;

import java.util.Date;

import lombok.Data;

@Data
public class ApicPoint {
	
	private double longtiude;
	private double latitude;
	private String geoProvider;
	
	private Date timestampReceived;
	private Date timestampRecorded;
	
	private String mac;
	private String deviceId;
	private String userId;
}
