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

import pt.com.gmv.lab.implant.helpers.GraphicHelpers;

/**
 * A class representing a vertex color.
 * 
 * The values are in the range 0.0-1.0.
 * 
 * @author fao
 * @since 1
 */
public final class Color {
	/**
	 * The red component.
	 */
	public final int Red;
	/**
	 * The green component.
	 */
	public final int Green;
	/**
	 * The blue component.
	 */
	public final int Blue;
	
	/**
	 * The alpha component
	 */
	public final int Alpha;
	

	/**
	 * Creates a new color with 4 components.
	 * The values are in the range 0-255.
	 * 
	 * @param red The color red component.
	 * @param green The color green component.
	 * @param blue The color blue component.
	 * @param alpha The color alpha component.
	 */
	public Color(final int red, final int green, final int blue, final int alpha) {
		Red = GraphicHelpers.toFixed(red/255);
		Green = GraphicHelpers.toFixed(green/255);
		Blue = GraphicHelpers.toFixed(blue/255);
		
		Alpha = GraphicHelpers.toFixed(alpha/255);
	}
	
	/**
	 * Creates a new color with 4 components.
	 * The values are in the range 0-255.
	 * 
	 * @param red The color red component.
	 * @param green The color green component.
	 * @param blue The color blue component.
	 */
	public Color(final int red, final int green, final int blue) {
		this(red, green, blue, 255);
	}
}
