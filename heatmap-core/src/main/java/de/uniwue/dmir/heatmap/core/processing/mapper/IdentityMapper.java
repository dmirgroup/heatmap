package de.uniwue.dmir.heatmap.core.processing.mapper;

import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;

public class IdentityMapper<T> implements IMapper<T, T>{

	@Override
	public T map(T object) {
		return object;
	}

}
