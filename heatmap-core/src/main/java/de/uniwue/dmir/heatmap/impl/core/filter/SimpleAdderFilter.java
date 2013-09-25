package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

@AllArgsConstructor
public class SimpleAdderFilter<T extends IExternalData, P> 
implements IFilter<T, P> {

	private IMapper<T, P> mapper;
	private IAdder<P> adder;
	
	public void filter(T dataPoint, ITile<T, P> tile) {
		
		P[] tileData = tile.getData();
		
		int tileWidth = tile.getDimensions().getWidth();
		int tileHeight = tile.getDimensions().getHeight();
		
		P addable = this.mapper.map(dataPoint);
		
		P currentValue = Arrays2d.get(
				dataPoint.getCoordinates().getX(), 
				dataPoint.getCoordinates().getY(), 
				tileData,
				tileWidth, 
				tileHeight);
		
		P sum;
		if (currentValue == null) {
			sum = addable;
		} else {
			sum = this.adder.add(addable, currentValue);
		}
		
		Arrays2d.set(
				sum, 
				dataPoint.getCoordinates().getX(), 
				dataPoint.getCoordinates().getY(), 
				tileData, 
				tileWidth,
				tileHeight);
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	public int getCenterX() {
		return 0;
	}

	public int getCenterY() {
		return 0;
	}
	
}