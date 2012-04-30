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
package pt.com.gmv.lab.implant.ar.marker;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import pt.com.gmv.lab.implant.ar.ImplantActivity;
import pt.com.gmv.lab.implant.ar.marker.util.Marker;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;
import pt.com.gmv.lab.implant.rendering.marker.ModelLoaders;
import pt.com.gmv.lab.implant.rendering.surfaces.MarkerSurface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * The base activity for marker detection applications.
 *
 * @author fao
 * @since 1
 */
public abstract class MarkerDetectionActivity extends ImplantActivity {
	/**
	 * An associative container that links markers identification and marker marker information.
	 */
	private final Map<Integer, Marker> mMarkers = Collections.synchronizedMap(new Hashtable<Integer, Marker>());
	
	/**
	 * Registers the model loaders and adds models to the application.
	 * 
	 * @see ImplantActivity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {		
		registerModelLoaders();
		addModels();
		
		super.onCreate(savedInstanceState);
	}

	/**
	 * Creates the model rendering view for this application.
	 * 
	 * @return An object of type MarkerSurface, that contains all markers registered with the application.
	 */
	@Override
	protected View setRenderingView() {
		return new MarkerSurface(this, mMarkers);
	}

	/**
	 * Returns a new CameraPreviewHandler that should analyze the captured frames.
	 * 
	 * @see ImplantActivity#setCameraPreviewHandler()
	 * 
	 * @return A MarkerCameraPreview object that analyzes frames and activates registered markers.
	 */
	@Override
	protected CameraPreviewHandler setCameraPreviewHandler() {
		return new MarkerCameraPreview((MarkerSurface) renderingView, getCameraFilename());
	}
	
	/**
	 * Adds a new marker relation to the platform.
	 * 
	 * @param id The marker identification.
	 * @param marker The marker metadata to associate with the identifier.
	 */
	public final void addMarker(int id, Marker marker) {
		mMarkers.put(id, marker);
	}
	
	/**
	 * Adds a marker based on a given template.
	 * 
	 * @param patternFile The template file.
	 * @param marker The marker metadata to associate with the identifier.
	 */
	public final void addMarker(String patternFile, Marker marker) {
		addMarker(pt.com.gmv.lab.implant.jni.ARToolkitPlus.addPattern(patternFile), marker);
	}
	
	/**
	 * Removes a marker from the application.
	 * 
	 * @param id The marker to remove
	 * @return <code>True</code> if a marker is successfully removed.
	 */
	public final boolean removeMarker(int id) {
		return mMarkers.remove(id) != null;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.ar.ImplantActivity#setActivityLayout()
	 */
	@Override
	protected void setActivityLayout() {
		final FrameLayout frame = new FrameLayout(this);

        frame.addView(renderingView);
        frame.addView(cameraPreview);
        
        setContentView(frame);		
	}
	
	/**
	 * Override to return the camera configuration filename.
	 * 
	 * @return The path to the configuration file.
	 */
	protected abstract String getCameraFilename();
	

	/**
	 * Override to register all ModelLoaders to use with the application.
	 * 
	 * @see ModelLoaders#registerLoader(Class)
	 * @see ModelLoaders#registerLoader(Class, ModelLoader)
	 */
	protected abstract void registerModelLoaders();
	
	/**
	 * Override to add models to the application
	 * 
	 * @see #addMarker(int, Marker)
	 * @see #addMarker(String, Marker)
	 */
	protected abstract void addModels();
}
