package jp.astra.cameramap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jp.astra.cameramap.helper.ActivityUtil;
import jp.astra.cameramap.helper.Dictionary;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.Util;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;

public class PhotoMapActivity extends MapActivity implements OnInfoWindowClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.photo_map);
    this.inflateControls(this);
    this.map = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.map)).getMap();
    this.map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
        	if (cameraPosition.zoom != PhotoMapActivity.this.oldZoom) {
        		PhotoMapActivity.this.checkMarkers(PhotoMapActivity.this.map);
        	}
        	PhotoMapActivity.this.oldZoom = cameraPosition.zoom;

        	PhotoMapActivity.this.latitude = Util.roundDecimals(cameraPosition.target.latitude);
        	PhotoMapActivity.this.longitude = Util.roundDecimals(cameraPosition.target.longitude);
        	PhotoMapActivity.this.zoomLevel = (int) cameraPosition.zoom;
        	Logger.debug(PhotoMapActivity.this, "new latitude    :" + PhotoMapActivity.this.latitude);
        	Logger.debug(PhotoMapActivity.this, "new longitude   :" + PhotoMapActivity.this.longitude);
        	Logger.debug(PhotoMapActivity.this, "new zoom level  :" + PhotoMapActivity.this.zoomLevel);
//        	latLng = cameraPosition.target;

        }
    });
    this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLng, 5));
    this.imageFolder = new  File(Util.getExternalStoragePath(this));

    this.latitude = (float) 35.645;
    this.longitude = (float) 139.452;
    this.zoomLevel = 21;

    Button buttonTakePicture = (Button)this.findViewById(R.id.takepicture);
    buttonTakePicture.setOnClickListener(this.captureButtonListener);

    Button buttonList = (Button)this.findViewById(R.id.gallerylist);
    buttonList.setOnClickListener(this.galleryListButtonListener);

    Button buttonScreenshots = (Button)this.findViewById(R.id.screenshots);
    buttonScreenshots.setOnClickListener(this.reportButtonListener);

    Button btnSave = (Button)this.findViewById(R.id.previewbtn);
    btnSave.setVisibility(View.VISIBLE);
    btnSave.setOnClickListener(new Button.OnClickListener(){

  		@Override
  		public void onClick(View arg0) {

  			Dictionary data = new Dictionary();
  			data.setData("latitude", ""+PhotoMapActivity.this.latitude);
  			data.setData("longitude", ""+PhotoMapActivity.this.longitude);
  			data.setData("zoomLevel", ""+PhotoMapActivity.this.zoomLevel);
  			data.setData("width", ""+PhotoMapActivity.this.width);
  			data.setData("height", ""+PhotoMapActivity.this.height);
  			data.setData("markers", PhotoMapActivity.this.markerStr);
  			ActivityUtil.startActivity(PhotoMapActivity.this, MapPreviewView.class, data);

  		}});
    this.progress.show();
    if(Util.isReachable(this)){
        this.map.clear();
        this.markers = new ArrayList<Marker>();
        this.fileNames = new HashMap<Marker, String>();
    	new BackgroundTask().execute();
    }else{
    	this.hideProgressHandler.sendEmptyMessage(0);
    	Notify.toast(this, "No network connection");
    }

  }
}
