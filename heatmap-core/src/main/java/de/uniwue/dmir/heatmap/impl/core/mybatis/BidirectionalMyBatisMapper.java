package de.uniwue.dmir.heatmap.impl.core.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis mapper for bidirectional mappings.
 * Corresponding MySQL table:
		<pre>
		CREATE TABLE IF NOT EXISTS bidirectional_mapping (
			`instance_id` CHAR(32) NOT NULL ,
			`query` VARCHAR(256) NOT NULL ,
			`result` VARCHAR(256) NOT NULL,
			PRIMARY KEY (`instance_id`, `query`),
			INDEX `result` (`instance_id`, `result`));
		</pre>
 * 
 * @author Martin Becker
 *
 */
public interface BidirectionalMyBatisMapper {
	
	@Select(
			"SELECT result "
			+ "FROM bidirectional_mappings "
			+ "WHERE instance_id = #{instanceId} "
			+ "AND query = #{query}")
	public String getResult(
			@Param("instanceId") String instanceId, 
			@Param("query") String query);
	
	@Select(
			"SELECT query "
			+ "FROM bidirectional_mappings "
			+ "WHERE instance_id = #{instanceId} "
			+ "AND result = #{result}")
	public String getQuery(
			@Param("instanceId") String instanceId, 
			@Param("result") String result);
}