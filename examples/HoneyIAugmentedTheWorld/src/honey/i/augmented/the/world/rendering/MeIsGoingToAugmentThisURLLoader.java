/**
 * 
 */
package honey.i.augmented.the.world.rendering;

import java.util.LinkedList;

import android.content.Context;
import pt.com.gmv.lab.implant.exceptions.ModelLoadingException;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;
import pt.com.gmv.lab.implant.interfaces.rendering.marker.ModelLoader;

/**
 * @author fabio
 *
 */
public class MeIsGoingToAugmentThisURLLoader implements ModelLoader {
	
	LinkedList<MeIsGoingToAugmentThisURLModel> mModels;

	private Context mContext;
	
	public MeIsGoingToAugmentThisURLLoader(Context context) {
		mContext = context;
		mModels = new LinkedList<MeIsGoingToAugmentThisURLModel>();
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.ModelLoader#load(java.lang.String)
	 */
	@Override
	public Model load(String location) throws ModelLoadingException {
		MeIsGoingToAugmentThisURLModel model;
		mModels.add(model = new MeIsGoingToAugmentThisURLModel(mContext, location));
		return model;
	}

	public void resetPages() {
		for(MeIsGoingToAugmentThisURLModel model : mModels)
			model.resetStatus();
	}
}
