package jp.astra.cameramap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener{

	 private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        this.getLocation();
    }

    public Location getLocation() {
        try {
            this.locationManager = (LocationManager) this.mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            this.isGPSEnabled = this.locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            this.isNetworkEnabled = this.locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!this.isGPSEnabled && !this.isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            GPSTracker.MIN_TIME_BW_UPDATES,
                            GPSTracker.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (this.locationManager != null) {
                        this.location = this.locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (this.isGPSEnabled) {
                    if (this.location == null) {
                        this.locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                GPSTracker.MIN_TIME_BW_UPDATES,
                                GPSTracker.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (this.locationManager != null) {
                            this.location = this.locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (this.location != null) {
                                this.latitude = this.location.getLatitude();
                                this.longitude = this.location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.location;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(this.location != null){
            this.latitude = this.location.getLatitude();
        }

        // return latitude
        return this.latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(this.location != null){
            this.longitude = this.location.getLongitude();
        }

        // return longitude
        return this.longitude;
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
