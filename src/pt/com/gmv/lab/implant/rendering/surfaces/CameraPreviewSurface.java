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

import java.io.IOException;

import pt.com.gmv.lab.implant.R;
import pt.com.gmv.lab.implant.exceptions.runtime.ImplantException;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * Surface where the preview from the camera is shown.
 * 
 * @author fao
 * @since 1
 */
public final class CameraPreviewSurface extends SurfaceView implements Callback {
	
	/**
	 * The camera preview handler, where markers are detected.
	 */
	private CameraPreviewHandler 	mCameraPreviewer;
	/**
	 * The holder of this surface.
	 */
	private SurfaceHolder 			mSurfaceHolder;
	/**
	 * The camera device from where the capture is done.
	 */
	private Camera 					mCamera;

	/**
	 * Whether the surface is created.
	 */
	private boolean 				mIsSurfaceCreated;

	/**
	 * Creates a new surface, registering a callback for the surface so we can get hold of the surface creation and destruction.
	 * @param context
	 */
	public CameraPreviewSurface(final Context context) {
		super(context);
		
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	
	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	@Override
	public final void surfaceCreated(final SurfaceHolder holder) {
			/** NOP **/
	}

	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	@Override
	public final void surfaceChanged(final SurfaceHolder holder, 
										final int format, 
										final int width, 
										final int height) {
    	mSurfaceHolder = holder;
    	mIsSurfaceCreated = true;
    	
    	openCamera(width, height);
    	startPreview();		
	}

	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	@Override
	public final void surfaceDestroyed(final SurfaceHolder holder) {
		stopPreview();
		releaseCamera();

        mIsSurfaceCreated = false;
        mSurfaceHolder = null;
	}

	/**
	 * Sets the camera preview handler, for the class.
	 * 
	 * @param previewer
	 */
	public final void setPreviewHandler(final CameraPreviewHandler previewer) {
		mCameraPreviewer = previewer;
	}
    
    /**
     * Opens a connection to the camera device, sets the preview parameters and registers the surface has the preview display.
     * 
     * @throws ImplantException In the case the preview surface can't be set.
     */
    private final void openCamera(int width, int height) {		
		if(mCamera != null)  
			return;
		
		mCamera = Camera.open();
		
		Parameters param = mCamera.getParameters();
		param.setPreviewSize(width / 2, height / 2);
		param.setPreviewFormat(PixelFormat.YCbCr_420_SP);
		
		mCamera.setParameters(param);
		
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
		} catch (IOException e) {
			throw new ImplantException(getResources().getString(R.string.camera_preview_error), e);
		}
	}
	
	/**
	 * Releases the previous opened camera, taking care of all resources associated.
	 */
    private final void releaseCamera() {
        if (mCamera == null) 
        	return;
		
        mCamera.release();
        
    	mCamera = null;
	}

	/**
	 * Registers the preview callback and starts the preview.
	 * In this case, the preview callback has a one call lifetime and it's re-registered in a separate thread.
	 */
    private final void startPreview() {
		if(!mIsSurfaceCreated || (mCamera == null)) 
			return;

		if(mCameraPreviewer != null) {
			mCameraPreviewer.init(mCamera);
			mCamera.setOneShotPreviewCallback(mCameraPreviewer);
		}
		
		mCamera.startPreview();
	}
	
	/**
	 * Stops the preview from the camera, unregisters the callback and closes it.
	 */
    private final void stopPreview() {
		if(mCamera == null) 
			return;
		
		mCamera.stopPreview();
		mCamera.setPreviewCallback(null);
		
		if(mCameraPreviewer != null)
			mCameraPreviewer.close();
	}
}
