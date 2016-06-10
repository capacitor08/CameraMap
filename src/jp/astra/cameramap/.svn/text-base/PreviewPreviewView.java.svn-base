package jp.astra.cameramap;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.StringUtil;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jem
 */
public class PreviewPreviewView extends BaseActivity {

	private Bitmap bitmap;
	private ImageView image;
	private TextView filename;
	private TextView txtLatitude;
	private TextView txtLongitude;
	private TextView txtAddress;
	private TextView txtDate;
	private TextView txtResolution;
	private String filepath;
	
	private Bitmap bitmapMap;
	private ImageView imageMap;
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
		
		Bundle extras = this.getIntent().getExtras();
		this.latitude = extras.getString("latitude");
		this.longitude = extras.getString("longitude");
		this.zoomLevel = Integer.parseInt(extras.getString("zoomLevel")) + 1;
		this.width = Integer.parseInt(extras.getString("width"));
		this.height = Integer.parseInt(extras.getString("height"));
		this.markerStr = extras.getString("markers");
		
		Logger.debug(this, "preview extras latitude  :" + this.latitude);
		Logger.debug(this, "preview extras longitude :" + this.longitude);
		Logger.debug(this, "preview extras zoomLevel :" + this.zoomLevel);
		Logger.debug(this, "preview extras markerStr :" + this.markerStr);
		
		this.adjustSize(); // adjust for google static map
		
		this.source = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "center=" + this.latitude + "," + this.longitude
				+ "&zoom=" + this.zoomLevel
				+ "&size=" + this.width + "x" + this.height
				+ "&scale=2" 
				+ "&markers=" + this.markerStr
				+ "&sensor=false";

		Logger.debug(this, " >>> url >>> " + this.source);
		
		
		this.setBitmap();
		this.setContentView(R.layout.preview_preview_view);
        
        this.txtLatitude.setText(extras.getString("txtLatitude"));
		this.txtLongitude.setText(extras.getString("txtLongitude"));
		this.txtAddress.setText(extras.getString("address"));
		this.txtDate.setText(extras.getString("date"));
		this.txtResolution.setText(extras.getString("resolution"));
        
        this.progress.show();
    	if(Util.isReachable(this)){
    		new BackgroundTask().execute();
    	}else{
    		Notify.toast(this, "No network connection");
    	}
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
        
        this.imageMap = (ImageView)this.findViewById(R.id.preview_image_map);
    	
    	this.btnSave = (Button)this.findViewById(R.id.screenshot);
    	this.btnSave.getBackground().setAlpha(160);
		this.btnSave.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				PreviewPreviewView.this.btnSave.setVisibility(View.INVISIBLE);
				PreviewPreviewView.this.captureScreenHandler.sendEmptyMessageDelayed(0, 100);
			}});

        this.image = (ImageView)this.findViewById(R.id.preview_image);
        this.image.setImageBitmap(this.bitmap);

        this.filename = (TextView)this.findViewById(R.id.preview_title);
        this.filename.setText(StringUtil.fileBaseName(this.filepath));

        this.txtLatitude = (TextView)this.findViewById(R.id.preview_lat);
        this.txtLongitude = (TextView)this.findViewById(R.id.preview_long);
        this.txtAddress = (TextView)this.findViewById(R.id.preview_add);
        this.txtDate = (TextView)this.findViewById(R.id.preview_date);
        this.txtResolution = (TextView)this.findViewById(R.id.preview_res);
    }

    
    private class BackgroundTask extends AsyncTask<Void, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			HttpGet httpRequest = null;
			try {
				URL url = new URL(PreviewPreviewView.this.source);
			    httpRequest = new HttpGet(url.toURI());
			    HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				InputStream instream = bufHttpEntity.getContent();
				PreviewPreviewView.this.bitmapMap = BitmapFactory.decodeStream(instream);
				
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
			PreviewPreviewView.this.imageMap.setImageBitmap(PreviewPreviewView.this.bitmapMap);
			PreviewPreviewView.this.progress.hide();
			Notify.toast(PreviewPreviewView.this, "Finished loading map");
		}
	}
    
    private final static int MAX_SIZE = 640;
    
    private void adjustSize() {
    	if (this.width > PreviewPreviewView.MAX_SIZE || this.height > PreviewPreviewView.MAX_SIZE) {
    		float oldHeight = this.height;
    		float oldWidth = this.width;
    		if (this.height > this.width) {
    			this.height = PreviewPreviewView.MAX_SIZE;
    			this.width = (int) (this.height * (oldWidth / oldHeight));
    		} else {
    			this.width = PreviewPreviewView.MAX_SIZE;
    			this.height = (int) (this.width * (oldHeight / oldWidth));
    			
    		}
    	}
    }
    
    public final Handler captureScreenHandler = new Handler() {

	      @Override
	      public void handleMessage(Message msg) {
	    	  

	    	  Bitmap bitmap;
	    	  
	    	  View view = PreviewPreviewView.this.findViewById(R.id.scroll_view);
	    	  bitmap = Util.getBitmapFromView(view);

	    	  Util.saveScreenshot(PreviewPreviewView.this, bitmap);
	    	  
	    	  PreviewPreviewView.this.btnSave.setVisibility(View.VISIBLE);
	    	  Toast.makeText(PreviewPreviewView.this, "Save successful", Toast.LENGTH_SHORT).show();
	    	  
	    	  super.handleMessage(msg);
	      }
	  };
}
