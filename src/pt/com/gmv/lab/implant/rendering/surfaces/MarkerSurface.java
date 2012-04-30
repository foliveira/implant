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

import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pt.com.gmv.lab.implant.ar.marker.util.Marker;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * The model rendering surface based on Open GL ES.
 * It's main purpose is to draw models on top of markers and activating them when they're detected.
 * 
 * @author fao
 * @since 1
 */
public final class MarkerSurface extends GLSurfaceView {
	/**
	 * An associative container that holds marker identification and marker metadata relation.
	 */
	private final Map<Integer, Marker> mMarkers;
	
	/**
	 * Creates a rendering surface for models.
	 * 
	 * @param context The application Context.
	 * @param markers The markers associative container.
	 */
	public MarkerSurface(final Context context, final Map<Integer, Marker> markers) {
		super(context);
		
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		/*
		 * Set this surface renderer and set it to only render on request
		 */
		setRenderer(new ModelRenderer());
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		mMarkers = markers;
	}
	
	/**
	 * Activates a marker, setting its matrices and declaring it visible, so it can be drawn by the renderer.
	 * 
	 * @param marker The marker identifier.
	 * @param modelView The model view matrix.
	 * @param projection The projection matrix.
	 * 
	 * @return <code>False</code> in there's no marker with the given identifier. <code>True</code> otherwise.
	 */
	public final boolean activateMarker(final int marker, final float[] modelView, final float[] projection) {
		Marker m;
		
		if((m = mMarkers.get(marker)) == null)
			return false;
	
		m.setModelView(modelView);
		m.setProjection(projection);
		
		m.setVisible(true);
		
		return true;
	}

	/**
	 * The model renderer, in charge of rendering models that are visible.
	 */
	protected final class ModelRenderer implements Renderer {

		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
		 */
		@Override
		public final void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			synchronized (mMarkers) {
				for(Marker marker : mMarkers.values()) {
					if(marker.isVisible()) {
						marker.draw(gl);
						marker.setVisible(false);
					}
				}	
			}
		}

		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
		 */
		@Override
		public final void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			
			/*
			 * Set our projection matrix. This doesn't have to be done
			 * each time we draw, but usually a new projection needs to
			 * be set when the viewport is resized.
			 */
			
			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}
	
		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
		 */
		@Override
		public final void onSurfaceCreated(GL10 gl, EGLConfig config) {
	        /*
	         * By default, OpenGL enables features that improve quality
	         * but reduce performance. One might want to tweak that
	         * especially on software renderer.
	         */
	        gl.glDisable(GL10.GL_DITHER);

	        /*
	         * Some one-time OpenGL initialization can be made here
	         * probably based on features of this particular context
	         */
	         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

             gl.glClearColor(0, 0, 0, 0);
	         gl.glEnable(GL10.GL_CULL_FACE);
	         gl.glShadeModel(GL10.GL_SMOOTH);
	         gl.glEnable(GL10.GL_DEPTH_TEST);
	         
	         synchronized (mMarkers) {
		         for(Marker marker : mMarkers.values())
		        	 marker.init(gl);	
			}   
		}
	}
}
