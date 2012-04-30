package pt.pharmacias;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import pt.com.gmv.lab.implant.ar.poi.POIActivity;
import pt.com.gmv.lab.implant.exceptions.POILoadingException;
import pt.com.gmv.lab.implant.helpers.FileHelpers;
import pt.com.gmv.lab.implant.interfaces.camera.CameraPreviewHandler;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.readers.model.POIGroup;
import pt.com.gmv.lab.implant.rendering.poi.SimplePOI;
import pt.pharmacias.readers.PharmaciasReader;
import pt.pharmacias.rendering.Pharmacia;
import android.os.Bundle;
import android.view.View;

public class PharmaciasActivity extends POIActivity {
	protected final float mRadarRange = 2f;
	
	private Collection<POIGroup> mPois = new LinkedList<POIGroup>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try {
    		String filename = FileHelpers.moveFileToApplicationFilesystem(getFilesDir(), getString(R.string.arml_file), getAssets());
			mPois.addAll(getPOIFromStream(new PharmaciasReader(), new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (POILoadingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addPOIModels(createPOIModels());
		
        super.onCreate(savedInstanceState);
    }

	@Override
	protected CameraPreviewHandler setCameraPreviewHandler() {
		return null; //Do nothing with captured frames.
	}

	@Override
	protected Collection<? extends SimplePOI> createPOIModels() {
		return createPOIModels(mPois);
	}
	
	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.ar.poi.POIActivity#setRenderingView()
	 */
	@Override
	protected View setRenderingView() {
		return setRenderingView(2f);
	}

	private Collection<? extends SimplePOI> createPOIModels(Collection<POIGroup> pgs) {
		LinkedList<Pharmacia> pois = new LinkedList<Pharmacia>();
		
		for(POIGroup pg : pgs) { 
             for(POI poi : pg.getPointsOfInterest()) { 
            	 pois.add(new Pharmacia(poi)); 
             } 
		}
		
		return pois;
	}
}