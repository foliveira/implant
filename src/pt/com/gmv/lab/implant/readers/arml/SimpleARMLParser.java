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

import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import android.util.Xml.Encoding;

import pt.com.gmv.lab.implant.exceptions.POILoadingException;
import pt.com.gmv.lab.implant.interfaces.readers.POIReader;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.readers.model.POIGroup;

/**
 * An implementation of a ARML parser that parses the simple specification.
 * 
 * Based on an Android XML parser.
 * 
 * @author fao
 * @since 2
 */
public class SimpleARMLParser implements POIReader {
	private static final String ROOT = "kml";
	private static final String XMLNS = "http://www.opengis.net/kml/2.2";

	private static final String DOCUMENT = "Document";
	private static final String PLACEMARK = "Placemark";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String POINT = "Point";
	private static final String COORDINATES = "coordinates";
	
	/**
	 * The POI that is created with each iteration of the parser
	 */
	POI poi;
	
	/**
	 * The name and description read from the stream of the current parsed POI
	 */
	String name, desc;
	
	/**
	 * The latitude, longitude and altitude of the current parsed POI
	 */
	double lat, lon, alt;

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.readers.POIReader#read(java.io.InputStream)
	 */
	@Override
	public List<POIGroup> read(InputStream stream) throws POILoadingException {
		final POIGroup currentGroup = new POIGroup("default", "");
		final LinkedList<POIGroup> poiList = new LinkedList<POIGroup>();

		final RootElement root = new RootElement(XMLNS, ROOT);
		final Element document = root.getChild(XMLNS, DOCUMENT);
		final Element placemark = document.getChild(XMLNS, PLACEMARK);

		placemark.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				poi = new POI(name, desc, lat, lon, alt);
				currentGroup.addPointOfInterest(poi);
			}
		});
		placemark.getChild(XMLNS, NAME).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				name = body;
			}
		});
		placemark.getChild(XMLNS, DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				desc = body;
			}
		});
		placemark.getChild(XMLNS, POINT).getChild(XMLNS, COORDINATES).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				String[] coords = body.split(","); //Yeah, it's a bird...
				lon = Double.parseDouble(coords[0]);
				lat = Double.parseDouble(coords[1]);
				alt = Double.parseDouble(coords[2]);
			}
		});
		document.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				poiList.add(currentGroup);
			}
		});

		try {
			Xml.parse(stream, Encoding.UTF_16, root.getContentHandler());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		poiList.add(currentGroup);

		return poiList;
	}

}
