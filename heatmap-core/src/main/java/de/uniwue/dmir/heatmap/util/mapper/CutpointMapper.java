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
package de.uniwue.dmir.heatmap.util.mapper;

import java.util.Arrays;

public class CutpointMapper<TSource extends Comparable<TSource>, TDestination> 
implements IMapper<TSource, TDestination> {

	private TSource[] cutpoints;
	private TDestination[] values;
	private boolean equals;
	
	public CutpointMapper(TSource[] cutpoints, TDestination[] values, boolean equals) {
		this.cutpoints = cutpoints;
		this.values = values;
		this.equals = equals;
		
		if (this.cutpoints == null) {
			throw new NullPointerException("Cutpoints array must not be null.");
		}
		
		if (this.values == null) {
			throw new NullPointerException("Values array must not be null.");
		}
		
		checkIfDividersAreSortedAscending();
		checkObjectAndDividerCount();
	}

	private void checkObjectAndDividerCount() {
		if (this.values.length != this.cutpoints.length + 1) {
			throw new RuntimeException(
					String.format(
							"There must be exactly one more value "
							+ "than there are cutpoints. "
							+ "Given were %d objects and %d dividers.", 
							new Object[] {
									this.values.length, 
									this.cutpoints.length
							}));
		}
	}

	private void checkIfDividersAreSortedAscending() {
		
		TSource[] dividersCopy = Arrays.copyOf(
				this.cutpoints, 
				this.cutpoints.length);
		
		Arrays.sort(dividersCopy);
		
		if (!Arrays.equals(this.cutpoints, dividersCopy)) {
			throw new RuntimeException("Cutpoints were not sorted ascending.");
		}

	}

	public <TDerived extends TSource> TDestination map(TDerived value) {
		for (int i = 0; i < this.cutpoints.length; i++) {
			TSource cutpoint = this.cutpoints[i];
			int compare = value.compareTo(cutpoint);
			
			if (this.equals) {
				if (compare <= 0) {
					return this.values[i];
				}
			} else {
				if (compare < 0) {
					return this.values[i];
				}
			}
		}
		return this.values[this.values.length - 1];
	}

}