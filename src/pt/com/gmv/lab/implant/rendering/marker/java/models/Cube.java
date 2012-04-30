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
package pt.com.gmv.lab.implant.rendering.marker.java.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;

/**
 * A Cube done in Java.
 * 
 * It can be loaded with the Java Loader
 * 
 * @author fao
 * @since 1
 * @see pt.com.gmv.lab.implant.rendering.marker.java.JavaLoader
 */
public final class Cube implements Model {
	
	/**
	 * Represented as 65536 (base-10) since we're using fixed arithmetic with OpenGL ES
	 */
	private final int 				one 		= 	0x10000;
	
	/**
	 * The cube vertices, laid out in pairs of three, representing the (x, y, z) coordinates.
	 */
	private final int 				vertices[] 	=	{ 	-one, -one, -one,
										            	one, -one, -one,
										            	one,  one, -one,
										            	-one,  one, -one,
										            	-one, -one,  one,
										            	one, -one,  one,
										            	one,  one,  one,
										            	-one,  one,  one		};

	/**
	 * The color for each vertex.
	 */
	private final int 				colors[] 	= 	{	0,    0,    0,  one,
										            	one,    0,    0,  one,
											            one,  one,    0,  one,
											            0,  one,    0,  one,
											            0,    0,  one,  one,
											            one,    0,  one,  one,
											            one,  one,  one,  one,
											            0,  one,  one,  one		};

	/**
	 * The faces representation.
	 */
	private final byte 				indices[] 	= 	{ 	0, 4, 5,    0, 5, 1,
										            	1, 5, 6,    1, 6, 2,
											            2, 6, 7,    2, 7, 3,
											            3, 7, 4,    3, 4, 0,
											            4, 7, 6,    4, 6, 5,
											            3, 0, 1,    3, 1, 2		};
	
    /**
     * A native buffer for vertices.
     */
    private transient IntBuffer   	mVertexBuffer;
    /**
     * A native buffer for colors.
     */
    private transient IntBuffer   	mColorBuffer;
    /**
     * A native buffer for faces.
     */
    private transient ByteBuffer 	mIndexBuffer;
    
	/**
	 * Whether the cube is going to be rendered in wireframe or not.
	 */
	private final boolean mWireframe;

	/**
	 * The number of instances built.
	 */
	private static int sInstances = 0;
	
    /**
     * Creates a new cube. If the current number of instances is odd the cube is displayed in wireframe.
     */
    public Cube() {
    	mWireframe = (sInstances++ & 1) != 0;
    }
    
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#draw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
    public final void draw(final GL10 gl)
    {
    	gl.glPushMatrix();
    	{
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glDrawElements((mWireframe ? GL10.GL_LINES : GL10.GL_TRIANGLES), 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    	}
    	gl.glPopMatrix();
    }
    
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#init(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public final void init(final GL10 gl) {

	
	    // Buffers to be passed to gl*Pointer() functions
	    // must be direct, i.e., they must be placed on the
	    // native heap where the garbage collector cannot
	    // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order
	
	    mVertexBuffer = (IntBuffer) ByteBuffer.allocateDirect(vertices.length*4)
								    			.order(ByteOrder.nativeOrder())
							    				.asIntBuffer()
							    				.put(vertices)
							    				.rewind();
	    mColorBuffer = (IntBuffer) ByteBuffer.allocateDirect(colors.length*4)
							    				.order(ByteOrder.nativeOrder())
							    				.asIntBuffer()
							    				.put(colors)
							    				.rewind();
	    mIndexBuffer = (ByteBuffer) ByteBuffer.allocateDirect(indices.length)
											    .put(indices)
											    .rewind();
	}
}