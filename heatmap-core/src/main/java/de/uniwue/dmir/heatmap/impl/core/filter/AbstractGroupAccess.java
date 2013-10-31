package de.uniwue.dmir.heatmap.impl.core.filter;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.core.ITileFactory;
import de.uniwue.dmir.heatmap.core.filter.IGroupAccess;

@AllArgsConstructor
public abstract class AbstractGroupAccess<I, T> implements IGroupAccess<I, T> {
	protected ITileFactory<I> tileFactory;
}
