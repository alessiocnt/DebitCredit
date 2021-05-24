package com.simoale.debitcredit.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.simoale.debitcredit.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LocationUtils {

    Activity activity;

    // Location variables
    private boolean requestingLocationUpdates = false;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    //callback that keep monitored the internet connection
    private ConnectivityManager.NetworkCallback networkCallback;
    private Boolean isNetworkConnected = false;
    private Snackbar snackbar;

    private RequestQueue requestQueue;
    private final static String OSM_REQUEST_TAG = "OSM_REQUEST";

    public LocationUtils(Activity activity) {
        this.activity = activity;
    }

    public void initializeLocation(TextView endPointTextView, ActivityResultLauncher<String> requestPermissionLauncher) {
        this.requestPermissionLauncher = requestPermissionLauncher;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        // Set the desired interval for active location updates, in milliseconds.
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Update UI with location data
                android.location.Location location = locationResult.getLastLocation();
                Log.e("lat", String.valueOf(location.getLatitude()));
                if (isNetworkConnected) {
                    //if internet connection is available, I can make the request
                    sendVolleyRequest(endPointTextView, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                    requestingLocationUpdates = false;
                    stopLocationUpdates();
                } else {
                    //if internet connection is not available, I'll show the user a snackbar
                    snackbar.show();
                }
            }
        };
    }

    public void setupNetwork() {
        snackbar = Snackbar.make(activity.findViewById(R.id.nav_host_fragment),
                "No Internet Available",
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(intent);
                        }
                    }
                });
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isNetworkConnected = true;
                snackbar.dismiss();
                if (requestingLocationUpdates) {
                    startLocationUpdates();
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isNetworkConnected = false;
                snackbar.show();
            }
        };
        requestQueue = Volley.newRequestQueue(activity);
    }

    /**
     * Method called to register the NetworkCallback in the ConnectivityManager (SDK >= N) or
     * to get info about the network with NetworkInfo (Android 6)
     */
    public void registerNetworkCallback() {
        Log.d("LAB", "registerNetworkCallback");
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            } else {
                //Class deprecated since API 29 (android 10) but used for android 6
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                isNetworkConnected = networkInfo != null && networkInfo.isConnected();
            }
        } else {
            isNetworkConnected = false;
        }
    }

    /**
     * Method called to start requesting the updates for the Location
     * It checks also the permission fo the Manifest.permission.ACCESS_FINE_LOCATION
     */
    public void startLocationUpdates() {
        final String PERMISSION_REQUESTED = Manifest.permission.ACCESS_FINE_LOCATION;
        //permission granted
        if (ActivityCompat.checkSelfPermission(activity, PERMISSION_REQUESTED) == PackageManager.PERMISSION_GRANTED) {
            statusGPSCheck();
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_REQUESTED)) {
            //if the permission was denied before
            showDialog();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(PERMISSION_REQUESTED);
        }
    }

    /**
     * Method called to query the OpenStreetMap API
     *
     * @param endPointTextView endpoint to set
     * @param latitude         latitude of the device
     * @param longitude        longitude of the device
     */
    private void sendVolleyRequest(TextView endPointTextView, String latitude, String longitude) {
        Log.e("aa", "1");
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude +
                "&lon=" + longitude + "&format=jsonv2&limit=1";

        // Request a jsonObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("ssss", "dentro");
                try {
                    Log.e("aa", "2");
                    endPointTextView.setText(response.get("name").toString());
                    unRegisterNetworkCallback();
                } catch (JSONException e) {
                    endPointTextView.setText("Error finding your current location");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Network", error.toString());
            }
        });

        jsonObjectRequest.setTag(OSM_REQUEST_TAG);
        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Method called to stop the updates for the Location
     */
    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Method called to unregister the NetworkCallback (SDK >= N) or
     * to dismiss the snackbar in Android 6 (it works only if the snackbar is still visible)
     */
    public void unRegisterNetworkCallback() {
        if (activity != null) {
            Log.d("LAB", "unRegisterNetworkCallback");
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        connectivityManager.unregisterNetworkCallback(networkCallback);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    snackbar.dismiss();
                }
            }
        }
    }

    /**
     * Method called to check the status of the GPS (on or off)
     * If the GPS is off, a dialog will be displayed to the user to turn it on
     */
    private void statusGPSCheck() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //if gps is off, show the alert message
            new AlertDialog.Builder(activity)
                    .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> activity.startActivity(
                            new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .create()
                    .show();
        }
    }

    /**
     * Method called to create a new Dialog to check the user for previously denied permission
     */
    public void showDialog() {
        new AlertDialog.Builder(activity)
                .setMessage("Permission was denied, but is needed for the gps functionality.")
                .setCancelable(false) //Sets whether this dialog is cancelable with the BACK key.
                .setPositiveButton("OK", (dialog, id) -> activity.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    public boolean isRequestingLocationUpdates() {
        return requestingLocationUpdates;
    }

    public void setRequestingLocationUpdates(boolean requestingLocationUpdates) {
        this.requestingLocationUpdates = requestingLocationUpdates;
    }
}
