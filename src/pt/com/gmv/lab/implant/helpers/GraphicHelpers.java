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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * An helper class for graphics in the appication.
 * 
 * @author fao
 * @since 1
 */
/*
 * Why god, oh why, doesn't Java has extensions methods?!
 */
public final class GraphicHelpers {
	
	/**
	 * Creates a direct float buffer from a pre-existing array.
	 * 
	 * @param arr The array from where to get the values to the buffer.
	 * @return A native float buffer with the correct values.
	 */
	public final static FloatBuffer makeFloatBuffer(final float[] arr) {
		return (FloatBuffer) ByteBuffer.allocateDirect(arr.length*4)
										.order(ByteOrder.nativeOrder())
										.asFloatBuffer()
										.put(arr)
										.rewind();
	}
	
	/**
	 * Gets the next power of two from the given value.
	 * 
	 * @param value The value to check.
	 * @return The value of the next power of two.
	 */
	public final static int nextPowerOf2(final int value) {
		int v = value - 1;
		
		v |= v >> 1;
		v |= v >> 2;
		v |= v >> 4;
		v |= v >> 8;
		v |= v >> 16;
		
		return ++v;
	}
	
    /**
     * Converts a floating value to its fixed counterpart.
     * The conversion is done by multiplying the value by 0x10000
     * 
     * @param value The floating point value
     * 
     * @return The fixed point value
     */
    public static final int toFixed(float value) {
    	return (int) (value * 65536F);
    }
}
