/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.impl.core.mybatis;

import java.util.List;

import lombok.Data;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
public interface BidirectionalMyBatisMapper2 {

	@Select("SELECT element_value "
			+ "FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_key = #{elementKey}")
	String getValue(
			@Param("mappingId") String mappingId, 
			@Param("elementKey") String key);

	@Select("SELECT element_value "
			+ "FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_value = #{elementValue}")
	List<String> getKey(
			@Param("mappingId") String mappingId, 
			@Param("elementValue") String value);

	@Select("SELECT * "
			+ "FROM mappings "
			+ "WHERE mapping_id = #{mappingId}")
	@Results({
		@Result(column = "mapping_id", property = "mappingId"),
		@Result(column = "element_key", property = "key"),
		@Result(column = "element_value", property = "value"),
		@Result(column = "element_data", property = "data"),
	})
	List<Element> getElements(@Param("mappingId") String mappingId);

	@Select("SELECT * "
			+ "FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_key = #{elementKey}")
	@Results({
		@Result(column = "mapping_id", property = "mappingId"),
		@Result(column = "element_key", property = "key"),
		@Result(column = "element_value", property = "value"),
		@Result(column = "element_data", property = "data"),
	})
	Element getElementsByKey(
			@Param("mappingId") String mappingId, 
			@Param("elementKey") String key);

	@Select("SELECT * "
			+ "FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_value = #{elementValue}")
	@Results({
		@Result(column = "mapping_id", property = "mappingId"),
		@Result(column = "element_key", property = "key"),
		@Result(column = "element_value", property = "value"),
		@Result(column = "element_data", property = "data"),
	})
	List<Element> getElementsByValue(
			@Param("mappingId") String mappingId, 
			@Param("elementValue") String value);

	@Select("SELECT EXISTS("
			+ "SELECT 1 FROM mappings "
			+ "WHERE mapping_id = #{mappingId})")
	boolean containsMapping(@Param("mappingId") String mappingId);

	@Select("SELECT EXISTS("
			+ "SELECT 1 FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_key = #{elementKey})")
	boolean containsKey(
			@Param("mappingId") String mappingId, 
			@Param("elementKey") String key);

	@Select("SELECT EXISTS("
			+ "SELECT 1 FROM mappings "
			+ "WHERE mapping_id = #{mappingId}"
			+ "AND element_value = #{elementValue})")
	boolean containsValue(
			@Param("mappingId") String mappingId, 
			@Param("elementValue") String value);
	
	@Data
	public static class Element {
		private String mappingId;
		private String key;
		private String value;
		private String data;
	}
}