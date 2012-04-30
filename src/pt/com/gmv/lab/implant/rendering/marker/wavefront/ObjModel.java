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
package pt.com.gmv.lab.implant.rendering.marker.wavefront;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.rendering.marker.wavefront.parts.Color;
import pt.com.gmv.lab.implant.rendering.marker.wavefront.parts.Face;
import pt.com.gmv.lab.implant.rendering.marker.wavefront.parts.Vertex;

/**
 * A model representation loaded from a Wavefront OBJ file.
 * 
 * @author fao
 * @since 1
 */
public final class ObjModel implements Model {    	
	
	/**
	 * A collection of vertices.
	 */
	private final ArrayList<Vertex> mVertices;
	/**
	 * A collection of faces.
	 */
	private final ArrayList<Face> mFaces;
	
	/**
	 * A native buffer that holds all vertex colors.
	 */
	private transient IntBuffer mColorBuffer;
	/**
	 * A native buffer that holds all vertices.
	 */
	private transient IntBuffer mVertexBuffer;
	/**
	 * A native buffer that holds all vertex indices.
	 */
	private transient ShortBuffer mIndexBuffer;
	
	/**
	 * The total number of indices.
	 */
	private int mIndexCount;
	
	/**
	 * Creates a new model based on a wavefront obj representation.
	 */
	public ObjModel() {
		mVertices = new ArrayList<Vertex>();
		mFaces = new ArrayList<Face>();
	}
	
	/**
	 * Adds a face to the model.
	 * 
	 * @param face The face to add.
	 */
	public final void addFace(final Face face) {
		mFaces.add(face);
	}
	
	/**
	 * Sets a face color, based on the its index.
	 * 
	 * @param faceIdx The face index.
	 * @param color The face color.
	 */
	public final void setFaceColor(final int faceIdx, final Color color) {
		mFaces.get(faceIdx).setColor(color);
	}

	/**
	 * Returns a vertex from the face.
	 * 
	 * @param i The vertex index in the face.
	 * @return The vertex at position <code>i</code> or <code>null</code> if there's not a vertex at that position.
	 */
	public final Vertex getVertex(final int i) {
		if(mVertices.size() < i)
			return null;
		
		return mVertices.get(i);
	}

	/**
	 * Adds a vertex to the face.
	 * 
	 * @param x The x position on 3D space.
	 * @param y The y position on 3D space.
	 * @param z The z position on 3D space.
	 */
	public final void addVertex(final float x, final float y, final float z) {
		mVertices.add(new Vertex(mVertices.size(), x, y, z));
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#init(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public final void init(final GL10 gl) {
		mVertices.trimToSize();
		mFaces.trimToSize();
		mIndexCount = 0;
		
		for(Face face : mFaces)
			mIndexCount += face.getTotalIndices();

		mVertexBuffer 	= ByteBuffer.allocateDirect(mVertices.size() * 4 * 3)
									.order(ByteOrder.nativeOrder())
									.asIntBuffer();
		mColorBuffer 	= ByteBuffer.allocateDirect(mVertices.size() * 4 * 4)
									.order(ByteOrder.nativeOrder())
									.asIntBuffer();
		mIndexBuffer 	= ByteBuffer.allocateDirect(mIndexCount * 2)
									.order(ByteOrder.nativeOrder())
									.asShortBuffer();
		
		for(Vertex vertex : mVertices)
			vertex.copyToBuffers(mVertexBuffer, mColorBuffer);
		
		for(Face face : mFaces)
			face.copyToBuffer(mIndexBuffer);
		
		mVertexBuffer.rewind();
		mColorBuffer.rewind();
		mIndexBuffer.rewind();
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#draw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public final void draw(final GL10 gl) {   
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
    	gl.glPushMatrix();
    	{
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glDrawElements(GL10.GL_LINES, 36, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    	}
    	gl.glPopMatrix();
	}
}
