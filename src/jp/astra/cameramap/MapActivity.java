package jp.astra.cameramap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.astra.cameramap.MapActivity.BackgroundTask.Details;
import jp.astra.cameramap.helper.ActivityUtil;
import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Dictionary;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.StringUtil;
import jp.astra.cameramap.helper.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

public class MapActivity extends BaseActivity implements OnInfoWindowClickListener{

  protected GoogleMap map;
  protected String[] filePaths;
  protected File imageFolder;
  protected ArrayList<Marker> markers = new ArrayList<Marker>();
  protected LatLng latLng = new LatLng(35.645, 139.452);
  protected HashMap<Marker, String> fileNames = new HashMap<Marker, String>();
  protected float oldZoom = 0;
  protected double latitude = 35.645;
  protected double longitude = 139.452;
  protected int zoomLevel = 15;
  protected String markerStr = "";
  protected int left, top, width, height = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public void onResume(){
	  super.onResume();
  }

  @Override
	public void onWindowFocusChanged(boolean hasFocus) {

		View globalView = this.getWindow().getDecorView().getRootView();

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int topOffset = dm.heightPixels - globalView.getMeasuredHeight();

		View tempView = this.findViewById(R.id.map);
		int[] loc = new int[2];
		tempView.getLocationOnScreen(loc);

		final int y = loc[1] - topOffset;
		this.top = y;

		Rect rectf = new Rect();
		tempView.getLocalVisibleRect(rectf);

		this.left = rectf.left;
		this.width = rectf.width();
		this.height = rectf.height();

		Logger.debug(this, "WIDTH        :" + String.valueOf(this.width));
		Logger.debug(this, "HEIGHT       :" + String.valueOf(this.height));
		Logger.debug(this, "left         :" + String.valueOf(this.left));
		Logger.debug(this, "right        :" + String.valueOf(rectf.right));
		Logger.debug(this, "top          :" + String.valueOf(this.top));
		Logger.debug(this, "bottom       :" + String.valueOf(rectf.bottom));

		super.onWindowFocusChanged(hasFocus);
	}

  public LatLng setPhotoLocation(String filepath){
	  ExifInterface exif;
	  Float latitude;
	  Float longitude;
	try {
		exif = new ExifInterface(filepath);
		if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!= null  &&  exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)!= null){
			latitude = StringUtil.convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
			if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("S")){
				latitude = 0 - latitude;
			}
			longitude = StringUtil.convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
			if(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("W")){
				longitude = 0 - longitude;
			}
			this.latLng = new LatLng(latitude, longitude);
		}else{
			this.latLng = null;
		}

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return this.latLng;

  }

  public class BackgroundTask extends AsyncTask<Void, Details, Void> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public class Details
	{
	    public LatLng location;
	    public String locationName;
	    public String filePath;
	}

	@Override
	protected Void doInBackground(Void... params) {
    	for (File file : MapActivity.this.imageFolder.listFiles()) {
    		if (!file.isDirectory()) {
    			LatLng location = MapActivity.this.setPhotoLocation(file.getAbsolutePath());
    			if(location != null){
	    			GeoPoint point = new GeoPoint(
	    		              (int) (location.latitude * 1E6),
	    		              (int) (location.longitude * 1E6));
	    			String locationName = StringUtil.ConvertPointToLocation(MapActivity.this, point);
	    			Details details = new Details();
	    			details.locationName = locationName;
	    			details.location = location;
	    			details.filePath = file.getAbsolutePath();

	    			this.publishProgress(details);
    			}
    		}
    	}
		return null;
	}

	@Override
	protected void onProgressUpdate(Details... values) {
		super.onProgressUpdate(values);
		Marker marker = MapActivity.this.map.addMarker(new MarkerOptions().position(values[0].location)
				.snippet(values[0].locationName)
				.title(StringUtil.fileBaseName(values[0].filePath)));
		MapActivity.this.markers.add(marker);
		MapActivity.this.fileNames.put(marker, marker.getTitle());
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
//		MapActivity.this.mapHandler.sendEmptyMessage(0);
		MapActivity.this.mapUpdate();
	}
  }

  protected void mapUpdate() {
	  MapActivity.this.hideProgressHandler.sendEmptyMessage(0);
	  MapActivity.this.map.setOnInfoWindowClickListener(MapActivity.this);
	  MapActivity.this.map.setInfoWindowAdapter(new MarkerInfo(MapActivity.this, MapActivity.this.getLayoutInflater()));

	  LatLngBounds.Builder bld = new LatLngBounds.Builder();
	  if(!this.markers.isEmpty()){
		  for (Marker marker : this.markers) {
			  LatLng ll = marker.getPosition();
			  bld.include(ll);
		  }

		  LatLngBounds bounds = this.setMaxZoomBound(bld.build());
		  this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
	  }
	  this.markerStr = Util.markersToString(this.markers);
  }

  protected LatLngBounds setMaxZoomBound(LatLngBounds bounds){
	  LatLngBounds newBounds = bounds;
	  if(Math.abs(bounds.northeast.latitude - bounds.southwest.latitude) >= 0.0006 || Math.abs(bounds.northeast.longitude - bounds.southwest.longitude) >= 0.001){
		  return bounds;
	  }else{
		  double neLatitude = bounds.northeast.latitude;
		  double swLatitude = bounds.southwest.latitude;
		  double neLongitude = bounds.northeast.longitude;
		  double swLongitude = bounds.southwest.longitude;
		  if(Math.abs(neLatitude - swLatitude) < 0.0006){
			  double center = (neLatitude + swLatitude)/2;
			  neLatitude = center + 0.0003;
			  swLatitude = center - 0.0003;
		  }
		  if(Math.abs(neLongitude - swLongitude) < 0.001){
			  double center = (neLongitude + swLongitude)/2;
			  neLongitude = center + 0.0005;
			  swLongitude = center - 0.0005;
		  }
		  LatLng ne = new LatLng(neLatitude, neLongitude);
		  LatLng sw = new LatLng(swLatitude, swLongitude);
		  newBounds = new LatLngBounds(sw,ne);
	  }
	  return newBounds;
  }


  protected void checkMarkers(GoogleMap map) {
	    Projection projection = map.getProjection();
	    LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
	    HashMap<Marker, Point> points = new HashMap<Marker, Point>();
	    for (Marker marker : this.markers) {
	        if (bounds.contains(marker.getPosition())) {
	            points.put(marker, projection.toScreenLocation(marker.getPosition()));
	            marker.setVisible(false);
	        }
	    }
	    CheckMarkersTask checkMarkersTask = new CheckMarkersTask(MapActivity.this, this.fileNames);
	    checkMarkersTask.execute(points);
}

@Override
public void onInfoWindowClick(Marker marker) {
	if(marker.getTitle().endsWith(".jpg")){
		String filePath = Util.getExternalStoragePath(MapActivity.this) + "/" + marker.getTitle();
		Dictionary bundle = new Dictionary();
		bundle.setData("filepath", filePath);
		ActivityUtil.openActivityOnTop(MapActivity.this, PreviewView.class, bundle);
	}else{
		//show spinner
		String fileNames = marker.getSnippet().replaceFirst("files:", "");
		final String[] list = fileNames.split(",");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
		alertDialogBuilder.setTitle("Choose file");
		alertDialogBuilder.setItems(list, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int position) {
          	String filePath = Util.getExternalStoragePath(MapActivity.this) + "/" + list[position];
      		Dictionary bundle = new Dictionary();
      		bundle.setData("filepath", filePath);
      		ActivityUtil.openActivityOnTop(MapActivity.this, PreviewView.class, bundle);
          }
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
};

private class CheckMarkersTask extends AsyncTask<HashMap<Marker, Point>, Void, HashMap<Point, ArrayList<Marker>>> {

	  	Context context;
	  	HashMap<Marker, String> fileNames;

	  	public CheckMarkersTask(Context context, HashMap<Marker, String> fileNames){
	  		this.context = context;
	  		this.fileNames = fileNames;
		}

	  	private double findDistance(float x1, float y1, float x2, float y2) {
	        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	    }

	    @Override
	    protected HashMap<Point, ArrayList<Marker>> doInBackground(HashMap<Marker, Point>... params) {
	        HashMap<Point, ArrayList<Marker>> clusters = new HashMap<Point, ArrayList<Marker>>();
	        HashMap<Marker, Point> points = params[0];
	        boolean wasClustered;
	        for (Marker marker : points.keySet()) {
	            Point point = points.get(marker);
	            wasClustered = false;
	            for (Point existingPoint : clusters.keySet()) {
	                if (this.findDistance(point.x, point.y, existingPoint.x, existingPoint.y) < 5) {
	                    wasClustered = true;
	                    clusters.get(existingPoint).add(marker);
	                    break;
	                }
	            }
	            if (!wasClustered) {
	                ArrayList<Marker> markersForPoint = new ArrayList<Marker>();
	                markersForPoint.add(marker);
	                clusters.put(point, markersForPoint);
	            }
	        }
	        return clusters;
	    }

	    @Override
	    protected void onPostExecute(HashMap<Point, ArrayList<Marker>> clusters) {
	        for (Point point : clusters.keySet()) {
	        	Marker mainMarker;
	            ArrayList<Marker> markersForPoint = clusters.get(point);
	            if(markersForPoint.size() == 1){
	            	mainMarker = markersForPoint.get(0);
	            	GeoPoint geoPoint = new GeoPoint(
				              (int) (mainMarker.getPosition().latitude * 1E6),
				              (int) (mainMarker.getPosition().longitude * 1E6));
	            	mainMarker.setTitle(this.fileNames.get(mainMarker));
	            	mainMarker.setSnippet(StringUtil.ConvertPointToLocation(this.context, geoPoint));
	            	mainMarker.setVisible(true);
	            }else{
	            	String snippet = "files:";
	            	for(int i=0; i < markersForPoint.size(); i++){
	            		snippet = snippet + this.fileNames.get(markersForPoint.get(i));
	            		if(i < markersForPoint.size() - 1){
	            			snippet = snippet + ",";
	            		}
	            	}
	            	mainMarker = markersForPoint.get(0);
	            	mainMarker.setTitle(Integer.toString(markersForPoint.size()));
	            	mainMarker.setSnippet(snippet);
	            	mainMarker.setVisible(true);
	            }
	        }
	    }
	}
}
