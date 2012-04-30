/**
 * 
 */
package pt.pharmacias.rendering;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.view.MotionEvent;
import pt.com.gmv.lab.implant.readers.model.POI;
import pt.com.gmv.lab.implant.rendering.poi.SimplePOI;
import pt.pharmacias.R;

/**
 * @author fabio
 *
 */
public class Pharmacia extends SimplePOI {

	private AlertDialog mDialog;
	
	public Pharmacia(POI poi) {		
		super(poi);
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POI#drawPOI(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	protected void drawPOI(Canvas canvas, Paint paint, float maxHeight) {
		String status = getPOIInfo().getMetadata("status");
		
		if(status.equalsIgnoreCase("PERMANENTE")) {
			paint.setColor(Color.BLUE);
		} else {
			paint.setColor(Color.GREEN);
		}

		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(maxHeight / 10f);
		
		canvas.drawCircle(getPOILocation().getX(), getPOILocation().getY(), maxHeight / 1.5f, paint);
	}

	/* (non-Javadoc)
	 * @see pt.com.gmv.lab.implant.rendering.poi.POIModel#onClick(android.content.Context, android.view.MotionEvent)
	 */
	@Override
	public boolean onClick(final Context context, MotionEvent me) {
		if(mDialog == null) {
			final AlertDialog.Builder b = new Builder(context);
			final String[] desc = getPOIInfo().getDescription().split("\\s-\\s");
			
			b.setIcon(R.drawable.farmacia);
			b.setTitle(getPOIInfo().getName());
			b.setMessage(String.format("Morada: %s (%s)\nTelefone: %s", desc[0], desc[1], desc[2]));
			b.setPositiveButton("Telefonar", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent it= new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+desc[2])).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(it);
				}
			});
			b.setNegativeButton("Fechar", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			mDialog = b.create();
		}
		
		mDialog.show();
		
		return true;
	}
}
