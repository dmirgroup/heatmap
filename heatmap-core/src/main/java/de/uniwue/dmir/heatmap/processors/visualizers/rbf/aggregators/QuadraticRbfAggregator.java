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
package de.uniwue.dmir.heatmap.processors.visualizers.rbf.aggregators;

import lombok.AllArgsConstructor;
import de.uniwue.dmir.heatmap.filters.operators.IMapper;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.IDistanceFunction;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.IRadialBasisFunction;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.ReferencedData;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.distances.EuclidianDistance;
import de.uniwue.dmir.heatmap.processors.visualizers.rbf.rbfs.GaussianRbf;
import de.uniwue.dmir.heatmap.tiles.coordinates.RelativeCoordinates;
import de.uniwue.dmir.heatmap.util.IAggregator;
import de.uniwue.dmir.heatmap.util.IAggregatorFactory;

public class QuadraticRbfAggregator<TData> 
extends AbstractWeightedRbfAggregator<TData> {

	public QuadraticRbfAggregator(
			IMapper<TData, Double> pixelToValueMapper,
			IDistanceFunction<RelativeCoordinates> distanceFunction,
			IRadialBasisFunction radialBasisFunction) {
		super(pixelToValueMapper, distanceFunction, radialBasisFunction);
	}
	
	public QuadraticRbfAggregator(
			IMapper<TData, Double> pixelToValueMapper,
			double pointRadius) {
		super(pixelToValueMapper, pointRadius);
	}

	@Override
	public Double getAggregate() {
		return this.sumOfWeightedValues / this.sumOfWeights;
	}

	@Override
	protected void addData(double value, double distanceWeight) {
		this.sumOfWeightedValues += value * distanceWeight * distanceWeight;
		this.sumOfWeights += distanceWeight;
	}
	
	@AllArgsConstructor
	public static final class Factory<TData>
	implements IAggregatorFactory<ReferencedData<TData>, Double> {
		
		private IMapper<TData, Double> pixelToValueMapper;
		private IDistanceFunction<RelativeCoordinates> distanceFunction;
		private IRadialBasisFunction radialBasisFunction;

		public Factory(
				IMapper<TData, Double> pixelToValueMapper,
				double pointRadius) {
			
			this(
					pixelToValueMapper,
					new EuclidianDistance(),
					new GaussianRbf(pointRadius));
		}
		
		
		@Override
		public IAggregator<ReferencedData<TData>, Double> getInstance() {
			return new QuadraticRbfAggregator<TData>(
					this.pixelToValueMapper, 
					this.distanceFunction,
					this.radialBasisFunction);
		}
		
	}
	
}