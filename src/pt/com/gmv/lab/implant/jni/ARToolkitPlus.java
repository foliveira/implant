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
package pt.com.gmv.lab.implant.jni;

/**
 * A class that defines all the interfaces to the native ARToolkitPlus library.
 * 
 * @author fao
 * @since 1
 */
public abstract class ARToolkitPlus {
	/**
	 * Loads the native library into the application memory
	 */
	static {
		System.loadLibrary("arplus");
	}

	/**
	 * Creates a new marker tracker with the given parameters.
	 * 
	 * @param width The width of the image to analyze in px
	 * @param height The height of the image to analyze in px
	 * @param maxPatterns The maximum number of patterns that can be analyzed in a camera image.
	 * @param pWidth The pattern image width (must be 6 for binary markers)
	 * @param pHeight The pattern image height (must be 6 for binary markers)
	 * @param pSamples The maximum resolution at which a pattern is sampled from the camera image
	 * @param maxLoadPatterns The maximum number of pattern files that can be loaded.
	 */
	public static native void setup(int width, int height, int maxPatterns, int pWidth, int pHeight, int pSamples, int maxLoadPatterns);
	
	/**
	 * Loads a camera calibration file and stores data internally
	 * 
	 * @param calibFile The camera calibration file
	 * @param nearClip The near clipping value
	 * @param farClip The far clipping value
	 * 
	 * @return True if loading the camera calibration file was succesful.
	 */
	public static native boolean init(String calibFile, float nearClip, float farClip);
	
	/**
	 * Changes the resolution of the camera after the camerafile was already loaded
	 * 
	 * @param width The new camera width
	 * @param height The new camera height
	 */
	public static native void changeCameraSize(int width, int height);
	
	/**
	 * Sets the pixel format of the camera image.
	 * 
	 * @param format The image pixel format to set 
	 * @return True if the setter is succesful
	 */
	public static native boolean setPixelFormat(int format);
	
	/**
	 *  Set to true to try loading camera undistortion table from a cache file
     *  On slow platforms (e.g. Smartphone) creation of the undistortion lookup-table
     *  can take quite a while. Consequently caching will speedup the start phase.
     *  If set to true and no cache file could be found a new one will be created.
     *  The cache file will get the same name as the camera file with the added extension '.LUT'
     *  
	 * @param value If true uses a LUT cache file.
	 */
	public static native void setLoadUndistortionLUT(boolean value);
	
	/**
	 * Changes the Pose Estimation Algorithm
	 * 
	 * @param mode The mode to set
	 */
	public static native void setPoseEstimator(int mode);
	
	/**
     *  Activates the complensation of brightness falloff in the corners of the camera image.
     *  The threshold value will stay exactly the same at the center but will deviate near to 
     *  the border. All values specify a difference, not absolute values!
     *  nCorners .
     *  
     *  All values between these 9 points (center, 4 corners, left, right, top, bottom) will
     *  be interpolated.
     *  
	 * @param nEnable True to enable compensation
	 * @param nCorners Defines the falloff a all four corners
	 * @param nLeftRight Defines the falloff at the half y-position at the left and right side of the image
	 * @param nTopBottom defines the falloff at the half x-position at the top and bottom side of the image
	 */
	public static native void activateVignettingCompensation(boolean nEnable, int nCorners, int nLeftRight, int nTopBottom);
	
	/**
     * Sets the width and height of the patterns in OpenGL units
     * 
	 * @param width The width dimension
	 */
	public static native void setPatternWidth(float width);
	
	/**
     * Sets a new relative border width.
     *
	 * @param width The border width
	 */
	public static native void setBorderWidth(float width);
	
	/**
	 * Sets the threshold value that is used for black/white conversion
	 * 
	 * @param value The value to set
	 */
	public static native void setThreshold(int value);
	
	/**
	 * Returns the current threshold value.
	 * @return The current threshold
	 */
	public static native int getThreshold();
	
	/**
	 * Turns automatic threshold calculation on/off
	 */
	public static native void activateAutoThreshold(boolean enable);
	
	/**
	 * Returns true if automatic threshold detection is enabled
	 * @return True if automatic threshold is activated
	 */
	public static native boolean isAutoThresholdActivated();
	
	/**
	 *  Sets the number of times the threshold is randomized in case no marker was visible.
	 *  
     *  Autothreshold requires a visible marker to estime the optimal thresholding value. If
     *  no marker is visible ARToolKitPlus randomizes the thresholding value until a marker is
     *  found. This function sets the number of times ARToolKitPlus will randomize the threshold
     *  value and research for a marker per calc() invokation until it gives up.
     *  
     *  A value of 2 means that ARToolKitPlus will analyze the image a second time with an other treshold value
     *  if it does not find a marker the first time. Each unsuccessful try uses less processing power
     *  than a single full successful position estimation.
     *  
	 * @param retries The number of retries (Minimum: 1, Default: 2)
	 */
	public static native void setNumAutoThresholdRetries(int retries);
	
	/**
	 * Changes the undistortion mode
	 * 
	 * @param mode The mode to set
	 */
	public static native void setUndistortionMode(int mode);
	
	/**
	 * Activate the usage of id-based markers rather than template based markers
	 * 
	 *  Template markers are the classic marker type used in ARToolKit.
     *  Id-based markers directly encode the marker id in the image.
     *  Simple markers use 3-times redundancy to increase robustness, while
     *  BCH markers use an advanced CRC algorithm to detect and repair marker damages.
     *  In order to use id-based markers, the marker size has to be 6x6, 12x12 or 18x18.
	 * 
	 * @param mode The marker mode
	 */
	public static native void setMarkerMode(int mode);
	
	/**
	 *  Sets an image processing mode (half or full resolution)
     *  Half resolution is faster but less accurate. When using full resolution smaller 
     *  markers will be detected at a higher accuracy (or even detected at all).
     *  
	 * @param mode The mode to be set
	 */
	public static native void setImageProcessingMode(int mode);
	
	/**
	 * Returns the numbber of bits per pixel for the compiled-in pixel format
	 * 
	 * @return the number of bits per pixel
	 */
	public static native int getBitsPerPixel();
	
	/**
	 * Adds a pattern to ARToolKit
     * 
	 * @param filename The pattern filename
	 * @return The pattern id
	 */
	public static native int addPattern(String filename);
	
	/**
	 * Calculates the transformation matrix
	 * 
	 * @param image The image in RGBX format
	 * 
	 * @return Detected markers in image
	 */
	public static native int[] detectMarkers(byte[] image);
	
	/**
	 * Select the best marker based on confidence
	 * 
	 * @return The marker id
	 */
	public static native int selectBestMarkerByCf();
	
	/**
	 * Select one of the detected markers.
	 * 
	 * @param id The id of the selected marker
	 */
	public static native void selectDetectedMarker(int id);
	
	/**
	 * Returns the confidence value of the currently best detected marker.
	 * 
	 * @return The confidence value
	 */
	public static native float getConfidence();
	
	/**
	 * Returns an OpenGL-style ModelView transformation matrix
	 * 
	 * @return The model view transformation matrix
	 */
	public static native float[] getModelViewMatrix();
	
	/**
	 * Returns an OpenGL-style Projection matrix
	 * 
	 * @return The projection matrix
	 */
	public static native float[] getProjectionMatrix();
} 
