package jp.astra.cameramap;

import java.io.File;

import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.Util;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author jem
 */
public class ScreenshotPreviewView extends BaseActivity {

	private Bitmap bitmap;
	private ImageView image;
	private String filepath;
	private String pdfPath;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setBitmap();
		this.setContentView(R.layout.screenshot_preview_view);

		Button buttonSend = (Button)this.findViewById(R.id.send_button);
	    buttonSend.setOnClickListener(new Button.OnClickListener(){

	  		@Override
	  		public void onClick(View arg0) {
	  			
	  			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScreenshotPreviewView.this);
	  			alertDialogBuilder.setTitle("Send file");
	  			final String[] list = {"Gmail", "Bluetooth", "Others"};
	  			alertDialogBuilder.setItems(list, new DialogInterface.OnClickListener() {
	  	          @Override
	  	          public void onClick(DialogInterface dialog, int position) {
	  	        	String path = Util.saveToPdf(ScreenshotPreviewView.this.filepath);
		  			if (TextUtils.isEmpty(path)) {
		  				path = ScreenshotPreviewView.this.filepath;
		  			}
		  			
	  	        	switch(position){
	  	        	case 0:
	  	        		Util.sendEmail(ScreenshotPreviewView.this, path);
	  	        		break;
	  	        	case 1:
	  	        		Util.sendBlueTooth(ScreenshotPreviewView.this, path);
	  	        		break;
	  	        	case 2:
	  	        		Util.sendGeneric(ScreenshotPreviewView.this, path);
	  	        		break;
	  	        	}
	  	          }
	  			});
	  			// create alert dialog
	  			AlertDialog alertDialog = alertDialogBuilder.create();
	  			alertDialog.show();


	  		}});


    }

	@Override
    public void onResume() {
    	super.onResume();
    }

	private void setBitmap() {

		Bundle extras = this.getIntent().getExtras();
        this.filepath = extras.getString("filepath");

        File preview = new  File(this.filepath);
        Bitmap mapBitmap = BitmapFactory.decodeFile(preview.getAbsolutePath());

        int [] allpixels = new int [mapBitmap.getHeight() * mapBitmap.getWidth()];
        mapBitmap.getPixels(allpixels, 0, mapBitmap.getWidth(), 0, 0, mapBitmap.getWidth(), mapBitmap.getHeight());

        this.bitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), mapBitmap.getConfig());
        this.bitmap.setPixels(allpixels, 0, mapBitmap.getWidth(), 0, 0, mapBitmap.getWidth(), mapBitmap.getHeight());

	}

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        this.image = (ImageView)this.findViewById(R.id.preview_image);

        Canvas canvas = new Canvas(this.bitmap);
//        double maxH = 2048.0;

        Display display = this.getWindowManager().getDefaultDisplay();
        double maxH = display.getHeight();

        Logger.debug(this, "Maximum bitmap height       :" + maxH);
        Logger.debug(this, "Original bitmap ---- width  :" + this.bitmap.getWidth());
        Logger.debug(this, "Original bitmap ---- height :" + this.bitmap.getHeight());

        if (this.bitmap.getHeight() > maxH) {

            int newW = (int) (this.bitmap.getWidth() * (maxH / this.bitmap.getHeight()));

            Bitmap scaled = Bitmap.createScaledBitmap(this.bitmap, newW, (int) maxH, true);

            Logger.debug(this, "Scaled bitmap ---- width    :" + scaled.getWidth());
            Logger.debug(this, "Scaled bitmap ---- height   :" + scaled.getHeight());

            this.image.setImageBitmap(scaled);

        } else {
        	this.image.setImageBitmap(this.bitmap);
        }
    }

}
