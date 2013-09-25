package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;
import de.uniwue.dmir.heatmap.core.filter.operators.IAdder;
import de.uniwue.dmir.heatmap.core.filter.operators.IMapper;
import de.uniwue.dmir.heatmap.core.tile.ITile;
import de.uniwue.dmir.heatmap.core.util.Arrays2d;

@AllArgsConstructor
@Getter
public class ErodeFilter<T extends IExternalData, P>
implements IFilter<T, P>{

	private IMapper<T, P> mapper;
	private IAdder<P> adder;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;

	public void filter(T dataPoint, ITile<T, P> tile) {
		
		P[] tileData = tile.getData();
		
		int startX = dataPoint.getCoordinates().getX();
		int startY = dataPoint.getCoordinates().getY();
		startX -= this.centerX;
		startY -= this.centerY;
		
		int stopX = startX + this.width;
		int stopY = startY + this.height;
		
		for (int x = startX; x < stopX; x++) {
			for (int y = startY; y < stopY; y ++) {
				
				if (!Arrays2d.checkIndex(
						x, 
						y, 
						tile.getDimensions().getWidth(), 
						tile.getDimensions().getHeight())) {
					continue;
				}
				
				
				P addable = this.mapper.map(dataPoint);
				P currentValue = Arrays2d.get(
						x, y, 
						tileData, 
						tile.getDimensions().getWidth(), 
						tile.getDimensions().getHeight());
				
				P sum;
				if (currentValue == null) {
					sum = addable;
				} else {
					sum = this.adder.add(currentValue, addable);
				}
				
				Arrays2d.set(
						sum, x, y, 
						tileData, 
						tile.getDimensions().getWidth(), 
						tile.getDimensions().getHeight());
			}
		}
	}
}
