package jp.astra.cameramap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.Util;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback {

	protected Bitmap unrotatedBmp;

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	private Location location = null;
	private LocationManager locationManager = null;
	private String filename = "";
	private int cameraId = 0;

    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_camera);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Get screensize
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        this.getWindow().setFormat(PixelFormat.UNKNOWN);
        this.surfaceView = (SurfaceView)this.findViewById(R.id.camerapreview);
        if(width < 720){
        	this.surfaceView.setLayoutParams(new LinearLayout.LayoutParams(480, 640));
        }else{
        	this.surfaceView.setLayoutParams(new LinearLayout.LayoutParams(720, 960));
        }
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.surfaceHolder.addCallback(this);

        this.inflateControls(this);

        //Check if device has camera
        if(!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && !this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
        	//finish activity
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        if(numberOfCameras == 1){
        	this.cameraId = 0;
        }else{
	        for (int i = 0; i < numberOfCameras; i++) {
	          CameraInfo info = new CameraInfo();
	          Camera.getCameraInfo(i, info);
	          if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
	            this.cameraId = i;
	            break;
	          }
	        }
        }

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Notify.toast(this, "GPS not enabled");
			this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		} else {
			Notify.toast(this, "GPS is enabled!");
		}

		this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (this.location != null) {
			this.gpsLocationListener.onLocationChanged(this.location);
        }

        Button buttonTakePicture = (Button)this.findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(this);

        Button buttonGallery = (Button)this.findViewById(R.id.gallerylist);
        buttonGallery.setOnClickListener(this.galleryListButtonListener);

        Button buttonMap = (Button)this.findViewById(R.id.photomap);
        buttonMap.setOnClickListener(this.mapButtonListener);

        Button buttonView = (Button)this.findViewById(R.id.ChangeCamera);
        if(numberOfCameras > 1){
        	buttonView.setVisibility(View.VISIBLE);
	        buttonView.setOnClickListener(this);
        }else{
        	buttonView.setVisibility(View.GONE);
        }

        Button buttonScreenshots = (Button)this.findViewById(R.id.screenshots);
        buttonScreenshots.setOnClickListener(this.reportButtonListener);

    }

    @Override
    public void onResume(){

    	super.onResume();
    	this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.gpsLocationListener);
		this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.networkListener);

    }

    @Override
    public void onClick(View view) {
    	switch(view.getId()){
    	case R.id.takepicture:
    		Camera.Parameters parameters = CameraActivity.this.camera.getParameters();
			if (CameraActivity.this.location != null) {
        		parameters.setGpsLatitude(CameraActivity.this.location.getLatitude());
        		parameters.setGpsLongitude(CameraActivity.this.location.getLongitude());
        		parameters.setGpsTimestamp(CameraActivity.this.location.getTime());
        		parameters.setRotation(90);
        		CameraActivity.this.camera.setParameters(parameters);
        	}

			CameraActivity.this.camera.autoFocus(CameraActivity.this.myAutoFocusCallback);
    		break;
    	case R.id.ChangeCamera:
    		CameraActivity.this.changeCameraView();
    		break;
    	}

    }

    public void changeCameraView(){
    	int newDirection;
    	CameraInfo info = new CameraInfo();
    	Camera.getCameraInfo(this.cameraId, info);
    	if(info.facing == CameraInfo.CAMERA_FACING_BACK){
    		newDirection = CameraInfo.CAMERA_FACING_FRONT;
    	}else{
    		newDirection = CameraInfo.CAMERA_FACING_BACK;
    	}
    	int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {

          Camera.getCameraInfo(i, info);
          if (info.facing == newDirection) {
            this.cameraId = i;
            break;
          }
        }
		this.camera.stopPreview();
		this.camera.release();
		this.camera = Camera.open(this.cameraId);
		if(this.previewing){
			this.camera.stopPreview();
			this.previewing = false;
		}

		if (this.camera != null){
			try {
				this.camera.setPreviewDisplay(this.surfaceHolder);
				this.camera.setDisplayOrientation(90);
				this.camera.startPreview();
				this.previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intent = new Intent(CameraActivity.this, GalleryListView.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            CameraActivity.this.startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    ShutterCallback myShutterCallback = new ShutterCallback(){

		@Override
		public void onShutter() {

		}};

	PictureCallback myPictureCallback_RAW = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {


		}};

	PictureCallback myPictureCallback_JPG = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {

			CameraActivity.this.camera.cancelAutoFocus();

			File pictureFileDir = CameraActivity.this.getDir();

		    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

		      Log.d("CameraMap", "Can't create directory to save image.");
		      return;

		    }

		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = sdf.format(new Date());
		    String photoFile = date + ".jpg";

		    CameraActivity.this.filename = pictureFileDir.getPath() + File.separator + photoFile;

		    File pictureFile = new File(CameraActivity.this.filename);

		    try {
		      FileOutputStream fos = new FileOutputStream(pictureFile);
		      fos.write(data);
		      fos.close();

		    } catch (Exception error) {
		      Log.d("CameraMap", "File" + CameraActivity.this.filename + "not saved: "
		          + error.getMessage());
		      return;
		    }

		    CameraActivity.this.camera.startPreview();

		}};

		private File getDir() {
			File sdDir = new  File(Util.getExternalStoragePath(this));
			return sdDir;
	  }


	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

	  @Override
	  public void onAutoFocus(boolean arg0, Camera arg1) {
//
		  CameraActivity.this.camera.takePicture(CameraActivity.this.myShutterCallback,
					CameraActivity.this.myPictureCallback_RAW, CameraActivity.this.myPictureCallback_JPG);
	  }
	};


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(this.previewing){
			this.camera.stopPreview();
			this.previewing = false;
		}
//		Parameters parameters = this.camera.getParameters();

		if (this.camera != null){
			try {
				this.camera.setPreviewDisplay(this.surfaceHolder);
				this.camera.setDisplayOrientation(90);
				this.camera.startPreview();
				this.previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		this.camera = Camera.open(this.cameraId);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.camera.stopPreview();
		this.camera.release();
		this.camera = null;
		this.previewing = false;
	}

	private final LocationListener gpsLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            case LocationProvider.AVAILABLE:
            	Notify.toast(CameraActivity.this, "GPS available again");
                break;
            case LocationProvider.OUT_OF_SERVICE:
            	Notify.toast(CameraActivity.this, "GPS out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	Notify.toast(CameraActivity.this, "Temporary available");
                break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        	Notify.toast(CameraActivity.this, "Provider Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
        	Notify.toast(CameraActivity.this, "Provider Disabled");
        }

        @Override
        public void onLocationChanged(Location location) {
//            CameraActivity.this.locationManager.removeUpdates(this);
        	CameraActivity.this.location = location;
//            Notify.toast(CameraActivity.this, "LATITUDE = "+location.getLatitude() + " LONGITUDE = " + location.getLongitude());

        }
    };

    @Override
    public void onPause(){
    	this.locationManager.removeUpdates(this.networkListener);
    	this.locationManager.removeUpdates(this.gpsLocationListener);
    	super.onPause();
    }

	private final LocationListener networkListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            case LocationProvider.AVAILABLE:
            	Notify.toast(CameraActivity.this, "Network Location available again");
                break;
            case LocationProvider.OUT_OF_SERVICE:
            	Notify.toast(CameraActivity.this, "Network Location out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	Notify.toast(CameraActivity.this, "Network Location Temporary available");
                break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        	Notify.toast(CameraActivity.this, "Network Provider Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
        	Notify.toast(CameraActivity.this, "Network Provider Disabled");
        }

        @Override
        public void onLocationChanged(Location location) {
        	CameraActivity.this.location = location;
//            Notify.toast(CameraActivity.this, "network location LATITUDE = "+location.getLatitude() + " LONGITUDE = " + location.getLongitude());

        }
    };

}

