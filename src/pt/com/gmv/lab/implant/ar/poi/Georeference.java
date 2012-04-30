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
package pt.com.gmv.lab.implant.ar.poi;

import pt.com.gmv.lab.implant.helpers.PointHelpers;
import pt.com.gmv.lab.implant.rendering.poi.util.Matrix;
import pt.com.gmv.lab.implant.rendering.poi.util.WorldVector;
import android.hardware.GeomagneticField;
import android.location.Location;

/**
 * Gathers all information about the geographic location of the device
 * 
 * @author fao
 * @since 2
 */
public final class Georeference {

	/**
	 * The current device location represented by a Location object.
	 */
	private Location 	mLocation;
	
	/**
	 * The rotation matrix representing the device orientation
	 */
	private Matrix 		mRotation = new Matrix();

	/**
	 * Sets the device current location
	 * 
	 * @param location The Location object containing the device location
	 */
	public final void setLocation(Location location) {
		
		mLocation = location;
	}

	/**
	 * Returns the device location
	 * 
	 * @return A Location object containing the device location.
	 */
	public final Location getLocation() {
		return mLocation;
	}

	/**
	 * Sets the device rotation
	 * 
	 * @param rotation The rotation matrix
	 */
	public final void setRotation(Matrix rotation) {
		synchronized (this) {
			mRotation.set(rotation);
		}
	}
	
	/**
	 * Returns the device rotation in the form of a matrix.
	 * 
	 * @return The device rotation matrix.
	 */
	public final Matrix getRotation() {
		return mRotation;
	}

	/**
	 * Returns the device current azimuth.
	 * 
	 * The value is calculated with the rotation matrix.
	 * 
	 * @return The device azimuth is in the scale 0 <= azimuth <= 360.
	 */
	public final float getAzimuth() {
		Matrix tmp = new Matrix();
		tmp.set(mRotation);
		WorldVector looking = new WorldVector(1, 0, 0);
		
		synchronized (this) {
			tmp.transpose();
			looking.product(tmp);
			return (int) (PointHelpers.getAngle(0, 0, looking.getX(), looking.getZ())  + 360 ) % 360 ;
		}
	}
	
	/**	 
 	 * Returns the device current pitch.
	 * 
	 * The value is calculated with the rotation matrix.
	 * 
	 * @return The device pitch is in the scale 0.0 <= pitch <= 1.0.
	 */
	public final float getPitch() {
		WorldVector looking = new WorldVector(0, 1, 0);
		synchronized (this) {
			looking.product(mRotation);
			return -PointHelpers.getAngle(0, 0, looking.getY(), looking.getZ());	
		}
	}
	
	/**
	 * Builds a new GeomagneticField object, with each call, for the device current location
	 * 
	 * @see GeomagneticField#getDeclination()
	 * 
	 * @return The GeomagneticField
	 */
	public final GeomagneticField getGeomagneticField() {
		return new GeomagneticField((float) mLocation.getLatitude(), 
									(float) mLocation.getLongitude(), 
									(float) mLocation.getAltitude(), 
									System.currentTimeMillis());
	}
	
	/**
	 * Calculates the distance from the current location to a given location.
	 * 
	 * @param location The location to calculate the distance from the current location.
	 * 
	 * @return An array with the distance between the two points in meters and the bearing of the distance.
	 */
	public final float[] getDistanceTo(final Location location) {
		final float[] results = new float[2];
		
		Location.distanceBetween(mLocation.getLatitude(), mLocation.getLongitude(), location.getLatitude(), location.getLongitude(), results);
		
		return results;		
	}
}
