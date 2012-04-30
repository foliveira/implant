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
package pt.com.gmv.lab.implant.helpers;

/**
 * An helper class for Point operations.
 * 
 * @author fao
 * @since 2
 */
/*
 * Why god, oh why, doesn't Java has extensions methods?!
 */
public class PointHelpers {
	
	/**
	 * Returns an angle based on 4 points
	 * 
	 * @param centerX The x center point
	 * @param centerY The y center point
	 * @param postX The other x point
	 * @param postY The other y point
	 * 
	 * @return The angle in degrees
	 */
	public static float getAngle(float centerX, float centerY, float postX, float postY) {
		final float tmpX = postX - centerX;
		final float tmpY = postY - centerY;
		final float angle = (float) Math.toDegrees(Math.acos(tmpX / ((float) Math.sqrt(tmpX * tmpX + tmpY * tmpY))));

		return (tmpY < 0) ? angle * -1 : angle;
	}
	
	/**
	 * Detects if a point is inside a rectangle
	 * 
	 * @param pointX The rectangle x origin point
	 * @param pointY The rectangle y origin point
	 * @param rectX The rectangle outer x point
	 * @param rectY The rectangle outer y point
	 * @param rectW The rectangle width
	 * @param rectH The rectangle height
	 * 
	 * @return <code>True</code> if the point is inside the rectangle.
	 */
	public static boolean pointInside(float pointX, float pointY, float rectX, float rectY, float rectW, float rectH) {
		return pointX > rectX 
				&& pointX < rectX + rectW 
				&& pointY > rectY 
				&& pointY < rectY + rectH;
	}

	/**
	 * Returns a string with the distance formatted.
	 * 
	 * @param meters The distance in meters
	 * 
	 * @return A String with the correct format.
	 */
	public static String getDistance(float meters) {		
		return (meters < 1000) ? String.format("%.0fm",  meters) : 
									String.format("%.0fkm", meters / 1000f);
	}
}
