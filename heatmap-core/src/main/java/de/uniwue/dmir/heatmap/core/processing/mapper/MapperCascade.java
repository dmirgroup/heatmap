package de.uniwue.dmir.heatmap.core.processing.mapper;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;

@AllArgsConstructor
public class MapperCascade<TSource, TIntermediate, TResult> 
implements IMapper<TSource, TResult>{

	private IMapper<TSource, TIntermediate> sourceMapper;
	private IMapper<TIntermediate, TResult> intermediateMapper;
	
	@Override
	public TResult map(TSource object) {
		TIntermediate intermediate = this.sourceMapper.map(object);
		return this.intermediateMapper.map(intermediate);
	}
}
