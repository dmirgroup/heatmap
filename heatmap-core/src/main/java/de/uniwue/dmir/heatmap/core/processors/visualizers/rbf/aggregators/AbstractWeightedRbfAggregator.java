/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.aggregators;

import de.uniwue.dmir.heatmap.core.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.core.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.core.tiles.coordinates.RelativeCoordinates;

public abstract class AbstractWeightedRbfAggregator<TData> 
extends AbstractGenericRbfAggregator<TData> {

	public AbstractWeightedRbfAggregator(
			IMapper<TData, Double> pixelToValueMapper,
			IDistanceFunction<RelativeCoordinates> distanceFunction,
			IRadialBasisFunction radialBasisFunction) {
		super(pixelToValueMapper, distanceFunction, radialBasisFunction);
	}
	
	public AbstractWeightedRbfAggregator(
			IMapper<TData, Double> pixelToValueMapper,
			double pointRadius) {
		super(pixelToValueMapper, pointRadius);
	}

	protected double sumOfWeightedValues = 0;
	protected double sumOfWeights = 0;
	
	@Override
	public void reset() {
		this.sumOfWeightedValues = 0;
		this.sumOfWeights = 0;
	}
	
}