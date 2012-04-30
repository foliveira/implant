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
 * Represents a camera that performs point projections on the screen
 * 
 * @author fao
 * @since 2
 */
public final class ProjectionCamera {
	/**
	 * The camera width (usually the screen width)
	 */
	private int mWidth;
	
	/**
	 * The camera height (usually the screen height)
	 */
	private int mHeight;
	
	/**
	 * The distance calculated with the view angle for the camerea
	 */
	private float mDistance;

	/**
	 * The device rotation matrix
	 */
	private final Matrix mRotation = new Matrix();
	
	/**
	 * Creates a new camera with a given size
	 * 
	 * @param width The camera width
	 * @param height The camera height
	 */
	public ProjectionCamera(int width, int height) {
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * Sets the camera view angle
	 * 
	 * @param viewAngle The camera view angle
	 */
	public final void setViewAngle(float viewAngle) {
		mDistance = (mWidth / 2) / (float) Math.tan(viewAngle / 2);
	}
	
	/**
	 * Calculates a point projection based on an origin point.
	 * 
	 * @param orgPoint The origin point
	 * @param prjPoint The projection point
	 * @param addX A factor to add in the x axis of the point
	 * @param addY A factor to add in the y axis of the point
	 */
	public final void projectPoint(WorldVector orgPoint, WorldVector prjPoint, float addX, float addY) {
		float x = mDistance * orgPoint.getX() / -orgPoint.getZ();
		float y = mDistance * orgPoint.getY() / -orgPoint.getZ();
		float z = orgPoint.getZ();
		
		x = x + addX + mWidth / 2;
		y = -y + addY + mHeight / 2;

		prjPoint.set(x, y, z);
	}
	
	/**
	 * Returns the camera width
	 * 
	 * @return The width
	 */
	public final int getWidth() {
		return mWidth;
	}
	
	/**
	 * Returns the camera height
	 * 
	 * @return the height
	 */
	public final int getHeight() {
		return mHeight;
	}
	
	/**
	 * Sets the camera rotation matrix
	 * 
	 * @param m The matrix to set
	 */
	public final void setRotation(Matrix m) {
		mRotation.set(m);
	}
	
	/**
	 * Returns the camera rotation matrix
	 * 
	 * @return The rotation matrix
	 */
	public final Matrix getRotation() {
		return mRotation;
	}
}
