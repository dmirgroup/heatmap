package de.uniwue.dmir.heatmap.core.processing.mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;

@AllArgsConstructor
public class MapperPipeline<T> implements IMapper<T, T> {

	@Getter
	private List<IMapper<T, T>> mappers;

	public MapperPipeline() {
		this(new ArrayList<IMapper<T,T>>());
	}
	
	@Override
	public T map(T object) {

		T result = object;
		for (IMapper<T, T> m : this.mappers) {
			result = m.map(result);
		}
		
		return result;
	}
	
}
