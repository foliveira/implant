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

import pt.com.gmv.lab.implant.ar.marker.toolkit.ARToolkitPlus;
import pt.com.gmv.lab.implant.helpers.GraphicHelpers;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;
import pt.com.gmv.lab.implant.jni.ImageProcessing;
import pt.com.gmv.lab.implant.rendering.surfaces.MarkerSurface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

/**
 *
 * A class that handles frames received from the camera and proceeds to detect them with the toolkit.
 *
 * @author fao
 * @since 1
 * @see CameraPreviewHandler
 */
public final class MarkerCameraPreview implements CameraPreviewHandler {
	/**
	 * The model rendering object.
	 */
	private final MarkerSurface			mRenderSurface;
	
	/**
	 * The capture analyzer object
	 */
	private final CaptureAnalyzer 		mCaptureAnalyzer;
	
	/**
	 * The camera calibration filename
	 */
	private final String 				mCameraFilename;
	
	/**
	 * The augmented reality toolkit object
	 */
	private ARToolkitPlus 				mToolkit;
	
	/**
	 * The capture timer object.
	 */
	private CaptureTimer 				mCaptureTimer;	
	
	/**
	 * The camera device from where the capture is done.
	 */
	private Camera 						mCamera;

	/**
	 * The pixel height from the preview frame.
	 */
	private int 						mPreviewHeight = 160;
	
	/**
	 * The pixel width from the preview frame.
	 */
	private int 						mPreviewWidth = 240;
	
	/**
	 * The texture size in pixels.
	 */
	private int 						mTextureSize = 256;
	
	/**
	 * The number of pixels on the image.
	 */
	private int 						mImagePixels = mPreviewHeight * mPreviewWidth;

	/**
	 * Creates a new camera preview handler that can detect markers on a captured frame.
	 * 
	 * @param renderSurface The render surface in which model rendering occurs.
	 * @param cameraFilename The camera filename to 
	 */
	public MarkerCameraPreview(final MarkerSurface renderSurface, String cameraFilename) {
		mCameraFilename = cameraFilename;
		mRenderSurface = renderSurface;
		
		mCaptureAnalyzer = new CaptureAnalyzer();
	}

	/**
	 * Initiates the camera preview handler, setting the preview sizes according to the sizes
	 * reported by the device camera.
	 * 
	 * Initiates a new ARToolkitPlus with the calculated values and a CaptureTimer object that fires
	 * every 100ms.
	 * 
	 * @param camera The camera representation
	 */
	@Override
	public final void init(final Camera camera) {
		mCamera = camera;
		
		Parameters params = camera.getParameters();

		Size previewSz = params.getPreviewSize();
		mPreviewWidth = previewSz.width;
		mPreviewHeight = previewSz.height;
		mTextureSize = GraphicHelpers.nextPowerOf2(Math.max(mPreviewWidth, mPreviewHeight));
		mImagePixels = mPreviewWidth * mPreviewHeight;

		mToolkit = new ARToolkitPlus(mPreviewWidth, mPreviewHeight, 8, 6, 6, 6, 0);
		mToolkit.init(mCameraFilename, 1.0f, 1000.0f);
		
		mCaptureTimer = new CaptureTimer(10);
		mCaptureTimer.start();
	}


	/**
	 * Interrupts the CaptureTimer thread, halting image analysis.
	 */
	public final void close() {
		mCaptureTimer.interrupt();
	}
	
	/**
	 * Receives a frame from the device Camera and delivers it to the CaptureAnalyzer object.
	 * 
	 * @param image The captured frame
	 * @param camera An object representing the device Camera
	 */
	@Override
	public final void onPreviewFrame(final byte[] image, final Camera camera) {
		if(image == null || camera == null)
			return;

		mCaptureAnalyzer.detectFrame(image);
	}
	
	/**
	 * Analyzes a captured frame and sends it to the augmented reality toolkit to detect markers in it.
	 */
	protected final class CaptureAnalyzer {
		/**
		 * The frame to analyze in frame detection.
		 */
		private final byte[] mFrame = new byte[mImagePixels * 3];
		
		/**
		 * Detects markers on a image frame, converted from YUV420 to RGB, and activates them until the next frame is grabbed.
		 * It also requests the renderer surface to call the render routine and prompts the capture timer to re-register
		 * 
		 * @param image
		 */
		public final void detectFrame(final byte[] image) {
			
			ImageProcessing.yuv420sp2rgb(image, mPreviewWidth, mPreviewHeight, mTextureSize, mFrame);
			
			if(mToolkit.detectMarkers(mFrame) > 0) {
				int[] markers = mToolkit.getDetectedMarkers();
				
				for(int id : markers) {
					mToolkit.selectDetectedMarker(id);
					mRenderSurface.activateMarker(id, mToolkit.getModelViewMatrix(), mToolkit.getProjectionMatrix());
				}
			}
			
			//This is called everytime there's a call to this method, since there's a need to
			//clear the rendering surface when there's no markers being detected.
			mRenderSurface.requestRender();
			
			synchronized (mCaptureTimer) {
				mCaptureTimer.notify();
			}
		}
	}
	
	/**
	 * This class represents a timer that registers a new preview callback on a given time interval.
	 */
	protected final class CaptureTimer extends Thread {
		/**
		 * The time in milliseconds to wait to re-register the preview callback.
		 */
		private int mWaitTime;
		
		/**
		 * Creates a new timer that registers a new preview callback in a given time or when notified from the
		 * current preview callback.
		 * 
		 * @param fps The number of fps to achieve.
		 */
		public CaptureTimer(final int fps) {
			setName("CaptureTimer");
			setDaemon(true);
			
			mWaitTime = 1000/fps;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public final void run() {
			synchronized (mCaptureTimer) {
				while(true) {
					try {
						wait(mWaitTime);
					} catch (InterruptedException e) { break; }
					
					mCamera.setOneShotPreviewCallback(MarkerCameraPreview.this);
				}
			}
		}
		
	}
}