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

import java.text.BreakIterator;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.location.Location;
import android.util.FloatMath;
import android.view.MotionEvent;

import pt.com.gmv.lab.implant.helpers.PointHelpers;
import pt.com.gmv.lab.implant.interfaces.rendering.poi.POIModel;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.rendering.poi.util.ProjectionCamera;
import pt.com.gmv.lab.implant.rendering.poi.util.WorldVector;

/**
 * A simple POI to be exhibited on screen with an accompanying text representation
 * 
 * @author fao
 * @since 2
 */
public class SimplePOI implements POIModel {

	/**
	 * The underlying POI object with all the information
	 */
	private final POI mPointOfInterest;
	
	/**
	 * The vector with screen coordinates for the POI
	 */
	private final WorldVector mScreenLocation;
	/**
	 * The POI position in the world, based on the device position
	 */
	private final WorldVector mPOIPosition;	
	/**
	 * The text position on screen
	 */
	private final WorldVector mTextPosition;
	
	/**
	 * The text block representation
	 */
	private TextBlock mTextBlock;

	/**
	 * Whether the POI is visible
	 */
	private boolean mIsVisible;
	
	/**
	 * Whether the POI is centered on screen
	 */
	private boolean mIsLookingAt;
	
	/**
	 * Max height for the text
	 */
	private float mMaxHeight;

	/**
	 * Creates a POI based on a existing point of interest.
	 * 
	 * @param poi The point of interest object containing all information
	 */
	public SimplePOI(POI poi) {
		mPointOfInterest = poi;

		mScreenLocation = new WorldVector(0, 0, 0);

		mPOIPosition = new WorldVector();
		mTextPosition = new WorldVector();
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#getScreenLocation()
	 */
	@Override
	public WorldVector getScreenLocation() {
		return mScreenLocation;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#getPOILocation()
	 */
	@Override
	public WorldVector getPOILocation() {
		return mPOIPosition;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#getPOIInfo()
	 */
	@Override
	public POI getPOIInfo() {
		return mPointOfInterest;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return mIsVisible;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#isAtCenter()
	 */
	@Override
	public boolean isAtCenter() {
		return mIsLookingAt;
	}

	/**
	 * Draws the model on screen.
	 * 
	 * The text block is created if there isn't one and the model is drawn on screen, taking
	 * into account the screen coordinates.
	 * 
	 * @see #drawPOI(Canvas, Paint, float)
	 */
	@Override
	public void draw(Canvas canvas, Paint paint) {
		mMaxHeight = FloatMath.floor((canvas.getHeight() / 10f) + .5f) + 1;

		if(mTextBlock == null)
			mTextBlock = new TextBlock(mPointOfInterest.getName(), 160, -paint.ascent() / 2);

		if(mIsVisible) {
			final float currentAngle = PointHelpers.getAngle(mPOIPosition.getX(), mPOIPosition.getY(), mTextPosition.getX(), mTextPosition.getY());
			
			drawPOI(canvas, paint);

			paint.setStrokeWidth(1f);
			canvas.save();
			{
				canvas.translate(mTextPosition.getX() - mTextBlock.getWidth() / 2 + mTextBlock.getWidth() / 2, mTextPosition.getY() + mMaxHeight + mTextBlock.getHeight() / 2);
				canvas.rotate(currentAngle + 90);
				canvas.translate(-(mTextBlock.getWidth() / 2), -(mTextBlock.getHeight() / 2));
				mTextBlock.draw(canvas, paint);
			}
			canvas.restore();
		}
	}
	
	/**
	 * Draws the model with a fixed max height for the text.
	 *  
	 * @param canvas The canvas where to draw to 
	 * @param paint The paint object to use in the drawing
	 */
	private void drawPOI(Canvas canvas, Paint paint) {
		drawPOI(canvas, paint, mMaxHeight);
	}
	
	/**
	 * Override to customize the drawing routine of a POI
	 * 
	 * This implementation draws a hollow circle in dark red color, with variable
	 * height, depending on the fact that the model is centered in the screen
	 * 
	 * @param canvas The canvas where to draw the POI
	 * @param paint The paint object used in the drawing
	 * @param maxHeight Text and model maxheight
	 */
	protected void drawPOI(Canvas canvas, Paint paint, float maxHeight) {
		paint.setColor(Color.argb(0xdd, 0xee, 0x11, 0x11));
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(maxHeight / (mIsLookingAt ? 5f : 10f));
		
		canvas.drawCircle(mPOIPosition.getX(), mPOIPosition.getY(), maxHeight / 1.5f, paint);
	}

	/**
	 * Updated the screen location based on the current device location.
	 * 
	 * The update is done taking into account the current screen location.
	 * 
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#update(android.location.Location)
	 */
	@Override
	public void update(Location current) {
		POI.convertLocationToVector(current, mPointOfInterest, mScreenLocation);
	}

	/**
	 * Performs a set of transformations to the position vectors.
	 * 
	 * The rotation matrix is applied to the screen location vector.
	 * 
	 * With a call to this method, the model gets positioned on screen, according to the device orientation.
	 * 
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#performWorldTransformations(pt.com.gmv.lab.implant.rendering.poi.util.ProjectionCamera)
	 */
	@Override
	public void performWorldTransformations(ProjectionCamera mCamera) {
		final WorldVector wv1 = new WorldVector(0, 0, 0);
		final WorldVector wv2 = new WorldVector(0, 1, 0);
		final WorldVector wv3 = new WorldVector();

		wv1.add(mScreenLocation);
		wv2.add(mScreenLocation);
		wv1.product(mCamera.getRotation());
		wv2.product(mCamera.getRotation());
		
		mCamera.projectPoint(wv1, wv3, 0, 0);
		mPOIPosition.set(wv3);
		mCamera.projectPoint(wv2, wv3, 0, 0);
		mTextPosition.set(wv3);

		mIsVisible = (mPOIPosition.getZ() < -1f);
		mIsLookingAt = PointHelpers.pointInside(mPOIPosition.getX(), mPOIPosition.getY(), (mCamera.getWidth() / 2) - 20, 0, 40, mCamera.getHeight());
	}

	/**
	 * Stale implementation that always returns <code>true</code>	 * 
	 * 
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#onClick(android.content.Context, android.view.MotionEvent)
	 */
	@Override
	public boolean onClick(Context context, MotionEvent me) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#isClickValid(float, float)
	 */
	@Override
	public boolean isClickValid(float x, float y) {
		final float pX = mPOIPosition.getX();
		final float pY = mPOIPosition.getY();
		
		return PointHelpers.pointInside(x, y, pX - mMaxHeight, pY - mMaxHeight, pX + mMaxHeight, pY + mMaxHeight);
	}
	
	/**
	 * A class representing a text block for the model
	 */
	public class TextBlock {
		/**
		 * The text
		 */
		private final String mText;
		/**
		 * The max width for the text banner
		 */
		private final float mMaxWidth;

		/**
		 * The lines of text to show
		 */
		private String[] mLines;
		/**
		 * The width of each line
		 */
		private float[] mLineWidths;

		/**
		 * The total text area width
		 */
		private float mAreaWidth;
		/**
		 * The total text area height
		 */
		private float mAreaHeight;
		/**
		 * The current line height
		 */
		private float mLineHeight;

		/**
		 * The block width
		 */
		private float mWidth;
		/**
		 * The block height
		 */
		private float mHeight;

		/**
		 * Whether the block was initiated
		 */
		private boolean mIsInited;
		/**
		 * Padding value
		 */
		private float mPad;


		/**
		 * Creates a new block with a given text and width aswell as a padding value
		 * 
		 * @param txt The text to show
		 * @param maxWidth Maximum width for the block
		 * @param pad The padding value
		 */
		public TextBlock(String txt, float maxWidth, float pad) {
			mText = txt;
			mMaxWidth = maxWidth;
			mPad = pad;
		}

		/**
		 * Draws a box containing the text
		 * 
		 * @param canvas The canvas to where to write
		 * @param paint The paint object to use
		 */
		public void draw(Canvas canvas, Paint paint) {
			if(!mIsInited)
				init(canvas, paint);

			paint.setStyle(Style.FILL);
			paint.setColor(Color.argb(100, 0xcc, 0xcc, 0xcc));

			canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), 5, 5, paint);

			paint.setTextSize(14);
			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			
			for (int i = 0; i < mLines.length; i++) {
				canvas.drawText(mLines[i], mPad, mPad + mLineHeight * i + (-paint.ascent()), paint);
			}
		}

		/**
		 * Returns the block width
		 * 
		 * @return The width
		 */
		public float getWidth() {
			return mWidth;
		}

		/**
		 * Returns the block height
		 * 
		 * @return The height
		 */
		public float getHeight() {
			return mHeight;
		}

		/**
		 * Initiates the text block values.
		 * 
		 * Reads every line and breaks into readable chunks to show on screen.
		 * 
		 * @param canvas The canvas where the block is going to be drawn
		 * @param p The paint object used on the painting
		 */
		private void init(Canvas canvas, Paint p) {
			mAreaWidth = mMaxWidth - mPad * 2;
			mLineHeight = (-p.ascent()) + p.descent();

			final ArrayList<String> lineList = new ArrayList<String>();
			final BreakIterator boundary = BreakIterator.getWordInstance();
			boundary.setText(mText);

			int start = boundary.first();
			int end = boundary.next();
			int prevEnd = start;
			while (end != BreakIterator.DONE) {
				String line = mText.substring(start, end);
				String prevLine = mText.substring(start, prevEnd);
				float lineWidth = p.measureText(line);

				if (lineWidth > mAreaWidth) {
					lineList.add(prevLine);

					start = prevEnd;
				}

				prevEnd = end;
				end = boundary.next();				
			}
			
			String line = mText.substring(start, prevEnd);
			lineList.add(line);

			mLines = new String[lineList.size()];
			mLineWidths = new float[lineList.size()];
			lineList.toArray(mLines);

			float maxLineWidth = 0;
			for (int i = 0; i < mLines.length; i++) {
				mLineWidths[i] = p.measureText(mLines[i]);
				if (maxLineWidth < mLineWidths[i])
					maxLineWidth = mLineWidths[i];
			}
			mAreaWidth = maxLineWidth;
			mAreaHeight = mLineHeight * mLines.length;

			mWidth = mAreaWidth + mPad * 2;
			mHeight = mAreaHeight + mPad * 2;

			mIsInited = true;
		}
	}
}
