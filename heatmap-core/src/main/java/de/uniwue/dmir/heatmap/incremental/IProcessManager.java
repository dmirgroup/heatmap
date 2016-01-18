package de.uniwue.dmir.heatmap.incremental;

import java.util.Date;

import lombok.Data;

public interface IProcessManager {
	
	ProcessManagerEntry getEntry();
	
	void start(Date minTime);
	void finish(Date maxTime);
	
	@Data
	public static class ProcessManagerEntry {
		
		private Date startTime;
		private Date minTime;
		
		private Date endTime;
		private Date maxTime;
	}
}