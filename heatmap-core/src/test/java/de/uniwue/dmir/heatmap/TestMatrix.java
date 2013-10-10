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
package de.uniwue.dmir.heatmap;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class TestMatrix {

	public static void main(String[] args) {
		
		int m = 5;
		int n = 5;
		
		DenseMatrix64F A = new DenseMatrix64F(m,n);
		DenseMatrix64F x = new DenseMatrix64F(n,1);
		DenseMatrix64F b = new DenseMatrix64F(m,1);
		
		System.out.println(A);
		System.out.println(x);
		System.out.println(b);
		
		if( !CommonOps.solve(A,b,x) ) {
			throw new IllegalArgumentException("Singular matrix");
		}
		System.out.println(A);
		System.out.println(x);
		System.out.println(b);

	}
}
