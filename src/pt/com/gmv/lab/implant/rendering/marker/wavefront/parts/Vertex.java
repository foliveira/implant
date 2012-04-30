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
package pt.com.gmv.lab.implant.rendering.marker.wavefront.parts;

import java.nio.IntBuffer;

/**
 * A vector representation for a wavefront model.
 * 
 * @author fao
 * @since 1
 */
public final class Vertex {
	/**
	 * The vertex X point.
	 */
	private final float mXAxis;
    /**
     * The vertex Y point.
     */
    private final float mYAxis;
    /**
     * The vertex Z point.
     */
    private final float mZAxis;

    /**
     * The vertex index on the model.
     */
    private final short mIndex;
    
    /**
     * The vertex color.
     */
    private Color mColor;
    
    /**
     * Creates a vertex with all coordinates in 3D space and stuff. It also has an index. Yes it has!
     * 
     * @param index The vertex index.
     * @param x The x position.
     * @param y The y position.
     * @param z The z position.
     */
    public Vertex(final int index, final float x, final float y, final float z) {
    	mIndex = (short) index;
    	
    	mXAxis = x;
    	mYAxis = y;
    	mZAxis = z;
    }

	/**
	 * Returns this vertex index.
	 * 
     * @return The vertex index.
     */
    public final short getIndex() {
    	return mIndex;
    }
    
    /**
     * Sets the vertex color.
     * 
     * @param color The color object.
     */
    public final void setColor(final Color color) {
    	mColor = color;
    }
    
    /**
     * Copies vertex and color values to the corresponding native buffers, to be drawn by Open GL ES.
     * 
     * @param vertices The vertices native buffer.
     * @param colors The color native buffer.
     */
    public final void copyToBuffers(final IntBuffer vertices, final IntBuffer colors) {
    	vertices.put(toFixed(mXAxis));
    	vertices.put(toFixed(mYAxis));
    	vertices.put(toFixed(mZAxis));
    	
    	colors.put((int) ((mColor == null) ? (0x10000 | (System.nanoTime() % 0xFFFF)) : mColor.Red));
    	colors.put((int) ((mColor == null) ? (0x10000 | (System.currentTimeMillis() % 0xFFFF)) : mColor.Green));
    	colors.put((int) ((mColor == null) ? (0x10000 | (System.nanoTime() % 0xFFFF)) : mColor.Blue));
    	colors.put((int) ((mColor == null) ? 0x10000 : mColor.Alpha));
    }
    
    /**
     * Converts a float value to its fixed counterpart.
     * 
     * @param value The float value to convert.
     * @return A normalized value, converted from float to fixed.
     */
    private static final int toFixed(float value) {
    	return (int) (value * 65536F);
    }
}
