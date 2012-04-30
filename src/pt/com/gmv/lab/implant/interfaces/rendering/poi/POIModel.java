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
package pt.com.gmv.lab.implant.interfaces.rendering.poi;

import pt.com.gmv.lab.implant.interfaces.rendering.poi.POIModel;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.rendering.poi.util.ProjectionCamera;
import pt.com.gmv.lab.implant.rendering.poi.util.WorldVector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.view.MotionEvent;

/**
 * A POIModel represents a POI on the screen.
 * 
 * It has a POI associated that has all the information related to the physical place.
 * 
 * @author fao
 * @since 2
 */
public interface POIModel {

	/**
	 * Returns a vector with the POIModel location on screen.
	 * 
	 * @return A vector containing the screen location
	 */
	public WorldVector getScreenLocation();

	/**
	 * Returns the POI location on the world.
	 * 
	 * It's not equal to the GPS location of the point, since these values represent
	 * a distance to the actual device location.
	 * 
	 * @return A vector containing the world location of the POI to the device.
	 */
	public WorldVector getPOILocation();

	/**
	 * Returns the POI object that contains all relevant information about the point of interest.
	 * 
	 * @return The associated POI object.
	 */
	public POI getPOIInfo();

	/**
	 * If the POIModel is visible on screen.
	 * 
	 * @return <code>True</code> if the POIModel is visible on screen
	 */
	public boolean isVisible();

	/**
	 * If the POIModel is in a small strip at the center of the screen
	 * 
	 * @return <code>True</code> if the POIModel is in that small strip.
	 */
	public boolean isAtCenter();

	/**
	 * Draw the POIModel on screen
	 * 
	 * @param canvas The canvas where to draw
	 * @param paint The Paint object to use in the drawing routine
	 */
	public void draw(Canvas canvas, Paint paint);

	/**
	 * Updates the POIModel location values based on the device current location.
	 * 
	 * @param current The Location object containing the device coordinates.
	 */
	public void update(Location current);

	/**
	 * Performs the necessary transformations to the POIModel according to the last
	 * known location of the device.
	 * 
	 * Should affect the World and Screen location of the POIModel.
	 * 
	 * @param mCamera The camera used to help apply the transformations
	 */
	public void performWorldTransformations(ProjectionCamera mCamera);

	/**
	 * What should happen when a click on a POIModel on the screen.
	 * 
	 * @param context The application context
	 * @param me The MotionEvent object of the click
	 * 
	 * @return <code>True</code> in case the click is considered valid
	 */
	public boolean onClick(Context context, MotionEvent me);

	/**
	 * Reports if a click is valid for a given POIModel
	 * 
	 * @param x The click x coordinate
	 * @param y The clock y coordinate
	 * 
	 * @return <code>True</code> if the click is valid.
	 */
	public boolean isClickValid(float x, float y);

}