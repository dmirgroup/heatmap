package de.uniwue.dmir.heatmap.core.processors.mappers;

import java.util.List;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;

@AllArgsConstructor
public class MapperPipe<TKey, TValue> 
implements IMapper<TKey, TValue> {

	private List<IMapper<TKey, TValue>> mappers;
	private boolean first;
	
	public MapperPipe(List<IMapper<TKey, TValue>> mappers) {
		this(mappers, true);
	}
	
	@Override
	public TValue map(TKey object) {
		
		TValue result = null;
		
		for (IMapper<TKey, TValue> mapper : this.mappers) {
			
			if (result == null) {
				result = mapper.map(object);
			} else {
				if (!this.first) {
					TValue value = mapper.map(object);
					if (value != null) {
						result = value;
					}
				}
			}
		}
		
		return result;
	}

}
