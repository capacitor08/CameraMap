package jp.astra.cameramap;

import java.io.File;
import java.io.IOException;

import jp.astra.cameramap.helper.ActivityUtil;
import jp.astra.cameramap.helper.Dictionary;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.StringUtil;
import jp.astra.cameramap.helper.Util;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

/**
 * @author Lau
 */
public class PreviewView extends MapActivity {

	private Bitmap bitmap;
//	private Bitmap previewBitmap;
	private ImageView image;
	private TextView filename;
	private TextView txtLatitude;
	private TextView txtLongitude;
	private TextView txtAddress;
	private TextView txtDate;
	private TextView txtResolution;
	private String filepath;
	private GoogleMap map;
	private LatLng latlng;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setBitmap();
		this.setContentView(R.layout.preview_view);
		this.inflateControls(this);

		Button buttonGallery = (Button)this.findViewById(R.id.gallerylist);
        buttonGallery.setOnClickListener(this.galleryListButtonListener);

		Button buttonTakePicture = (Button)this.findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(this.captureButtonListener);

        Button buttonMap = (Button)this.findViewById(R.id.photomap);
        buttonMap.setOnClickListener(this.mapButtonListener);

        Button buttonScreenshots = (Button)this.findViewById(R.id.screenshots);
        buttonScreenshots.setOnClickListener(this.reportButtonListener);

        Button buttonPreview = (Button)this.findViewById(R.id.previewbtn);
        buttonPreview.setVisibility(View.VISIBLE);
        buttonPreview.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {

				Dictionary data = new Dictionary();
	  			data.setData("latitude", ""+PreviewView.this.latitude);
	  			data.setData("longitude", ""+PreviewView.this.longitude);
	  			data.setData("zoomLevel", ""+PreviewView.this.zoomLevel);
	  			data.setData("width", ""+PreviewView.this.width);
	  			data.setData("height", ""+PreviewView.this.height);
	  			data.setData("markers", PreviewView.this.markerStr);
		        data.setData("filepath", PreviewView.this.filepath);
		        data.setData("txtLatitude", PreviewView.this.txtLatitude.getText());
		        data.setData("txtLongitude", PreviewView.this.txtLongitude.getText());
		        data.setData("address", PreviewView.this.txtAddress.getText());
		        data.setData("date", PreviewView.this.txtDate.getText());
		        data.setData("resolution", PreviewView.this.txtResolution.getText());
	  			ActivityUtil.startActivity(PreviewView.this, PreviewPreviewView.class, data);


		        Logger.debug(PreviewView.this, "extras latitude  :" + PreviewView.this.latitude);
				Logger.debug(PreviewView.this, "extras longitude :" + PreviewView.this.longitude);
				Logger.debug(PreviewView.this, "extras zoomLevel :" + PreviewView.this.zoomLevel);
				Logger.debug(PreviewView.this, "extras markerStr :" + PreviewView.this.markerStr);

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
        this.image.setImageBitmap(this.bitmap);

        this.filename = (TextView)this.findViewById(R.id.preview_title);
        this.filename.setText(StringUtil.fileBaseName(this.filepath));

        this.txtLatitude = (TextView)this.findViewById(R.id.preview_lat);
        this.txtLongitude = (TextView)this.findViewById(R.id.preview_long);
        this.txtAddress = (TextView)this.findViewById(R.id.preview_add);

        this.map = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.map))
        .getMap();

	    this.setPhotoLocation();

	    if(this.latlng != null){
	    	this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latlng, 21));
	    }

	    // Zoom in, animating the camera.
	    this.map.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);

	    this.map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                PreviewView.this.latitude = Util.roundDecimals(cameraPosition.target.latitude);
            	PreviewView.this.longitude = Util.roundDecimals(cameraPosition.target.longitude);
            	PreviewView.this.zoomLevel = (int) cameraPosition.zoom;
            	Logger.debug(PreviewView.this, "new latitude    :" + PreviewView.this.txtLatitude);
            	Logger.debug(PreviewView.this, "new longitude   :" + PreviewView.this.txtLongitude);
            	Logger.debug(PreviewView.this, "new zoom level  :" + PreviewView.this.zoomLevel);
//            	latLng = cameraPosition.target;
            }
        });


    }

    public void setPhotoLocation(){
		  ExifInterface exif;
		  Float latitude;
		  Float longitude;
		  GeoPoint point;
	  	  String locationName;
		try {
			exif = new ExifInterface(this.filepath);
			if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null && exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null){
				latitude = StringUtil.convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
				if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("S")){
					latitude = 0 - latitude;
				}
				this.txtLatitude.setText(StringUtil.convertToDegreeString(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE), exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)));
				longitude = StringUtil.convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
				if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("W")){
					longitude = 0 - longitude;
				}
				this.txtLongitude.setText(StringUtil.convertToDegreeString(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE), exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)));
				this.latlng = new LatLng(latitude, longitude);

				this.latitude = latitude;
				this.longitude = longitude;

				point = new GeoPoint(
			              (int) (latitude * 1E6),
			              (int) (longitude * 1E6));
				locationName = StringUtil.ConvertPointToLocation(PreviewView.this, point);
				this.txtAddress.setText(locationName);
				Marker marker = this.map.addMarker(new MarkerOptions().position(this.latlng)
						.snippet(locationName)
				        .title(StringUtil.fileBaseName(this.filepath)));

				this.markers.add(marker);
				this.markerStr = Util.markersToString(this.markers);

				this.txtDate = (TextView)this.findViewById(R.id.preview_date);
				if(exif != null){
					this.txtDate.setText(StringUtil.convertDate(exif.getAttribute(ExifInterface.TAG_DATETIME)));

					this.txtResolution = (TextView)this.findViewById(R.id.preview_res);
					this.txtResolution.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "x" + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
				}

//				this.buttonPreview.setVisibility(View.VISIBLE);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  }

}
