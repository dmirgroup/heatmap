package de.uniwue.dmir.heatmap.mybatis;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import de.uniwue.dmir.heatmap.incremental.IProcessLimiter;

public interface MyBatisProcessLimiter extends IProcessLimiter {
	Date getMaxTime(@Param("start") Date start, @Param("limit") int limit);
}
