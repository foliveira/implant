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
package pt.com.gmv.lab.implant.ar.marker.toolkit;

import pt.com.gmv.lab.implant.ar.marker.toolkit.ARConstants.ImageProcessing;
import pt.com.gmv.lab.implant.ar.marker.toolkit.ARConstants.MarkerMode;
import pt.com.gmv.lab.implant.ar.marker.toolkit.ARConstants.PixelFormat;
import pt.com.gmv.lab.implant.ar.marker.toolkit.ARConstants.PoseEstimator;
import pt.com.gmv.lab.implant.ar.marker.toolkit.ARConstants.UndistortionMode;

/**
 * This class represents an implementation of the augmented reality toolkit operations.
 *
 * @author fao
 * @since 1
 * 
 */
public final class ARToolkitPlus {
	/**
	 * This empty array is returned when there's no detected markers in a image.
	 * 
	 * @since 1
	 */
	private final static int[] 	sEmptyMarkers = new int[0];
	
	/**
	 * An array that should contain markers when they're detected.
	 *  
	 * @since 1
	 */
	private int[] 				mDetectedMarkers = sEmptyMarkers;

	/**
	 * Creates a new toolkit instance with the given values.
	 * 
	 * @param width The image width.
	 * @param height The image height.
	 * @param maxPatterns The maximum number of patterns to detect.
	 * @param patternWidth A pattern width.
	 * @param patternHeight A pattern height.
	 * @param patternSamples The number of samples in a pattern.
	 * @param maxLoadPatterns The number of loaded patterns at a given time. Only used when doing multi tracking.
	 *  
	 * @since 1
	 */
	public ARToolkitPlus(final int width, 
							final int height, 
							final int maxPatterns, 
							final int patternWidth, 
							final int patternHeight, 
							final int patternSamples, 
							final int maxLoadPatterns) {
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setup(width, height, maxPatterns, patternWidth, patternHeight, patternSamples, maxLoadPatterns);
	}
	
	/**
	 * Initiates the toolkit by providing a file with camera calibration parameters and values for the clipping fields.
	 * 
	 * @param calibFile The path to the calibration file.
	 * @param nearClip The value of the near clipping field.
	 * @param farClip The value of the far clipping field.
	 * 
	 * @return <code>True</code> if the toolkit initiation is successful, <code>false</code> if not.
	 *  
	 * @since 1
	 */
	public final boolean init (final String calibFile, final float nearClip, final float farClip) {
		return init(calibFile, nearClip, farClip, MarkerMode.ID_BCH, 2.0f, 0.125f);
	}
	
	/**
	 * Initiates the toolkit by providing a file with camera calibration parameters, values for the clipping fields and marker type.
	 * 
	 * The default values are the RGB pixel format, the automatic threshold level of 150, the Standard
	 * undistortion mode, the RPP pose estimator and half resolution image processing. This are regarded
	 * as optimal parameters.
	 * 
	 * @param calibFile The path to the calibration file.
	 * @param nearClip The value of the near clipping field.
	 * @param farClip The value of the far clipping field.
	 * @param markerMode The marker type.
	 * @param patternWidth The pattern width
	 * @param borderWidth The pattern border width
	 * 
	 * @return <code>True</code> if the toolkit initiation is successful, <code>false</code> if not.
	 *  
	 * @since 1
	 */
	public final boolean init(final String calibFile, 
								final float nearClip, 
								final float farClip, 
								final int markerMode,
								final float patternWidth,
								final float borderWidth) {
		if(!pt.com.gmv.lab.implant.jni.ARToolkitPlus.init(calibFile, nearClip, farClip))
			return false;
		
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setPixelFormat(PixelFormat.RGB);
		
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setPatternWidth(patternWidth);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setBorderWidth(borderWidth);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setThreshold(150);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.activateAutoThreshold(true);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setUndistortionMode(UndistortionMode.STD);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setPoseEstimator(PoseEstimator.RPP);
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setImageProcessingMode(ImageProcessing.HALF_RES);
		
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.setMarkerMode(markerMode);
				
		return true;	
	}

	/**
	 * Detects markers on a given image.
	 * 
	 * Stores the detected markers ids in an array that can be retrieved with <code>getDetectedMarkers</code>
	 * 
	 * @param image The image to analyze.
	 * 
	 * @return The number of markers detected on the image.
	 *  
	 * @since 1
	 */
	public final synchronized int detectMarkers(final byte[] image) {
		int[] markers = pt.com.gmv.lab.implant.jni.ARToolkitPlus.detectMarkers(image);
		
		mDetectedMarkers = (markers == null) ? sEmptyMarkers : markers;
			
		return mDetectedMarkers.length;
	}
	
	/**
	 * Returns the resulting markers detected on an image.
	 * 
	 * @return An array with the identification of the detected markers or an empty array if there's no markers.
	 *  
	 * @since 1
	 */
	public final synchronized int[] getDetectedMarkers() {
		return mDetectedMarkers.clone();
	}
	
	/**
	 * Selects the best marker, based on the confidence with which it was detected.
	 * 
	 * @return The identification of the best marker based on the toolkit confidence of it.
	 *  
	 * @since 1
	 */
	public final synchronized int selectBestMarkerByConfidence() {
		return pt.com.gmv.lab.implant.jni.ARToolkitPlus.selectBestMarkerByCf();
	}
	
	/**
	 * Selects a marker by it's identification number.
	 * 
	 * @param id The marker identification number
	 *  
	 * @since 1
	 */
	public final synchronized void selectDetectedMarker(final int id) {
		pt.com.gmv.lab.implant.jni.ARToolkitPlus.selectDetectedMarker(id);
	}
	
	/**
	 * Returns the confidence of the current selected marker.
	 * 
	 * @return The selected marker confidence.
	 *  
	 * @since 1
	 */
	public final synchronized float getConfidence() {
		return pt.com.gmv.lab.implant.jni.ARToolkitPlus.getConfidence();
	}
	
	/**
	 * Returns the model view matrix for the current selected marker.
	 * 
	 * @return An array that can be used as a model view matrix.
	 *  
	 * @since 1
	 */
	public final synchronized float[] getModelViewMatrix() {
		return pt.com.gmv.lab.implant.jni.ARToolkitPlus.getModelViewMatrix();
	}
	
	/**
	 * Returns the projection matrix for the current selected marker.
	 * 
	 * @return An array that can be used as a projection matrix.
	 *  
	 * @since 1
	 */
	public final synchronized float[] getProjectionMatrix() {
		return pt.com.gmv.lab.implant.jni.ARToolkitPlus.getProjectionMatrix();
	}
	
	/**
	 * Adds a template pattern to the toolkit.
	 * 
	 * @param patternFile The file where the pattern is described.
	 * @return The pattern identifier.
	 */
	public final int addPatern(final String patternFile) {
		return pt.com.gmv.lab.implant.jni.ARToolkitPlus.addPattern(patternFile);
	}
}
