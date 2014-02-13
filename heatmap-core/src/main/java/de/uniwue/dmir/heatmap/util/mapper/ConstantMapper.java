package de.uniwue.dmir.heatmap.util.mapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConstantMapper<TSource, TDestination> 
implements IMapper<TSource, TDestination> {

	private TDestination destination;
	
	@Override
	public TDestination map(TSource object) {
		return this.destination;
	}

}
