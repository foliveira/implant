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
package pt.com.gmv.lab.implant.rendering.marker;

import java.util.Hashtable;

import pt.com.gmv.lab.implant.exceptions.ModelLoadingException;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.ModelLoader;

/**
 * Manages the registration of model loaders.
 * 
 * Can load a model, with a given type, by resolving its association to a model loader.
 * 
 * @author fao
 * @since 1
 */
public final class ModelLoaders {
	/**
	 * A associative container that represents the association between a loader type and its instance.
	 */
	private static final Hashtable<Class<? extends ModelLoader>, ModelLoader> sModelLoaders = new Hashtable<Class<? extends ModelLoader>, ModelLoader>();
	
	/**
	 * Registers a loader in the platform, with its instance.
	 * Use this methods when the class has a constructor that accepts parameters and it needs to be used.
	 * 
	 * @param type The loader class type.
	 * @param loader The loader instance.
	 */
	public static final void registerLoader(final Class<? extends ModelLoader> type, final ModelLoader loader) {
		sModelLoaders.put(type, loader);
	}
	
	/**
	 * Registers a loader in the platform, creating a new instance, via introspection.
	 * Use this method when the only constructor is the parameterless one.
	 * 
	 * @param type The loader class type.
	 * @throws IllegalAccessException In case there isn't a visible default constructor visible.
	 * @throws InstantiationException In case the instance can't be created.
	 */
	public static final void registerLoader(final Class<? extends ModelLoader> type) throws IllegalAccessException, InstantiationException {
		registerLoader(type, type.newInstance());
	}
	
	/**
	 * Returns a registered loader.
	 * 
	 * @param type The loader class type.
	 * 
	 * @return The given model loader.
	 */
	public static final ModelLoader getLoader(final Class<? extends ModelLoader> type) {
		return sModelLoaders.get(type);
	}
	
	/**
	 * Does a direct model load from the correct model loader.
	 * 
	 * @param type The loader class type.
	 * @param location The model location.
	 * 
	 * @return A model object loaded from the given location.
	 * 
	 * @throws ModelLoadingException In case there's no model loader registered in the platform.
	 */
	public static final Model loadModel(final Class<? extends ModelLoader> type, final String location) throws ModelLoadingException {
		ModelLoader ml = getLoader(type);
		if(ml != null)
			return ml.load(location);
		
		throw new ModelLoadingException(type.toString());
	}
}