/**
 * 
 */
package honey.i.augmented.the.world.rendering;

import javax.microedition.khronos.opengles.GL10;

import pt.com.gmv.lab.implant.interfaces.rendering.marker.Model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author fabio
 *
 */
public class MeIsGoingToAugmentThisURLModel implements Model {
	
	private Intent mIntent; 
	private boolean mIsLoaded;
	
	private String mLocationURL;
	private Context mContext;
	
	public MeIsGoingToAugmentThisURLModel(Context ctx, String location) {
		this.mContext = ctx;
		mLocationURL = location;
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#init(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void init(GL10 gl) {
		mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLocationURL)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.interfaces.rendering.Model#draw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		if(!mIsLoaded) {
			mContext.startActivity(mIntent);
			mIsLoaded = true;
		}
	}
	
	public void resetStatus() {
		mIsLoaded = false;
	}
}
