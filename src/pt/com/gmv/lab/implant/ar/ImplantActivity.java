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
package pt.com.gmv.lab.implant.ar;

import pt.com.gmv.lab.implant.R;
import pt.com.gmv.lab.implant.helpers.ActivityHelpers;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;
import pt.com.gmv.lab.implant.rendering.surfaces.CameraPreviewSurface;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;

/**
 * The base activity in the platform.
 * 
 * @author fao
 * @since 1
 */
public abstract class ImplantActivity extends Activity {

	/**
	 * The OpenGL ES surface where the model rendering is done.
	 */
	protected View 		renderingView;
	
	/**
	 * A surface where the camera preview is captured.
	 */
	protected CameraPreviewSurface 	cameraPreview;
	
	/**
	 * An Android WakeLock to keep the device active.
	 */
	private WakeLock mWakeLock;
	
	/**
	 * Sets the rendering view, the camera preview surface and handler and the device WakeLock.
	 * 
	 * Called when the application is starting.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		renderingView = setRenderingView();
		
		cameraPreview = new CameraPreviewSurface(this);
		cameraPreview.setPreviewHandler(setCameraPreviewHandler());
		
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getString(R.string.implant));

		setActivityHelpers();
		setActivityLayout();
	}	

	/**
	 * Called when an application resumes its activity.
	 * 
	 * Reaquires the WakeLock if it isn't held.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!mWakeLock.isHeld())
			mWakeLock.acquire();
	}

	/**
	 * When an application is going to be paused and put in hold.
	 * 
	 * Releases the device WakeLock if it is held.
	 */
	@Override
	protected void onPause() {
		if(mWakeLock.isHeld())
			mWakeLock.release();
		
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {		
		super.onDestroy();
	}
	
	/**
	 * Enables a series of auxiliary functions as setting no title bar on the application and
	 * executing it in fullscreen mode.
	 */
	protected void setActivityHelpers() {
		ActivityHelpers.disableScreenTurnOff(this);
		ActivityHelpers.setFullscreen(this);
		ActivityHelpers.setNoTitle(this);
	}
	
	/**
	 * Override this method to set the activity components layout.
	 */
	protected abstract void setActivityLayout();
	
	/**
	 * Override to set the camera preview handler for the activity.
	 */
	protected abstract CameraPreviewHandler setCameraPreviewHandler();

	/**
	 * Override to set the rendering view surface.
	 */
	protected abstract View setRenderingView();
}
