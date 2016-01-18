package de.uniwue.dmir.heatmap.incremental;

import java.util.Date;

public interface IProcessLimiter {
	Date getMaxTime(Date start, int limit);
}
