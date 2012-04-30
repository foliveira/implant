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

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import pt.com.gmv.lab.implant.ar.ImplantActivity;
import pt.com.gmv.lab.implant.exceptions.POILoadingException;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;
import pt.com.gmv.lab.implant.interfaces.readers.POIReader;
import pt.com.gmv.lab.implant.interfaces.rendering.poi.POIModel;
import pt.com.gmv.lab.implant.readers.model.POIGroup;
import pt.com.gmv.lab.implant.rendering.poi.util.Matrix;
import pt.com.gmv.lab.implant.rendering.surfaces.POIView;

/**
 * The base activity for point of interest exhibition applications.
 * 
 * @author fao
 * @since 2
 */
public abstract class POIActivity extends ImplantActivity {
	/**
	 * A Criteria object with no restrictions.
	 */
	private static final Criteria NO_RESTRICTIONS = new Criteria();
	
	/**
	 * The default radar range for the application.
	 */
	private static final float RADAR_RANGE = 10f;
	
	/**
	 * The Georeference object that holds geographic information of the device
	 */
	private Georeference mGeo = new Georeference();
	
	/**
	 * A collection containing all POIModels to be shown on screen.
	 * It's a synchronizedCollection to assure that every access to it's items is done in a
	 * synchronized fashion
	 */
	private Collection<POIModel> mPoiModels = Collections.synchronizedCollection(new LinkedList<POIModel>());
	
	/**
	 * Object representing the OrientationService that gathers orientation information about the device
	 */
	private OrientationService mOrientation;
	/**
	 * Object representing the LocationService that gathers location information about the device
	 */
	private LocationService mLocation;
	
	/** Location Service related fields **/
	/**
	 * The Android system Location Manager
	 */
	private LocationManager mLocationManager;
	/**
	 * The selected location updates provider
	 */
	private String mLocationProvider;
	
	/** Orientation Service related fields **/
	/**
	 * The Android system Sensor Manager
	 */
	private SensorManager mSensorManager;
	/**
	 * The Sensor that represents the device accelerometer
	 */
	private Sensor mAccelerometer;
	/**
	 * The Sensor that represents the device magnetometer
	 */
	private Sensor mMagnetometer;
	/**
	 * An array to store the magnetic readings
	 */
	private float[] mMagneticValues = new float[3];
	/**
	 * An array to store the accelerometer readings
	 */
	private float[] mAccelerometerValues = new float[3];
	/**
	 * The calculated rotation matrix
	 */
	private float[] mRotationMatrix = new float[9];
	
	/**
	 * The index on the <code>mMatrixHistory</code> array
	 */
	private int mHistoryIndex = 0;
	/**
	 * An array to reduce digital noise on the orientation readings
	 */
	private Matrix[] mMatrixHistory = new Matrix[60];
	
	/* Matrices that help calculating the final orientation matrix */
	private Matrix mTempMatrix = new Matrix();
	private Matrix mFinalMatrix = new Matrix();
	private Matrix mFirstRotation = new Matrix();
	private Matrix mSecondRotation = new Matrix();
	private Matrix mThirdRotation = new Matrix();
	private Matrix mDeclinationRotation = new Matrix();

	/**
	 * Sets the rendering surface for the application
	 * 
	 * @see ImplantActivity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		setRenderingView();
		
		super.onCreate(savedInstanceState);
	}

	/**
	 * Sets the location and orientation services and clears the digital noise filter array.
	 * 
	 * @see ImplantActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		double angle = Math.toRadians(-90);
		
		mFirstRotation.toXRot(angle);
		mSecondRotation.toYRot(angle);		
		mThirdRotation.toXRot(angle);

		for (int i = 0; i < mMatrixHistory.length; i++)
			mMatrixHistory[i] = new Matrix();
		
		setGeoreferenceServices();
	}

	/**
	 * Removes the updates from the various sensors
	 * 
	 * @see ImplantActivity#onPause()
	 */
	@Override
	protected void onPause() {
		mLocationManager.removeUpdates(mLocation);
		mSensorManager.unregisterListener(mOrientation);
		
		super.onPause();
	}
	
	/**
	 * Sets the rendering view with a default radar range.
	 * 
	 * @see #RADAR_RANGE
	 * @see ImplantActivity#setRenderingView()
	 */
	@Override
	protected View setRenderingView() {
		return setRenderingView(RADAR_RANGE);
	}
	
	/**
	 * Sets the rendering view with the given radar range
	 * 
	 * @param radarRange The range in kilometers
	 * @return A <code>POIView</code> object
	 */
	protected View setRenderingView(final float radarRange) {
		return new POIView(this, mGeo, mPoiModels, radarRange);
	}
	
	/**
	 * Adds a collection of POIModel to the application
	 * 
	 * @param pois The given collection of POIModels
	 * @return A coolection of POIModels
	 */
	protected boolean addPOIModels(final Collection<? extends POIModel> pois) {
		return mPoiModels.addAll(pois);
	}
	
	/**
	 * Removes a given POIModel from the application
	 * 
	 * @param poi The POIModel to remove
	 * @return <code>True</code> if the POIModel was successfully removed
	 */
	protected boolean removePOI(final POIModel poi) {
		return mPoiModels.remove(poi);
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.ar.ImplantActivity#setActivityLayout()
	 */
	@Override
	protected void setActivityLayout() {
		final FrameLayout frameLayout = new FrameLayout(this);

		frameLayout.setMinimumWidth(3000);
		frameLayout.setPadding(10, 0, 10, 10);

		setContentView(cameraPreview);

		addContentView(renderingView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(frameLayout, new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));	
	}

	/**
	 * Registers the listeners for the different needed sensors.
	 * 
	 * For the Location service, it gets the best available provider and request location updates from it,
	 * setting the current location as the last known location from the given provider.
	 * 
	 * As for the Orientation service, a listener is registered for the default accelerometer and magnetometer
	 * on the device.
	 */
	protected void setGeoreferenceServices() {
		mOrientation = new OrientationService();
		mLocation = new LocationService();
		
		/** Location updates **/
		{
			mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
			
			mLocationProvider = mLocationManager.getBestProvider(NO_RESTRICTIONS, true); 
			
			if(mLocationManager.isProviderEnabled(mLocationProvider))
				mLocationManager.requestLocationUpdates(mLocationProvider, 5000, 0, mLocation);
			
			Location loc = mLocationManager.getLastKnownLocation(mLocationProvider);

			mGeo.setLocation(loc);
			mDeclinationRotation.toYRot(Math.toRadians(-mGeo.getGeomagneticField().getDeclination()));
		}
		
		/** Orientation updates **/
		{
			mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
			
			mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	
			mSensorManager.registerListener(mOrientation, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(mOrientation, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	/**
	 * @see ImplantActivity#setCameraPreviewHandler()
	 */
	@Override
	protected abstract CameraPreviewHandler setCameraPreviewHandler();

	/**
	 * Override to create and return a collection of POIModel from the information gathered from various
	 * data sources.
	 * 
	 * @see #getPOIFromStream(POIReader, InputStream)
	 * 
	 * @return A collection containing POIModels
	 */
	protected abstract Collection<? extends POIModel> createPOIModels();

	/**
	 * Reads information from a stream using the designated reader, returning a list with POIGroups
	 * 
	 * @param reader The reader that knows how to read the type of data.
	 * @param stream The stream from where to read the data
	 * 
	 * @return A list containing all the POIGroups read from the stream.
	 * 
	 * @throws POILoadingException When reader encounters a problem with the data from the stream.
	 */
	public static Collection<POIGroup> getPOIFromStream(final POIReader reader, final InputStream stream) throws POILoadingException {
		return new LinkedList<POIGroup>(reader.read(stream));
	}
	
	/**
	 * A class that receives updates from the device's sensors.
	 * 
	 * @author fao
	 */
	public final class OrientationService implements SensorEventListener {
		/* (non-Javadoc)
		 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
		 */
		@Override
		public final void onAccuracyChanged(Sensor sensor, int accuracy) {
			/** Nothing to do here **/
		}

		/**
		 * According to the type of the Sensor that fired the update, the values are copied to the
		 * given array and a calculation that produces the rotation matrix, is done, from where the
		 * application can extract the azimuth and pitch of the device.
		 * 
		 * The data read is filtered by a digital noise filter in the process and suffers some transformations
		 * in between, given that the Android system reports the values in strange manner, which needs
		 * some post-processing.
		 */
		@Override
		public final void onSensorChanged(SensorEvent event) {
			switch(event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					System.arraycopy(event.values, 0, mAccelerometerValues, 0 , 3);
					renderingView.postInvalidate();
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					System.arraycopy(event.values, 0, mMagneticValues, 0 , 3);
					renderingView.postInvalidate();
					break;
				default:
					break;
			}

			if(SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerValues, mMagneticValues)){
				SensorManager.remapCoordinateSystem(mRotationMatrix.clone(), SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z, mRotationMatrix);
				
				mTempMatrix.set(mRotationMatrix);
				mFinalMatrix.toIdentity();
				mFinalMatrix.product(mDeclinationRotation);
				mFinalMatrix.product(mFirstRotation);
				mFinalMatrix.product(mTempMatrix);
				mFinalMatrix.product(mSecondRotation);
				mFinalMatrix.product(mThirdRotation);
				mFinalMatrix.invert();
				
				mMatrixHistory[mHistoryIndex++].set(mFinalMatrix);
				mHistoryIndex %= mMatrixHistory.length;

				mFinalMatrix.clear();
				for (int i = 0; i < mMatrixHistory.length; ++i)
					mFinalMatrix.add(mMatrixHistory[i]);
				
				mFinalMatrix.multiply(1 / (float) mMatrixHistory.length);
				
				mGeo.setRotation(mFinalMatrix);
			}
		}
	}
	
	/**
	 * A class that receives updates from the location sensors.
	 * 
	 * @author fao
	 */
	public final class LocationService implements LocationListener {


		/**
		 * When the location of the device has changed, the Georeference object is used to update the location
		 * 
		 * @see Georeference#setLocation(Location)
		 */
		@Override
		public final void onLocationChanged(Location location) {
			mGeo.setLocation(location);
			
			mDeclinationRotation.toYRot(Math.toRadians(-mGeo.getGeomagneticField().getDeclination()));
		}

		/**
		 * When a provider is disabled an attempt is made to request location updates from a new provider
		 */
		@Override
		public final void onProviderDisabled(String provider) {
			mLocationManager.removeUpdates(this);
			
			mLocationProvider = mLocationManager.getBestProvider(NO_RESTRICTIONS, false);
			mLocationManager.requestLocationUpdates(mLocationProvider, 1000, 25, this);
		}

		/**
		 * When a new provider is enabled it is compared to the current one and enabled if better.
		 * 
		 * A provider is considered better if it has a better accuracy than the one already enabled.
		 */
		@Override
		public final void onProviderEnabled(String provider) {
			LocationProvider enabled = mLocationManager.getProvider(provider);
			LocationProvider current = mLocationManager.getProvider(mLocationProvider);
			
			if((current.getAccuracy() != Criteria.ACCURACY_FINE) && (enabled.getAccuracy() == Criteria.ACCURACY_FINE)) {
				mLocationManager.removeUpdates(this);
				
				mLocationProvider = provider;
				mLocationManager.requestLocationUpdates(provider, 0, 0, this);
			}
		}

		/* (non-Javadoc)
		 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
		 */
		@Override
		public final void onStatusChanged(String provider, int status, Bundle extras) {
			//No implementation
		}
	}
}
