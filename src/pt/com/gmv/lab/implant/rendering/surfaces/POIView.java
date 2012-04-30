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
package pt.com.gmv.lab.implant.rendering.surfaces;

import java.util.Collection;
import java.util.Collections;

import pt.com.gmv.lab.implant.ar.poi.Georeference;
import pt.com.gmv.lab.implant.interfaces.rendering.poi.POIModel;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.rendering.poi.Radar;
import pt.com.gmv.lab.implant.rendering.poi.Radar.FieldOfVision;
import pt.com.gmv.lab.implant.rendering.poi.util.ProjectionCamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;

/**
 * A surface where the POI models are painted and superimposed on top of a capture from the camera.
 * 
 * @author fao
 * @since 2
 */
public class POIView extends View implements OnClickListener {
	/**
	 * A collection containing all the POI to be painted on screen
	 */
	private final Collection<? extends POIModel> mPois;
	/**
	 * The object that holds all location and orientation information for the device
	 */
	private Georeference mGeo;
	/**
	 * The device radar to be drawn on screen
	 */
	private Radar mRadar;
	
	/**
	 * The global paint object to be used to paint
	 */
	private final Paint mPaint;
    /**
     * A gesture detector to detect simple gestures on screen
     */
    private final GestureDetector mGestureDetector;
    /**
     * A listener for gestures on screen
     */
    private final View.OnTouchListener mGestureListener;
	
	/**
	 * If every structure has been initiated
	 */
	private boolean mIsInited;
	/**
	 * The camera to use in our projections
	 */
	private ProjectionCamera mCamera;
	
	/**
	 * Creates a new surface to draw POIs on.
	 * 
	 * Sets all listeners for gestures on screen.
	 * 
	 * @param context The application context
	 * @param geo The geographic information object
	 * @param pois The POIs collection
	 * @param range The starting range for the radar
	 */
	public POIView(Context context, Georeference geo, Collection<? extends POIModel> pois, float range) {
		super(context);
			
		mGeo = geo;
		mPois = pois;
		
		setRadar(range);
		
		mPaint = new Paint();		
		mPaint.setAntiAlias(true);
		
		mGestureDetector = new GestureDetector(new ScreenGestureDetector());
        mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
            	return mGestureDetector.onTouchEvent(event);
            }
        };
        
        setOnTouchListener(mGestureListener);
        setOnClickListener(POIView.this);
	}

	/**
	 * Draws all POIs that are deemed visible on screen and are within range.
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(!mIsInited) {
			mCamera = new ProjectionCamera(canvas.getWidth(), canvas.getHeight());
			mCamera.setViewAngle(FieldOfVision.DEFAULT_VIEW_ANGLE);
			mIsInited = true;
		}
		
		float bearing = mGeo.getAzimuth();
		Location currentLocation = mGeo.getLocation();
		mCamera.setRotation(mGeo.getRotation());
		
		synchronized (mPois) {
			for(POIModel poi : mPois) {
				POI p = poi.getPOIInfo();
				float[] dist = new float[1];
				
				Location.distanceBetween(p.getLatitude(), p.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), dist);
							
				if (dist[0] < mRadar.getRange()) {
					poi.update(currentLocation);
					poi.performWorldTransformations(mCamera);
					poi.draw(canvas, mPaint);
				}
			}			
		}
		
		mRadar.paint(canvas, mPaint, bearing, mPois);
	}
	
	/**
	 * Does nothing
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		return;
    }
	
	/**
	 * Returns a read-only copy of the POIs collection
	 * 
	 * @return A read-only collection of POIs
	 */
	protected Collection<? extends POIModel> getPOIModels() {
		return Collections.unmodifiableCollection(mPois);
	}

	
	/**
	 * Returns the object holding location and orientation information
	 * 
	 * @return The georeference object
	 */
	protected Georeference getGeoreferenciation() {
		return mGeo;
	}
	
	/**
	 * Returns the screen radar
	 * 
	 * @return The radar
	 */
	protected Radar getRadar() {
		return mRadar;
	}
	
	/**
	 * Sets the application radar
	 * 
	 * @param radar The radar
	 * @param range The starting range for the radar
	 */
	protected void setRadar(Radar radar, float range) {
		mRadar = radar;
		mRadar.setRange(range);
	}	
	
	/**
	 * Sets a default radar with a starting range
	 * 
	 * @param range The starting range
	 */
	protected void setRadar(float range) {
		setRadar(new Radar(), range);
	}
	
	/**
	 * Creates a simple screen gesture detector
	 */
	private class ScreenGestureDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 250;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	    
		/**
		 * For a tap on screen, check if a POI was clicked and if it was the {@link POIModel#onClick(Context, MotionEvent)} is triggered
		 * 
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onSingleTapConfirmed(android.view.MotionEvent)
		 */
		@Override
		public final boolean onSingleTapConfirmed(MotionEvent e) {
			final float x = e.getX();
			final float y = e.getY();

		for(POIModel poi : mPois) {				
				if(poi.isClickValid(x, y))
					return poi.onClick(getContext(), e);
			}
			
			return false;
		}

		/* (non-Javadoc)
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
		 */
		@Override
		public final boolean onDown(MotionEvent e) {
			return super.onDown(e);
		}

		/**
		 * If a fling gesture is detected across the screen the radar range is increased or decreased.
		 * 
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH || Math.abs(velocityX) <= SWIPE_THRESHOLD_VELOCITY)
                return false;
            
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
            	mRadar.setRange(mRadar.getRangeInKilometers() / 2);
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
            	mRadar.setRange(mRadar.getRangeInKilometers() * 2);
            }
            
	        return true;
	    }
	}
}
