/**
 * 
 */
package pt.pharmacias.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import pt.com.gmv.lab.implant.exceptions.POILoadingException;
import pt.com.gmv.lab.implant.interfaces.readers.POIReader;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.readers.model.POIGroup;

/**
 * @author fano
 *
 */
public class PharmaciasReader implements POIReader {

	private static final String DOCUMENT = "Document";
	private static final String PLACEMARK = "Placemark";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String COORDINATES = "coordinates";
	private static final String STATUS = "status";
	
	@Override
	public List<POIGroup> read(InputStream stream) throws POILoadingException {
		final POIGroup currentGroup = new POIGroup("default", "");
		final LinkedList<POIGroup> poiList = new LinkedList<POIGroup>();

		POI poi;
		
		String name = null;
		String desc = null;
		String status = null;
		double lon = 0;
		double lat = 0;
		double alt = 0;

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
							String[] coords = parser.nextText().split(",");
							
							lon = Double.parseDouble(coords[0]);
							lat = Double.parseDouble(coords[1]);
							alt = Double.parseDouble(coords[2]);
						} else if(tag.equalsIgnoreCase(STATUS)) {
							status = parser.nextText();
						}
	
						break;
					case XmlPullParser.END_TAG:
						tag = parser.getName();
	
						if(tag.equalsIgnoreCase(PLACEMARK)) {
							poi = new POI(name, desc, lat, lon, alt);
							poi.addMetadata("status", status);
							
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
			Log.e("implant", e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e("implant", e.getLocalizedMessage());
		}

		return poiList;
	}
}
