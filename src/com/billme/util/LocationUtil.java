package com.billme.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtil {
	LocationManager locationManager;

	public LocationUtil(Context context) {
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context
				.getSystemService(serviceName);
	}

	public Location getLocationByGPS(Context context) {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return location;
	}

	public Location getLocationByNetwork(Context context) {
		String serviceName = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(serviceName);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		return location;
	}

	private LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

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
	};
}
