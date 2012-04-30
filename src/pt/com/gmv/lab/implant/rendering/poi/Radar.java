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
package pt.com.gmv.lab.implant.rendering.poi;

import java.util.Collection;

import pt.com.gmv.lab.implant.helpers.PointHelpers;
import pt.com.gmv.lab.implant.interfaces.rendering.poi.POIModel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.FloatMath;

/**
 * A radar widget, shown on screen with the device orientation and POIs location
 * 
 * @author fao
 * @since 2
 */
public class Radar {
	
	/**
	 * The radius in pixels
	 */
	private static final int sRadius = 40;
	/**
	 * An array with a double-copy of every bearing available, to be shown on screen.
	 */
	private static final String[] sBearingText = new String[] { "N", "NE", "NE", "E", 
																"E", "SE", "SE", "S", 
																"S", "SW", "SW", "W", 
																"W", "NW", "NW", "N" };
	
	/**
	 * A line that delimits the left side of the radar field of vision
	 */
	private final FieldOfVision mLeftLine;
	/**
	 * A line that delimits the right side of the radar field of vision
	 */
	private final FieldOfVision mRightLine;
	
	/**
	 * The real world radar range in meters
	 */
	private float mRange;
	
	/**
	 * Creates a new radar with a field of vision
	 * 
	 * @see FieldOfVision#DEFAULT_VIEW_ANGLE
	 */
	public Radar() {
		mLeftLine = new FieldOfVision(0, -sRadius);
		mRightLine = new FieldOfVision(0, -sRadius);
		
		/** 
		 * Sets the field of vision lines to be shown in the direction
		 * the user is facing and in a 45º angle
		 */
		mLeftLine.rotate(FieldOfVision.DEFAULT_VIEW_ANGLE / 2);
		mRightLine.rotate(-FieldOfVision.DEFAULT_VIEW_ANGLE / 2);
		mLeftLine.add(10 + sRadius, 20 + sRadius);
		mRightLine.add(10 + sRadius, 20 + sRadius);
	}


	/**	 
	 * Paints the radar on screen.
	 * 
	 * Performs rotations to keep it oriented with the device rotation and draws all visible POIs
	 * on screen in the radar.
	 * 
	 * @param canvas The canvas where to paint the radar
	 * @param paint The paint object used
	 * @param bearing The bearing to rotate and show on screen
 	 * @param pois The collection of pois to draw on the radar
	 */
	public void paint(Canvas canvas, Paint paint, float bearing, Collection<? extends POIModel> pois) {
		canvas.save();
		{
			/** 
			 * Performs canvas transformations 
			 * so the radar rotates as the bearing
			 * changes.
			 */
			canvas.translate(10 + getWidth() / 2, 20 + getHeight() / 2);
			canvas.rotate(-bearing);
			canvas.scale(1, 1);
			canvas.translate(-(getWidth() / 2), -(getHeight() / 2));
			
			/** Draw the radar */
			paint.setStyle(Style.FILL);
			paint.setColor(Color.argb(100, 0xcc, 0xcc, 0xcc));
			
			canvas.drawCircle(sRadius, sRadius, sRadius, paint);
			
			/** Draw the inner circles **/
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.argb(0x88, 0x22, 0x22, 0x22));
			
			canvas.drawCircle(sRadius, sRadius, sRadius, paint);
			canvas.drawCircle(sRadius, sRadius, sRadius / 1.5f, paint);
			canvas.drawCircle(sRadius, sRadius, sRadius / 2.5f, paint);
	
			/** Draw the POIs in the radar */
			float scale = mRange / sRadius;
			
			paint.setStyle(Style.FILL);
			paint.setColor(Color.RED);
			
			synchronized (pois) {
				for(POIModel poi : pois) {
					float x = poi.getScreenLocation().getX() / scale;
					float y = poi.getScreenLocation().getZ() / scale;
					float result = (x * x + y * y);
					
					if ((result != 0.0) && (result < sRadius * sRadius))
						canvas.drawCircle(x + sRadius, y + sRadius, 1.5f, paint);
				}
			}
		}
		canvas.restore();

		/** 
		 * Draw the radar extra information
		 * as field of view, bearing information
		 * and radar range
		 */
		drawRadarExtras(canvas, paint, bearing);
	}
	
	/**
	 * Returns the radar pixel width
	 * 
	 * @return The width
	 */
	public float getWidth() {
		return sRadius * 2;
	}

	/**
	 * Returns the radar pixel height
	 * 
	 * @return The height
	 */
	public float getHeight() {
		return sRadius * 2;
	}
	
	/**
	 * Returns the radar range in meters
	 * 
	 * @return The radar range in meters
	 */
	public float getRange() {
		return mRange;
	}
	
	/**
	 * Returns the radar range in kilometers
	 * 
	 * @return The radar range in kilometers
	 */
	public float getRangeInKilometers() {
		return mRange / 1000f;
	}
	
	/**
	 * Sets the radar range
	 * 
	 * The value must be in kilometers
	 * 
	 * @param rangeKm The radar range in kilometers
	 */
	public void setRange(float rangeKm) {
		mRange = rangeKm * 1000f;
	}
	
	/**
	 * Draws all extra text elements of the radar on screen.
	 * 
	 * @param canvas The canvas where to draw the radar
	 * @param paint The paint object used to draw the radar
	 * @param bearing The bearing of the device
	 */
	private void drawRadarExtras(Canvas canvas, Paint paint, float bearing) {
		final int bearingIdx = (int) (bearing / (360f / 16f));
		final String text = String.format("%.0fº %s", bearing, sBearingText[bearingIdx]);
		
		final int x = 10 + Radar.sRadius;
		final int y = 15;
		
		float width = paint.measureText(PointHelpers.getDistance(getRange())) + 8;
		float height = -paint.ascent() + paint.descent() + 4;
		
		/** Draw the range value **/
		paint.setTextSize(12);
		paint.setColor(Color.WHITE);
		canvas.drawText(PointHelpers.getDistance(getRange()), (4 + 15 + Radar.sRadius - width / 2), (2 + (-paint.ascent()) + 10 + Radar.sRadius * 2 - height / 2), paint);

		/** Draw the field of vision lines **/
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.argb(0xff, 0x22, 0x22, 0x22));
		mLeftLine.draw(canvas, paint);
		mRightLine.draw(canvas, paint);

		/** Draw the bearing box **/
		width = paint.measureText(text) + 8;
		height = -paint.ascent() + paint.descent() + 4;
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
	
		canvas.drawRoundRect(new RectF(x - width / 2, y - height / 2, x - width / 2 + width , y - height / 2 + height), 5, 5, paint);
		
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		canvas.drawRoundRect(new RectF(x - width / 2, y - height / 2, x - width / 2 + width , y - height / 2 + height), 5, 5, paint);
		
		paint.setColor(Color.WHITE);
		canvas.drawText(text, (4 + x - width / 2), (2 + (-paint.ascent()) + y - height / 2), paint);
	}
	
	/**
	 * A line that delimits the device field of vision on one side of the screen
	 */
	public static class FieldOfVision {		
		/**
		 * The default view angle of 45 degrees
		 */
		public static final float DEFAULT_VIEW_ANGLE = (float) Math.toRadians(45);
		
		/**
		 * The x axis position
		 */
		private float mX;
		/**
		 * The y axis position
		 */
		private float mY;
		
		/**
		 * Creates a new line on the origin
		 */
		public FieldOfVision() {
			set(0, 0);
		}
		
		/**
		 * Creates a new line with a set of coordinates
		 * 
		 * @param x The x axis coordinate
		 * @param y The y axis coordinate
		 */
		public FieldOfVision(float x, float y) {
			set(x, y);
		}
		
		/**
		 * Sets the two coordinates
		 * 
		 * @param x The x axis coordinate
		 * @param y The y axis coordinate
		 */
		public void set(float x, float y) {
			mX = x;
			mY = y;
		}
		
		/**
		 * Rotates the line by a number of degrees.
		 * 
		 * @param angle The angle to rotate by.
		 */
		public void rotate(float angle) {
			final float tmpX = FloatMath.cos(angle) * mX - FloatMath.sin(angle) * mY;
			final float tmpY = FloatMath.sin(angle) * mX + FloatMath.cos(angle) * mY;
			
			mX = tmpX;
			mY = tmpY;
		}
		
		/**
		 * Translates the line on each axis independently.
		 * 
		 * @param x The x axis value to move
		 * @param y The y axis value to move
		 */
		public void add(float x, float y) {
			mX += x;
			mY += y;
		}
		
		/**
		 * Draws a line on top of the radar
		 * 
		 * @param canvas The canvas where to draw
		 * @param p The paint object to use to paint
		 */
		public void draw(Canvas canvas, Paint p) {
			canvas.drawLine(mX, mY, Radar.sRadius + 10, Radar.sRadius + 20, p);
		}
	}
}
