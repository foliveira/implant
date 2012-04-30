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

import java.nio.ShortBuffer;
import java.util.ArrayList;


/**
 * A model face, made up by a collection of vertices. 
 * 
 * @author fao
 * @since 1
 */
public final class Face {
	/**
	 * The vertices collection.
	 */
	private final ArrayList<Vertex> mVertices;
	
	/**
	 * Creates a new face from a collection of vertices.
	 * 
	 * @param vertices A variable length list of vertices.
	 */
	public Face(final Vertex ... vertices) {
		mVertices = new ArrayList<Vertex>(4);
		
		for(Vertex vertex : vertices) 
			mVertices.add(vertex);
		
		mVertices.trimToSize();
	}
	
	/**
	 * Copies the various vertex indices, that make a face, to a native buffer.
	 * 
	 * @param mIndexBuffer The native buffer to be used by Open GL ES.
	 */
	public final void copyToBuffer(final ShortBuffer mIndexBuffer) {
		int last = mVertices.size() - 1;
		
		 Vertex top = mVertices.get(0);
		 Vertex mid = mVertices.get(1);
		 Vertex bottom = mVertices.get(last);
		 
         for (int i = 1; i < last; mid = mVertices.get(++i)) {  
        	 mIndexBuffer.put(top.getIndex());
        	 mIndexBuffer.put(mid.getIndex());
        	 mIndexBuffer.put(bottom.getIndex());
             
             top = mid;
         }
	}

	/**
	 * Sets the color of the face, by setting the various vertices colors.
	 * 
	 * @param color The face color.
	 */
	public final void setColor(final Color color) {
		for(Vertex vertex : mVertices)
			vertex.setColor(color);
	}
	
	/**
	 * Returns the total number of indices on the face.
	 * 
	 * @return The number of indices on the face.
	 */
	public final int getTotalIndices() {
		return (mVertices.size() - 2) * 3 ;
	}
}
