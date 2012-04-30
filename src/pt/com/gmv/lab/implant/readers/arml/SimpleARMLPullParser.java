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
package pt.com.gmv.lab.implant.readers.arml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import pt.com.gmv.lab.implant.exceptions.POILoadingException;
import pt.com.gmv.lab.implant.interfaces.readers.POIReader;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.readers.model.POIGroup;

/**
 * An implementation of a ARML parser that parses the simple specification.
 * 
 * Based on an Android XML pull parser.
 * 
 * @author fao
 * @since 2
 */
public class SimpleARMLPullParser implements POIReader {

	private static final String DOCUMENT = "Document";
	private static final String PLACEMARK = "Placemark";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String COORDINATES = "coordinates";

	/**
	 * The POI that is created with each iteration of the parser
	 */
	private POI poi;
	
	/**
	 * The name and description read from the stream of the current parsed POI
	 */
	private String name, desc;
	
	/**
	 * The latitude, longitude and altitude of the current parsed POI
	 */
	private double lat, lon, alt;

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.readers.POIParser#read(java.io.InputStream)
	 */
	@Override
	public List<POIGroup> read(InputStream stream) throws POILoadingException {
		final POIGroup currentGroup = new POIGroup("default", "");
		final LinkedList<POIGroup> poiList = new LinkedList<POIGroup>();

		XmlPullParser parser = Xml.newPullParser();
		
		try {
			parser.setInput(stream, "iso-8859-1");

			int eventType = parser.getEventType();
			boolean done = false;

			while(eventType != XmlPullParser.END_DOCUMENT && !done) {
				String tag = null;

				switch (eventType) {
					case XmlPullParser.START_TAG:
						tag = parser.getName();
	
						if(tag.equalsIgnoreCase(NAME)) {
							name = parser.nextText();
						} else if(tag.equalsIgnoreCase(DESCRIPTION)) {
							desc = parser.nextText();
						} else if(tag.equalsIgnoreCase(COORDINATES)) {
							String[] coords = parser.nextText().split(","); //Yeah, it's a bird...
							lon = Double.parseDouble(coords[0]);
							lat = Double.parseDouble(coords[1]);
							alt = Double.parseDouble(coords[2]);
						}
	
						break;
					case XmlPullParser.END_TAG:
						tag = parser.getName();
	
						if(tag.equalsIgnoreCase(PLACEMARK)) {
							poi = new POI(name, desc, lat, lon, alt);
							currentGroup.addPointOfInterest(poi);
						} else if(tag.equalsIgnoreCase(DOCUMENT)) {
							done = true;
						}
						break;
					default:
						break;
				}

				eventType = parser.next();
			}		

			poiList.add(currentGroup);
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return poiList;
	}

}
