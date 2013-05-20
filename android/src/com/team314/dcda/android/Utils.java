package com.team314.dcda.android;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Utils {

	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String TAG = "Utils";

	
	public static String getAdminLocation(Context context)
	{
        
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        Location locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        List<String>  providerList = locationManager.getAllProviders();
        if(null!=locations && null!=providerList && providerList.size()>0)
        {                 
	        double longitude = locations.getLongitude();
	        double latitude = locations.getLatitude();
	        Geocoder geocoder = new Geocoder(context, Locale.US);                 
	        try 
	        {
	            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
	            if(null!=listAddresses&&listAddresses.size()>0)
	            {
	                String locality = listAddresses.get(0).getAdminArea();
	                Log.d(TAG, "Location is" + locality);
	    			//return locality;
	                return "Timis";
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

        }
		return "Timisoara";
	}
	
	
	public static void createAlertDialog(Context context, String title, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {					 
		    	  dialog.dismiss();
		    } });
		alertDialog.show();
	}
}
