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
package pt.com.gmv.lab.implant.readers.model;

import java.util.Hashtable;

import pt.com.gmv.lab.implant.rendering.poi.util.WorldVector;

import android.location.Location;

/**
 * A point of interest representation.
 * 
 * Aggregates every piece of information that defines a POI.
 * 
 * @author fao
 * @since 2
 */
/**
 * @author fabio
 *
 */
public class POI {
	
	/**
	 * The point of interest identifier
	 */
	private int mIdentifier;
	
	/**
	 * The point of interest name
	 * 
	 * Shown on screen below a POIModel
	 */
	private String mName;
	
	/**
	 * A short description of a point of interest
	 */
	private String mDescription;
	
	/**
	 * The fixed latitude of a point of interest
	 */
	private double mLatitude;
	/**
	 * The fixed longitude of a point of interest
	 */
	private double mLongitude;
	/**
	 * The altitude of a point of interest
	 * May not be used, as it is only useful to determine the elevation of a POIModel on screen
	 */
	private double mAltitude;
	
	/**
	 * An associative container that holds metadata for this point of interest
	 */
	private final Hashtable<String, String> mMetadata;
	
	/**
	 * Builds a POI with no associated information
	 */
	public POI() {
		mMetadata = new Hashtable<String, String>();
	}
	
	/**
	 * Builds a POI with geographic information
	 * 
	 * @param lat The POI latitude
	 * @param lon The POI longitude
	 * @param alt The POI altitude
	 * 
	 * @see POI#POI()
	 */
	public POI(double lat, double lon, double alt) {
		this();
		
		mLatitude = lat;
		mLongitude = lon;
		mAltitude = alt;
	}
	
	/**
	 * Builds a POI with complete information
	 * 
	 * @param name The POI name to show
	 * @param desc The POI description 
	 * @param lat The POI latitude
	 * @param lon The POI longitude
	 * @param alt The POI altitude
	 * 
	 * @see POI#POI(double, double, double)
	 */
	public POI(String name, String desc, double lat, double lon, double alt) {
		this(lat, lon, alt);
		
		mName = name;
		mDescription = desc;
	}
	
	/**
	 * Sets this POI identifier
	 * 
	 * @param mIdentifier The identifier number
	 */
	public void setIdentifier(int mIdentifier) {
		this.mIdentifier = mIdentifier;
	}

	/**
	 * Returns the identifier number for this POI
	 * 
	 * @return The identifier
	 */
	public int getIdentifier() {
		return mIdentifier;
	}

	/**
	 * Sets the POI name
	 * 
	 * @param mName The name
	 */
	public void setName(String mName) {
		this.mName = mName;
	}

	/**
	 * Returns the POI name
	 * 
	 * @return The name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the POI description
	 * 
	 * @param mDescription The description
	 */
	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	/**
	 * Returns the POI description
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Sets the POI latitude
	 * 
	 * @param mLatitude The latitude
	 */
	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	/**
	 * Returns the POI latitude
	 * 
	 * @return The latitude
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Sets the POI longitude
	 * 
	 * @param mLongitude The longitude
	 */
	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	/**
	 * Returns the POI longitude
	 * 
	 * @return The longitude
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * Sets the POI altitude
	 * 
	 * @param mAltitude The altitude
	 */
	public void setAltitude(double mAltitude) {
		this.mAltitude = mAltitude;
	}

	/**
	 * Returns the POI altitude
	 * 
	 * @return The altitude
	 */
	public double getAltitude() {
		return mAltitude;
	}
	
	/**
	 * Returns the metadata linked to a name
	 * 
	 * @param name The name key
	 * 
	 * @return The metadata value or <code>NULL</code> if not present
	 */
	public String getMetadata(String name) {
		if(!mMetadata.containsKey(name))
			return null;
		
		return mMetadata.get(name);
	}
	
	/**
	 * Adds metadata to the POI under a key-value pair.
	 * 
	 * @param name The key
	 * @param value The value
	 */
	public void addMetadata(String name, String value) {
		mMetadata.put(name, value);
	}
	
	/**
	 * Converts a location to a vector.
	 * 
	 * Works by first calculating the distance in the z axis between 2 positions and then for the x axis.
	 * Calculates the altitude (or y axis) by calculating the difference between the origin location and the POI.
	 * If there's a value that is negative, the absolute value is calculate.
	 * 
	 * @param org The origin location, represented by a Location object.
	 * @param poi The POI to where to calculate the distance.
	 * @param vec The vector from where to return the values.
	 */
	public static void convertLocationToVector(Location org, POI poi, WorldVector vec) {
		final float[] z = new float[1];
		Location.distanceBetween(org.getLatitude(), org.getLongitude(), poi.getLatitude(), org.getLongitude(), z);
		
		final float[] x = new float[1];
		Location.distanceBetween(org.getLatitude(), org.getLongitude(), org.getLatitude(), poi.getLongitude(), x);
		
		double y = poi.getAltitude() - org.getAltitude();
		
		if (org.getLatitude() < poi.getLatitude())
			z[0] *= -1;
		if (org.getLongitude() > poi.getLongitude())
			x[0] *= -1;

		vec.set(x[0], (float)y, z[0]);
	}
	
	/**
	 * Converts a vector to a Location with latitude and longitude.
	 * 
	 * Works by calculating the distance and bearing to the destination.
	 * 
	 * @param vec The vector to convert.
	 * @param org The origin location.
	 * @param poi The resulting POI, represented by a Location object.
	 */
	public static void convertVectorToLocation(WorldVector vec, Location org, Location poi) {
		final double brngNS = (vec.getZ() > 0) ? 180 : 0;
		final double brngEW = (vec.getX() < 0) ? 270 : 90;

		POI tmp1Loc = new POI();
		POI tmp2Loc = new POI();
		
		POI.calculateDestination(org.getLatitude(), org.getLongitude(), brngNS, Math.abs(vec.getZ()), tmp1Loc);
		POI.calculateDestination(tmp1Loc.getLatitude(), tmp1Loc.getLongitude(), brngEW, Math.abs(vec.getX()), tmp2Loc);

		poi.setLatitude(tmp2Loc.getLatitude());
		poi.setLongitude(tmp2Loc.getLongitude());
		poi.setAltitude(org.getAltitude() + vec.getY());
	}

	/**
	 * Calculates the latitude and longitude of a point based on an origin point and the distance to that point.
	 * 
	 * @param lat The origin latitude
	 * @param lon The origin longitude
	 * @param bear The bearing where the destination point is located
	 * @param d The distance to what axis the bearing point.
	 * @param dest The resulting point
	 */
	private static void calculateDestination(double lat, double lon, double bear, double d, POI dest) {
				
		final double brng = Math.toRadians(bear);
		final double lat1 = Math.toRadians(lat);
		final double lon1 = Math.toRadians(lon);
		final double R = 6371.0 * 1000.0; 

		final double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R) + Math.cos(lat1) * Math.sin(d / R) * Math.cos(brng));
		final double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / R) * Math.cos(lat1), Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

		dest.setLatitude(Math.toDegrees(lat2));
		dest.setLongitude(Math.toDegrees(lon2));
	}
}
