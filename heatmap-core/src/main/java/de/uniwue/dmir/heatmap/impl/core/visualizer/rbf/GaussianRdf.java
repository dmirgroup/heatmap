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
package de.uniwue.dmir.heatmap.impl.core.visualizer.rbf;

import de.uniwue.dmir.heatmap.impl.core.visualizer.RbfVisualizer.IRadialBasisFunction;

public class GaussianRdf implements IRadialBasisFunction {

	public static final double EPSILON = 0.5;
	
	private double epsilon;
	
	public GaussianRdf(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public GaussianRdf() {
		this(EPSILON);
	}
	
	@Override
	public double value(double value) {
		
		double weightedValue = value * this.epsilon;
		
		return Math.exp(-weightedValue * weightedValue);
	}

}
