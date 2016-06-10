package jp.astra.cameramap;

import java.io.InputStream;
import java.net.URL;

import jp.astra.cameramap.helper.ActivityUtil;
import jp.astra.cameramap.helper.ArrayUtil;
import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.CustomGridView;
import jp.astra.cameramap.helper.Dictionary;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jem
 */
public class GalleryPreviewView extends BaseActivity {

	private ImageAdapter imageAdapter;
	private CustomGridView gridview;
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
	private String[] files;
	private String imageFolderPath;


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
		this.files = extras.getStringArray("filePath");
		this.imageFolderPath = extras.getString("imageFolder");

		Logger.debug(this, "preview extras latitude  :" + this.latitude);
		Logger.debug(this, "preview extras longitude :" + this.longitude);
		Logger.debug(this, "preview extras zoomLevel :" + this.zoomLevel);
		Logger.debug(this, "preview extras markerStr :" + this.markerStr);
		Logger.debug(this, "preview extras imageFolder :" + this.imageFolderPath);

		this.adjustSize();

		this.source = "http://maps.googleapis.com/maps/api/staticmap?"
				+ "center=" + this.latitude + "," + this.longitude
				+ "&zoom=" + this.zoomLevel
				+ "&size=" + this.width + "x" + this.height
				+ "&scale=2"
				+ "&markers=" + this.markerStr
				+ "&sensor=false";

		Logger.debug(this, " >>> url >>> " + this.source);

    	this.progress.show();
    	if(Util.isReachable(this)){
    		new BackgroundTask().execute();
    	}else{
    		Notify.toast(this, "No network connection");
    	}

    	this.setContentView(R.layout.gallery_preview_view);

    }

	@Override
    public void onResume() {
    	super.onResume();
    	Notify.toast(GalleryPreviewView.this, "scanning...");
	}

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        this.image = (ImageView)this.findViewById(R.id.preview_image);

    	this.btnSave = (Button)this.findViewById(R.id.screenshot);
    	this.btnSave.getBackground().setAlpha(160);
		this.btnSave.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				GalleryPreviewView.this.btnSave.setVisibility(View.INVISIBLE);
				GalleryPreviewView.this.captureScreenHandler.sendEmptyMessageDelayed(0, 100);
			}});

        this.imageAdapter = new ImageAdapter();

        this.gridview = (CustomGridView) this.findViewById(R.id.gallery_grid_view);
        this.gridview.setExpanded(true);

        this.gridview.setAdapter(this.imageAdapter);
        this.gridview.setOnItemClickListener(this.imageListener);

        if (this.imageAdapter.isEmpty()) {
        	TextView emptyListText = (TextView) this.findViewById(R.id.gallery_no_images_text);
        	emptyListText.setVisibility(View.VISIBLE);
        }

    }

    private OnItemClickListener imageListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long extra) {
			Dictionary bundle = new Dictionary();
	        bundle.setData("filepath", GalleryPreviewView.this.imageAdapter.getItem(position));
	        ActivityUtil.openActivityOnTop(GalleryPreviewView.this, PreviewView.class, bundle);
		}

	};

//	public final Handler refreshGalleryHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//        	Notify.toast(GalleryPreviewView.this, "scanning complete!");
//
//        	GalleryPreviewView.this.imageAdapter.notifyDataSetChanged();
//	    	GalleryPreviewView.this.gridview.invalidateViews();
//	    	GalleryPreviewView.this.hideProgressHandler.sendEmptyMessage(0);
//
//        	super.handleMessage(msg);
//        }
//    };

    private class ImageAdapter extends BaseAdapter {

    	private String[] filePaths;

    	public ImageAdapter() {
			this.getImageFiles();
		}

    	@Override
		public boolean isEmpty() {
    		return this.getCount() < 1;
    	}

    	@Override
		public int getCount() {
            return this.filePaths.length;
        }

        @Override
		public Object getItem(int position) {
            return this.filePaths[position];
        }

        @Override
		public long getItemId(int position) {
            return 0;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {

        	View v = convertView;
        	if (v == null) {
        		LayoutInflater inflater = GalleryPreviewView.this.getLayoutInflater();
        		v = inflater.inflate(R.layout.gallery_row_view, null);
    	   }

            ImageView imageView = (ImageView)v.findViewById(R.id.imageGrid);
            TextView textView = (TextView)v.findViewById(R.id.filename);

            String imageFile = this.filePaths[position];
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 4;
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile, o2);
            imageView.setImageBitmap(imageBitmap);
            textView.setText(StringUtil.fileBaseName(imageFile));

            return v;

        }

        public void getImageFiles() {

        	int count = 0;
        	this.filePaths = new String[0];

        	for (String file : GalleryPreviewView.this.files) {
    			this.filePaths = ArrayUtil.insertToStringArray(this.filePaths, file, count);
    			count++;
        	}

        	if (count > 0) {
        		GalleryPreviewView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.INVISIBLE);
        	} else {
        		GalleryPreviewView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.VISIBLE);
        	}
        }

        @Override
        public void notifyDataSetChanged() {
        	this.getImageFiles();
        	super.notifyDataSetChanged();
        }
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
				URL url = new URL(GalleryPreviewView.this.source);
			    httpRequest = new HttpGet(url.toURI());
			    HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				InputStream instream = bufHttpEntity.getContent();
				GalleryPreviewView.this.bitmap = BitmapFactory.decodeStream(instream);

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
			GalleryPreviewView.this.image.setImageBitmap(GalleryPreviewView.this.bitmap);
			GalleryPreviewView.this.progress.hide();
			Notify.toast(GalleryPreviewView.this, "Finished loading map");
		}
	}

    private final static int MAX_SIZE = 640;

    private void adjustSize() {
    	if (this.width > GalleryPreviewView.MAX_SIZE || this.height > GalleryPreviewView.MAX_SIZE) {
    		float oldHeight = this.height;
    		float oldWidth = this.width;
    		if (this.height > this.width) {
    			this.height = GalleryPreviewView.MAX_SIZE;
    			this.width = (int) (this.height * (oldWidth / oldHeight));
    		} else {
    			this.width = GalleryPreviewView.MAX_SIZE;
    			this.height = (int) (this.width * (oldHeight / oldWidth));

    		}
    	}
    }

    public final Handler captureScreenHandler = new Handler() {

	      @Override
	      public void handleMessage(Message msg) {

	    	  Bitmap bitmap;

	    	  View view = GalleryPreviewView.this.findViewById(R.id.scroll_view);
	    	  bitmap = Util.getBitmapFromView(view);

	    	  Util.saveScreenshot(GalleryPreviewView.this, bitmap);

	    	  GalleryPreviewView.this.btnSave.setVisibility(View.VISIBLE);
	    	  Toast.makeText(GalleryPreviewView.this, "Save successful", Toast.LENGTH_SHORT).show();

	    	  super.handleMessage(msg);
	      }
	  };
}
