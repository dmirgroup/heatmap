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
