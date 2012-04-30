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
package pt.com.gmv.lab.implant.readers.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of POIs.
 * 
 * It is meant to hold POIs of the same category.
 * 
 * @author fao
 * @since 2
 */
public class POIGroup {
	/**
	 * The list of POIs that comprise the group
	 */
	private final LinkedList<POI> mPointsOfInterest;
	
	/**
	 * The group name 
	 */
	private final String mName;
	
	/**
	 * The group description 
	 */
	private final String mDescription;
	
	/**
	 * Creates a new group with a name and description
	 * 
	 * @param name The group name 
	 * @param desc The group description 
	 */
	public POIGroup(final String name, final String desc) {
		mPointsOfInterest = new LinkedList<POI>();
		
		mName = name;
		mDescription = desc;
	}
	
	/**
	 * Returns the group name
	 * 
	 * @return The name
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Returns the group description
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return mDescription;
	}
	
	/**
	 * Returns a collection of POIs that represent the group.
	 * 
	 * @return A read-only copy of the POIs that make the group
	 */
	public List<POI> getPointsOfInterest() {
		return Collections.unmodifiableList(mPointsOfInterest);
	}
	
	/**
	 * Adds a POI to the group
	 * 
	 * If the POI doesn't have an identifier, one is automatically assigned.
	 * 
	 * @param poi The POI to add
	 */
	public void addPointOfInterest(final POI poi) {
		mPointsOfInterest.add(poi);
		
		if(poi.getIdentifier() == 0)
			poi.setIdentifier(mPointsOfInterest.size());
	}
}
