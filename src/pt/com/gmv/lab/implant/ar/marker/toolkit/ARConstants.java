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

/**
 * Numeric constants for use with the native augmented reality toolkit.
 * 
 * Each static class maps to a enum construct in the toolkit.
 * 
 * @author fao
 * @since 1
 *
 */
public abstract class ARConstants {
	
	/**
	 * Constants for the image pixel format
	 *
	 * @since 1
	 */
	public abstract static class PixelFormat {
	    public static final int ABGR = 1;
	    public static final int BGRA = 2;
	    public static final int BGR = 3;
	    public static final int RGBA = 4;
	    public static final int RGB = 5;
	    public static final int RGB565 = 6;
	    public static final int LUM = 7;
	}
	
	/**
	 * Constants for the image undistortion mode
	 *
	 * @since 1
	 */
	public abstract static class UndistortionMode {
	    public static final int NONE = 0;
	    public static final int STD = 1;
	    public static final int LUT = 2;
	}
	
	/**
	 * Constants for the marker type
	 *
	 * @since 1
	 */
	public abstract static class MarkerMode {
	    public static final int TEMPLATE = 0;
	    public static final int ID_SIMPLE = 1;
	    public static final int ID_BCH = 2;
	}
	
	/**
	 * Constants for the image pose estimator
	 *
	 * @since 1
	 */
	public abstract static class PoseEstimator {
		public static final int ORIGINAL = 0;
		public static final int ORIGINAL_CONT = 1;
		public static final int RPP = 2;
	}
	
	/**
	 * Constants for the image processing size
	 *
	 * @since 1
	 */
	public abstract static class ImageProcessing {
		public static final int HALF_RES = 0;
		public static final int FULL_RES = 1;
	}
}
