package de.uniwue.dmir.heatmap.core.processing.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.impl.core.mybatis.BidirectionalMyBatisMapper;


public class StringLookupMapper 
implements IMapper<String, String>{

	private String instanceId;
	private boolean returnNull;
	
	@Autowired
	private BidirectionalMyBatisMapper mapper;
	
	public StringLookupMapper(String instanceId) {
		this.instanceId = instanceId;
		this.returnNull = false;
	}
	
	@Override
	public String map(String object) {
		
		String mapping = this.mapper.getResult(this.instanceId, object);
		
		if (mapping == null && !this.returnNull) {
			return object;
		}
		
		return mapping;
	}
}
