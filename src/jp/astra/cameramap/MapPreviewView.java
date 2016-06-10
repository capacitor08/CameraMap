package jp.astra.cameramap;

import java.io.InputStream;
import java.net.URL;

import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author jem
 */
public class MapPreviewView extends BaseActivity {

	private Bitmap bitmap;
	private ImageView image;
	private String latitude;
	private String longitude;
	private int width;
	private int height;
	private int zoomLevel;
	private String markerStr;
	private String source;
	private Button btnSave;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setBitmap();
		this.setContentView(R.layout.map_preview_view);

		this.btnSave = (Button)this.findViewById(R.id.screenshot);
		this.btnSave.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				MapPreviewView.this.btnSave.setVisibility(View.INVISIBLE);
				
				MapPreviewView.this.captureScreenHandler.sendEmptyMessageDelayed(0, 100);
				
			}});
	}

	@Override
    public void onResume() {
    	super.onResume();
    }
	
	public final Handler captureScreenHandler = new Handler() {

	      @Override
	      public void handleMessage(Message msg) {
	    	  
	    	  Bitmap bitmap;
	    	  View rootview = MapPreviewView.this.getWindow().getDecorView().getRootView();
	    	  rootview.setDrawingCacheEnabled(true);
	    	  bitmap = Bitmap.createBitmap(rootview.getDrawingCache());
	    	  rootview.setDrawingCacheEnabled(false);
	    	  
	    	  Util.saveScreenshot(MapPreviewView.this, bitmap);
	    	  
	    	  MapPreviewView.this.btnSave.setVisibility(View.VISIBLE);
	    	  Toast.makeText(MapPreviewView.this, "Save successful", Toast.LENGTH_SHORT).show();
	    	  
	    	  super.handleMessage(msg);
	      }
	  };

	private void setBitmap() {

		Bundle extras = this.getIntent().getExtras();
		this.latitude = extras.getString("latitude");
		this.longitude = extras.getString("longitude");
		this.zoomLevel = Integer.parseInt(extras.getString("zoomLevel")) + 1;
		this.width = Integer.parseInt(extras.getString("width"));
		this.height = Integer.parseInt(extras.getString("height"));
		this.markerStr = extras.getString("markers");
		
		this.adjustSize();
		
		this.source = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "center=" + this.latitude + "," + this.longitude
				+ "&zoom=" + this.zoomLevel
				+ "&size=" + this.width + "x" + this.height
				+ "&scale=2" 
				+ "&markers=" + this.markerStr
				+ "&sensor=false";

		Logger.debug(this, " >>> url >>> " + this.source);
		
		new BackgroundTask().execute();
		
	}

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        this.image = (ImageView)this.findViewById(R.id.preview_image);

    }
    
    private class BackgroundTask extends AsyncTask<Void, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			MapPreviewView.this.progress.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			HttpGet httpRequest = null;
			try {
				URL url = new URL(MapPreviewView.this.source);
			    httpRequest = new HttpGet(url.toURI());
			    HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				InputStream instream = bufHttpEntity.getContent();
				MapPreviewView.this.bitmap = BitmapFactory.decodeStream(instream);
				
			} catch (Exception e) {
			    e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MapPreviewView.this.image.setImageBitmap(MapPreviewView.this.bitmap);
			MapPreviewView.this.progress.hide();
			Notify.toast(MapPreviewView.this, "Finished loading map");
		}
	}
    
    private final static int MAX_SIZE = 640;
    
    private void adjustSize() {
    	if (this.width > MapPreviewView.MAX_SIZE || this.height > MapPreviewView.MAX_SIZE) {
    		float oldHeight = this.height;
    		float oldWidth = this.width;
    		if (this.height > this.width) {
    			this.height = MapPreviewView.MAX_SIZE;
    			this.width = (int) (this.height * (oldWidth / oldHeight));
    		} else {
    			this.width = MapPreviewView.MAX_SIZE;
    			this.height = (int) (this.width * (oldHeight / oldWidth));
    			
    		}
    	}
    }

}
