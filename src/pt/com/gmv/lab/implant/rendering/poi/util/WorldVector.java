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
 * A vector that represents a point in the world
 * 
 * @author fao
 * @since 2
 */
public final class WorldVector {

	/**
	 * The point position in the world x axis
	 */
	private float mX;
	/**
	 * The point position in the world y axis
	 */
	private float mY;
	/**
	 * The point position in the world z axis
	 */
	private float mZ;
	
	/**
	 * Creates a vector centered in the origin
	 */
	public WorldVector() {
		this(0, 0, 0);
	}
	
	/**
	 * Creates a vector with a set of coordinates
	 * 
	 * @param x The x axis coordinate
	 * @param y The y axis coordinate
	 * @param z The z axis coordinate
	 */
	public WorldVector(float x, float y, float z) {
		set(x, y, z);
	}

	/**
	 * Returns the x axis value
	 * 
	 * @return The x axis value
	 */
	public final float getX() {
		return mX;
	}
	/**
	 * Returns the y axis value
	 * 
	 * @return The y axis value
	 */
	public final float getY() {
		return mY;
	}
	/**
	 * Returns the z axis value
	 * 
	 * @return The z axis value
	 */
	public final float getZ() {
		return mZ;
	}

	/**
	 * Sets the values of the vector with a given set of values
	 * 
	 * @param x The x axis value
	 * @param y The y axis value
	 * @param z The z axis value
	 */
	public final void set(float x, float y, float z) {
		mX = x;
		mY = y;
		mZ = z;		
	}

	/**
	 * Sets the values of the vector with the values of another vector.
	 * 
	 * @param vec The vector containing the values to set
	 */
	public final void set(WorldVector vec) {
		set(vec.mX, vec.mY, vec.mZ);		
	}
	
	/**
	 * Adds a set of values to current vector coordinates
	 * 
	 * @param x The x axis value
	 * @param y The y axis value
	 * @param z The z axis value
	 */
	public final void add(float x, float y, float z) {
		mX += x;
		mY += y;
		mZ += z;
	}
	
	/**
	 * Adds two vectors together
	 * 
	 * @param vec The vector to add to the current one
	 */
	public final void add(WorldVector vec) {
		add(vec.mX, vec.mY, vec.mZ);
	}
	
	/**
	 * Subtracts a set of values from the vector coordinates.
	 * 
	 * @param x The x axis value
	 * @param y The y axis value
	 * @param z The z axis value
	 */
	public final void subtract(float x, float y, float z) {
		add(-x, -y, -z);
	}
	
	/**
	 * Subtracts two vectors
	 * 
	 * @param vec The vector to subtract from the current one
	 */
	public final void subtract(WorldVector vec) {
		add(-vec.mX, -vec.mY, -vec.mZ);
	}
	
	/**
	 * Multiplies the coordinates of the vector by a constant
	 * 
	 * @param s The constant to multiply for
	 */
	public final void multiply(float s) {
		mX *= s;
		mY *= s;
		mZ *= s;
	}

	/**
	 * Divides the coordinates of the vector by a constant
	 * 
	 * @param s The constant to divide by
	 */
	public final void divide(float s) {
		mX /= s;
		mY /= s;
		mZ /= s;
	}

	/**
	 * Calculates the length of the vector
	 * 
	 * @return The length of the vector
	 */
	public final float length() {
		return (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
	}

	/**
	 * Normalizes the vector.
	 */
	public final void normalize() {
		divide(length());
	}
	
	/**
	 * Calculates the dot value between this vector and another one.
	 * 
	 * @param v The other vector
	 * 
	 * @return The dot value
	 */
	public final float dot(WorldVector v) {
		return mX * v.mX + mY * v.mY + mZ * v.mZ;
	}

	/**
	 * Transforms the vector into the cross vector of two other vectors.
	 * 
	 * @param u The first vector
	 * @param v The second vector
	 */
	public final void cross(WorldVector u, WorldVector v) {
		mX = u.mY * v.mZ - u.mZ * v.mY;
		mY = u.mZ * v.mX - u.mX * v.mZ;
		mZ = u.mX * v.mY - u.mY * v.mX;
	}

	/**
	 * Calculates the product of the vector with a matrix represented by an array.
	 * 
	 * @param m The matrix array
	 */
	public final void product(float[] m) {
		final float xTemp = m[0] * mX + m[1] * mY + m[2] * mZ;
		final float yTemp = m[3] * mX + m[4] * mY + m[5] * mZ;
		final float zTemp = m[6] * mX + m[7] * mY + m[8] * mZ;

		mX = xTemp;
		mY = yTemp;
		mZ = zTemp;
	}
	
	/**
	 * Calculates the product between a matrix and the vector
	 * 
	 * @param m The matrix
	 */
	public final void product(Matrix m) {
		product(m.mMatrix);
	}
	
	/**
	 * Calculates the hash code of the object taking into account the vector coordinate values.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(mX);
		result = prime * result + Float.floatToIntBits(mY);
		result = prime * result + Float.floatToIntBits(mZ);
		return result;
	}

	/**
	 * Does a comparison between the coordinates of the vector and another vector.
	 * 
	 * @return <code>True</code> if the vector have the same coordinates
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WorldVector)) {
			return false;
		}
		WorldVector other = (WorldVector) obj;
		if (Float.floatToIntBits(mX) != Float.floatToIntBits(other.mX)) {
			return false;
		}
		if (Float.floatToIntBits(mY) != Float.floatToIntBits(other.mY)) {
			return false;
		}
		if (Float.floatToIntBits(mZ) != Float.floatToIntBits(other.mZ)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Compares the vector to a set of coordinates.
	 * 
	 * @param x The x axis coordinate
	 * @param y The y axis coordinate
	 * @param z The z axis coordinate
	 * 
	 * @return <code>True</code> if the vector and coordinates matches
	 */
	public final boolean equals(float x, float y, float z) {
		return (Float.floatToIntBits(x) == Float.floatToIntBits(mX))
				&& (Float.floatToIntBits(y) == Float.floatToIntBits(mY))
				&& (Float.floatToIntBits(z) == Float.floatToIntBits(mZ));
	}
}
