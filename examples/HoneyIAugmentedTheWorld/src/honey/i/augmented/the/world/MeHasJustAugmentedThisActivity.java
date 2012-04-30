package honey.i.augmented.the.world;

import honey.i.augmented.the.world.rendering.MeIsGoingToAugmentThisURLLoader;

import java.io.IOException;

import pt.com.gmv.lab.implant.ar.marker.MarkerDetectionActivity;
import pt.com.gmv.lab.implant.ar.marker.util.Marker;
import pt.com.gmv.lab.implant.exceptions.runtime.ImplantException;
import pt.com.gmv.lab.implant.helpers.FileHelpers;
import pt.com.gmv.lab.implant.rendering.marker.ModelLoaders;
import pt.com.gmv.lab.implant.rendering.marker.java.JavaLoader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Rick Moranis is still retired. He didn't want to participate in this application,
 * which is unfortunate, since he put quite a show in Spaceballs.
 */
public class MeHasJustAugmentedThisActivity extends MarkerDetectionActivity {
	private static final int RESET_MENUITEM = 1;
	private static final int EXIT_MENUITEM = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
	protected final String getCameraFilename() {
		try {
			return FileHelpers.moveFileToApplicationFilesystem(getFilesDir(), getString(R.string.camera_file), getResources().getAssets());
		} catch (IOException e) {
			String cameraError = getString(R.string.camera_file_error);
			Log.e(getString(R.string.implant), cameraError);
			throw new ImplantException(cameraError);
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, RESET_MENUITEM, 0, "Reset state").setIcon(R.drawable.reset);
		menu.add(2, EXIT_MENUITEM, 0, "Exit").setIcon(R.drawable.exit);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case RESET_MENUITEM:
				((MeIsGoingToAugmentThisURLLoader)ModelLoaders.getLoader(MeIsGoingToAugmentThisURLLoader.class)).resetPages();
				return true;
			case EXIT_MENUITEM:
				finish();
				return true;
			default:
				return false;
		}
	}

	@Override
	protected void registerModelLoaders() {
		ModelLoaders.registerLoader(MeIsGoingToAugmentThisURLLoader.class, new MeIsGoingToAugmentThisURLLoader(getApplicationContext()));
		
		try {
			ModelLoaders.registerLoader(JavaLoader.class);
		} catch (IllegalAccessException e) {
			Log.e(getString(R.string.implant), "Can not access loader");
		} catch (InstantiationException e) {
			Log.e(getString(R.string.implant), "Can not instantiate loader");
		}
	}

	@Override
	protected void addModels() {
    	//addMarker(1105, new Marker(MeIsGoingToAugmentThisURLLoader.class, "http://www.cc.isel.ipl.pt/"));
    	addMarker(1105, new Marker(JavaLoader.class, "pt.com.gmv.lab.implant.rendering.marker.java.models.Cube"));
	}
}