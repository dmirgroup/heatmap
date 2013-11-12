package de.uniwue.dmir.heatmap.core.processors.mappers;

import java.util.Map;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

@AllArgsConstructor
public class MapMapper<T, P> 
implements IMapper<T, P> {

	private Map<T, P> map;
	
	@Override
	public P map(T object) {
		return this.map.get(object);
	}

}
