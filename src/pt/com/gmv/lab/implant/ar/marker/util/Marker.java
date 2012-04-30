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
package pt.com.gmv.lab.implant.ar.marker.util;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import pt.com.gmv.lab.implant.exceptions.ModelLoadingException;
import pt.com.gmv.lab.implant.helpers.GraphicHelpers;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.ModelLoader;
import pt.com.gmv.lab.implant.rendering.marker.ModelLoaders;

/**
 * A class that represents a marker and holds all information needed to show the corresponding model on screen
 *
 * @author fao
 * @since 1
 * 
 */
public class Marker {

	/**
	 * The marker confidence value.
	 * 
	 * @since 1
	 */
	private float 			mConfidence;
	
	/**
	 * Whether the marker is visible in the image or not.
	 * 
	 *  @since 1
	 */
	private boolean 		mVisible;	
	
	/**
	 * The model view matrix wrapped in a native buffer
	 * 
	 * @since 1
	 */
	private FloatBuffer 	mModelView;
	
	/**
	 * The projection matrix wrapped in a native buffer
	 * 
	 * @since 1 
	 */
	private FloatBuffer 	mProjection;
	
	/**
	 * The model loader type that loads the associated model
	 * 
	 * @since 1
	 */
	private final Class<? extends ModelLoader> 	mType;
	
	/**
	 * The model location
	 * 
	 * @since 1
	 */
	private final String	mLocation;
	
	/**
	 * Whether the model was already loaded or not
	 * 
	 * @since 1
	 */
	private boolean 		mIsLoaded;
	
	/**
	 * The model object corresponding to this marker
	 * 
	 * @since 1
	 */
	private Model 			mModel;

	/**
	 * Creates a new marker object with a given model type and location.
	 * 
	 * @param type The model type
	 * @param location The model location
	 * 
	 * @since 1
	 */
	public Marker(Class<? extends ModelLoader> type, String location) {
		mType = type;
		mLocation = location;
	}

	/**
	 * Sets the marker confidence value
	 * 
	 * @param confidence This marker confidence value
	 * 
	 * @since 1
	 */
	public void setConfidence(float confidence) {
		mConfidence = confidence;
	}
	
	/**
	 * Returns the marker confidence.
	 * The confidence is show in a decimal representation, between 0.0 and 1.0
	 * 
	 * @return This marker confidence
	 * 
	 * @since 1
	 */
	public float getConfidence() {
		return mConfidence;
	}
	
	/**
	 * Sets the model view matrix for this marker, wrapping it in a native buffer.
	 * 
	 * @param matrix The model view matrix
	 * 
	 * @since 1
	 */
	public void setModelView(float[] matrix) {
		mModelView = GraphicHelpers.makeFloatBuffer(matrix);
	}
	
	/**
	 * Returns the native buffer representing the model view matrix.
	 * 
	 * @return A read-only copy of the model view buffer.
	 * 
	 * @since 1
	 */
	public FloatBuffer getModelView() {
		return mModelView.asReadOnlyBuffer();
	}
	
	/**
	 * Sets the projection matrix for this marker, wrapping it in a native buffer.
	 * 
	 * @param matrix The projection matrix
	 * 
	 * @since 1
	 */
	public void setProjection(float[] matrix) {
		mProjection = GraphicHelpers.makeFloatBuffer(matrix);
	}
	
	/**
	 * Returns the native buffer representing the projection matrix.
	 * 
	 * @return A read-only copy of the projection buffer.
	 * 
	 * @since 1
	 */
	public FloatBuffer getProjection() {
		return mProjection.asReadOnlyBuffer();
	}
	
	/**
	 * Sets the marker visibility.
	 * 
	 * @param enable If <code>true</code> sets the marker visible.
	 * 
	 * @since 1
	 */
	public void setVisible(boolean enable) {
		mVisible = enable;
	}
	
	/**
	 * Returns whether the marker is visible.
	 * 
	 * @return <code>True</code> if the marker is visible, <code>false</code> if not.
	 * 
	 * @since 1
	 */
	public boolean isVisible() {
		return mVisible;
	}
	
	/**
	 * Draws the model assigned to the marker.
	 * It uses the matrices calculated by the toolkit to correctly position the model on screen.
	 * 
	 * Uses OpenGL ES calls to draw a figure on the screen, which doesn't mean that the underlaying model
	 * must use OpenGL in any way.
	 * 
	 * @param gl OpenGL ES context.
	 * 
	 * @since 1
	 */
	public void draw(GL10 gl) {		
    	gl.glMatrixMode(GL10.GL_PROJECTION);
    	gl.glLoadIdentity();
    	gl.glLoadMatrixf(mProjection);
    	
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glLoadMatrixf(mModelView);
     
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
		mModel.draw(gl);
	}

	/**
	 * Initializes the model and loads it if it isn't already loaded.
	 * 
	 * If there's some problem loading the model, it will be discarded.
	 * 
	 * @param gl OpenGL ES context.
	 * 
	 * @since 1
	 */
	public void init(GL10 gl) {		
		if(!mIsLoaded)
			ensureModel();
		
		mModel.init(gl);
	}
	
	/**
	 * Loads the model using a registered model loader.
	 * 
	 * Discards a model that can't be loaded
	 * 
	 * @since 1
	 */
	private void ensureModel() {
		try {
			mModel = ModelLoaders.loadModel(mType, mLocation);
		} catch (ModelLoadingException e) {
			Log.e(mType.toString(), "Cannot load model. Forfeiting.");
		} finally {
			mIsLoaded = true;
		}
	}
}
