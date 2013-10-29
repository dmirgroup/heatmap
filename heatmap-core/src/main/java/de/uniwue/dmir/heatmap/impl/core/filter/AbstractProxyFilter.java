package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.uniwue.dmir.heatmap.core.IFilter;
import de.uniwue.dmir.heatmap.core.data.type.IExternalData;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractProxyFilter<E extends IExternalData, I> 
extends AbstractFilter<E, I>{

	private IFilter<E, I> filter;

	@Override
	public int getWidth() {
		return this.filter.getWidth();
	}

	@Override
	public int getHeight() {
		return this.filter.getHeight();
	}

	@Override
	public int getCenterX() {
		return this.filter.getCenterX();
	}

	@Override
	public int getCenterY() {
		return this.filter.getCenterY();
	}

}
