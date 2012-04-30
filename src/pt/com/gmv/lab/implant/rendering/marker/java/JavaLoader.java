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
package pt.com.gmv.lab.implant.rendering.marker.java;

import pt.com.gmv.lab.implant.exceptions.ModelLoadingException;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.ModelLoader;

/**
 * Loads models represented by Java classes, by using introspection to create new instances.
 * 
 * @author fao
 * @since 1
 */
public class JavaLoader implements ModelLoader {

	/**
	 * Loads a model on the given location, using introspection
	 * 
	 * @throws ModelLoadingException In case there isn't an appropriate class or there's
	 * a problem in its instantiation.
	 * 
	 * @return A loaded model.
	 *
	 */
	@Override
	public final Model load(final String location) throws ModelLoadingException {
		final Model model;
		
		try {
			model = Class.forName(location).asSubclass(Model.class).newInstance();
		} catch (ClassNotFoundException e) {
			throw new ModelLoadingException(location, e);
		} catch (IllegalAccessException e) {
			throw new ModelLoadingException(location, e);
		} catch (InstantiationException e) {
			throw new ModelLoadingException(location, e);
		}

		return model;
	}
}
