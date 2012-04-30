/**
 * Copyright (C) 2010 Fábio Oliveira.
 * All rights reserved.
 *
 * This file is part of the IMPLANT project.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Fábio Oliveira nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package pt.com.gmv.lab.implant.rendering.poi.util;

/**
 * A 3x3 matrix that allows for various operations on it.
 * 
 * @author fao
 * @since 2
 */
public final class Matrix {
	/**
	 * The matrix elements size
	 */
	public final static int SIZE = 9;
	
	/**
	 * The array that supports the matrix
	 */
	protected final float[] mMatrix;
	
	/**
	 * Creates a matrix of a fixed size
	 */
	public Matrix() {
		mMatrix = new float[SIZE];
	}
	
	/**
	 * Creates a matrix from an array
	 * 
	 * @param matrix The array from which to create the matrix
	 */
	public Matrix(final float[] matrix) {
		this();
		
		set(matrix);
	}
	
	/**
	 * Sets the array of the matrix to passed values
	 * 
	 * @param matrix The array that represents the matrix
	 */
	public final void set(final float[] matrix) {
		System.arraycopy(matrix, 0, mMatrix, 0, SIZE);
	}

	/**
	 * Sets the current matrix with the values of the given matrix
	 * 
	 * @param m The matrix with the values to set.
	 */
	public final void set(final Matrix m) {
		set(m.mMatrix);
	}
	
	/**
	 * Clears the matrix values
	 */
	public final void clear() {
		set(new float[SIZE]);
	}

	/**
	 * Transforms the matrix in the identity matrix
	 */
	public final void toIdentity() {
		set(new float[] {
				1, 0, 0, 
				0, 1, 0, 
				0, 0, 1
		});
	}

	/**
	 * Transforms the matrix in a matrix rotating within some angle on the x axis
	 * 
	 * @param angleX The angle to rotate
	 */
	public final void toXRot(final double angleX) {
		set(new float[] {
				1, 0, 0,
				0, (float) Math.cos(angleX), (float) -Math.sin(angleX), 
				0, (float) Math.sin(angleX), (float) Math.cos(angleX)
		});
	}

	/**
	 * Transforms the matrix in a matrix rotating within some angle on the y axis
	 * 
	 * @param angleY The angle to rotate
	 */
	public final void toYRot(final double angleY) {
		set(new float[] {
				(float) Math.cos(angleY), 0, (float) Math.sin(angleY), 
				0, 1, 0, 
				(float) -Math.sin(angleY), 0, (float) Math.cos(angleY)
		});
	}

	/**
	 * Transforms the matrix in a matrix rotating within some angle on the z axis
	 * 
	 * @param angleZ The angle to rotate
	 */
	public final void toZRot(final double angleZ) {
		set(new float[] {
				(float) Math.cos(angleZ), (float) -Math.sin(angleZ), 0,
				(float) Math.sin(angleZ), (float) Math.cos(angleZ), 0, 
				0, 0, 1
		});
	}

	/**
	 * Transforms the matrix in a scaling matrix with the given factor
	 * 
	 * @param scale The scale factor
	 */
	public final void toScale(final float scale) {
		set(new float[] {
				scale, 0, 0, 
				0, scale, 0, 
				0, 0, scale
		});
	}

	/**
	 * Transforms a matrix with a camera vector and a point object, so an object can be transformed
	 * on screen.
	 * 
	 * @param cam The camera vector
	 * @param obj The object vector
	 */
	public final void toAt(final WorldVector cam, final WorldVector obj) {
		final WorldVector worldUp = new WorldVector(0, 1, 0);

		final WorldVector dir = new WorldVector();
		dir.set(obj);
		dir.subtract(cam);
		dir.multiply(-1f);
		dir.normalize();

		final WorldVector right = new WorldVector();
		right.cross(worldUp, dir);
		right.normalize();

		final WorldVector up = new WorldVector();
		up.cross(dir, right);
		up.normalize();

		set(new float[] {
				right.getX(), right.getY(), right.getZ(), 
				up.getX(), up.getY(), up.getZ(), 
				dir.getX(), dir.getY(), dir.getZ()
		});
	}
	
	/**
	 * Calculates the adjacency matrix
	 */
	public final void adj() {
		final float a11 = mMatrix[0];
		final float a12 = mMatrix[1];
		final float a13 = mMatrix[2];

		final float a21 = mMatrix[3];
		final float a22 = mMatrix[4];
		final float a23 = mMatrix[5];

		final float a31 = mMatrix[6];
		final float a32 = mMatrix[7];
		final float a33 = mMatrix[8];

		mMatrix[0] = smallDeterminant(a22, a23, a32, a33);
		mMatrix[1] = smallDeterminant(a13, a12, a33, a32);
		mMatrix[2] = smallDeterminant(a12, a13, a22, a23);

		mMatrix[3] = smallDeterminant(a23, a21, a33, a31);
		mMatrix[4] = smallDeterminant(a11, a13, a31, a33);
		mMatrix[5] = smallDeterminant(a13, a11, a23, a21);

		mMatrix[6] = smallDeterminant(a21, a22, a31, a32);
		mMatrix[7] = smallDeterminant(a12, a11, a32, a31);
		mMatrix[8] = smallDeterminant(a11, a12, a21, a22);
	}

	/**
	 * Inverts the matrix
	 */
	public final void invert() {
		final float det = determinant();

		adj();
		multiply(1 / det);
	}

	/**
	 * Transposes the matrix
	 */
	public final void transpose() {
		final float a21 = mMatrix[3];
		final float a23 = mMatrix[5];
		final float a31 = mMatrix[6];
		
		mMatrix[3] = mMatrix[1];
		mMatrix[1] = a21;
		
		mMatrix[5] = mMatrix[7];
		mMatrix[7] = a23;
		
		mMatrix[6] = mMatrix[2];
		mMatrix[2] = a31;
	}

	/**
	 * Calculates the matrix determinant
	 * 
	 * @return The determinant value
	 */
	public final float determinant() {
		return (mMatrix[0] * mMatrix[4] * mMatrix[8]) 
				- (mMatrix[0] * mMatrix[5] * mMatrix[7]) 
				- (mMatrix[1] * mMatrix[3] * mMatrix[8])
				+ (mMatrix[1] * mMatrix[5] * mMatrix[6]) 
				+ (mMatrix[2] * mMatrix[3] * mMatrix[7]) 
				- (mMatrix[2] * mMatrix[4] * mMatrix[6]);
	}

	/**
	 * Multiplies the matrix with a constant
	 * 
	 * @param c The constant value
	 */
	public final void multiply(final float c) {
		for(int i = 0; i < SIZE; ++i)
			mMatrix[i] *= c;
	}

	/**
	 * Adds two different matrices.
	 * 
	 * @param n The matrix to add
	 */
	public final void add(final Matrix n) {
		for(int i = 0; i < SIZE; ++i)
			mMatrix[i] += n.mMatrix[i];
	}

	/**
	 * Calculates the product of two matrices
	 * 
	 * @param n The matrix to calculate the product with
	 */
	public final void product(final Matrix n) {
		final Matrix m = new Matrix();
		m.set(this);

		mMatrix[0] = (m.mMatrix[0] * n.mMatrix[0]) + (m.mMatrix[1] * n.mMatrix[3]) + (m.mMatrix[2] * n.mMatrix[6]);
		mMatrix[1] = (m.mMatrix[0] * n.mMatrix[1]) + (m.mMatrix[1] * n.mMatrix[4]) + (m.mMatrix[2] * n.mMatrix[7]);
		mMatrix[2] = (m.mMatrix[0] * n.mMatrix[2]) + (m.mMatrix[1] * n.mMatrix[5]) + (m.mMatrix[2] * n.mMatrix[8]);

		mMatrix[3] = (m.mMatrix[3] * n.mMatrix[0]) + (m.mMatrix[4] * n.mMatrix[3]) + (m.mMatrix[5] * n.mMatrix[6]);
		mMatrix[4] = (m.mMatrix[3] * n.mMatrix[1]) + (m.mMatrix[4] * n.mMatrix[4]) + (m.mMatrix[5] * n.mMatrix[7]);
		mMatrix[5] = (m.mMatrix[3] * n.mMatrix[2]) + (m.mMatrix[4] * n.mMatrix[5]) + (m.mMatrix[5] * n.mMatrix[8]);
		
		mMatrix[6] = (m.mMatrix[6] * n.mMatrix[0]) + (m.mMatrix[7] * n.mMatrix[3]) + (m.mMatrix[8] * n.mMatrix[6]);
		mMatrix[7] = (m.mMatrix[6] * n.mMatrix[1]) + (m.mMatrix[7] * n.mMatrix[4]) + (m.mMatrix[8] * n.mMatrix[7]);
		mMatrix[8] = (m.mMatrix[6] * n.mMatrix[2]) + (m.mMatrix[7] * n.mMatrix[5]) + (m.mMatrix[8] * n.mMatrix[8]);
	}

	/**
	 * Calculates the 2x2 matrix determinant
	 * 
	 * @param a The first value
	 * @param b The second value
	 * @param c The third value
	 * @param d The fourth value
	 * 
	 * @return The determinant value
	 */
	private final float smallDeterminant(final float a, final float b, final float c, final float d) {
		return (a * d) - (b * c);
	}
}
