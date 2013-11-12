package de.uniwue.dmir.heatmap.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.uniwue.dmir.heatmap.impl.core.mybatis.BidirectionalMyBatisMapper2;
import de.uniwue.dmir.heatmap.impl.core.mybatis.BidirectionalMyBatisMapper2.Element;

public class MapFactoryBean implements FactoryBean<Map<String, String>> {

	@Autowired
	private BidirectionalMyBatisMapper2 bidirectionalMyBatisMapper;
	
	private String mapIdentifier;
	
	public MapFactoryBean(String mapIdentifier) {
		this.mapIdentifier = mapIdentifier;
	}
	
	@Override
	public Map<String, String> getObject() throws Exception {
		
		List<Element> elements = 
				this.bidirectionalMyBatisMapper.getElements(
						this.mapIdentifier);
		
		Map<String, String> map = new HashMap<String, String>();
		for (Element e : elements) {
			map.put(e.getKey(), e.getValue());
		}
		
		return map;
	}

	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
