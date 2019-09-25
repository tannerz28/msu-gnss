package edu.murraystate.gnssaccuracy;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView labelStart;
    private TextView locationGps;
    private TextView locationWifi;
    private TextView errorGps;
    private TextView errorWifi;
    private LocationManager locationManager;
    GnssStatus.Callback gnssStatusCallback;

    private static final String TAG = "GNSSAccuracy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labelStart = findViewById(R.id.labelStart);
        locationGps = findViewById(R.id.locationGps);
        locationWifi = findViewById(R.id.locationWifi);
        errorGps = findViewById(R.id.errorGps);
        errorWifi = findViewById(R.id.errorWifi);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        gnssStatusCallback = new GnssStatus.Callback() {
            @Override
            public void onStarted() { }

            @Override
            public void onStopped() { }

            @Override
            public void onFirstFix(int millis) { }

            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                Log.v(TAG, "GNSS Status: " + status.getSatelliteCount() + " satellites.");
            }
        };
    }

    public void onStartClick(View view) {
        setStartLabel();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0,this
        );
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 0, this
        );
    }

    public void setStartLabel() {
        String currentDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy 'at' hh:mm a"));
        String labelText = "Started on " + currentDateString;
        labelStart.setText(labelText);
    }

    private double getLocationMeasurement(Location location) {
        return Math.abs(location.getLongitude()) + Math.abs(location.getLatitude()) + Math.abs(location.getAltitude());
    }

    @Override
    public void onLocationChanged(Location location) {
        switch (location.getProvider()) {
            case LocationManager.NETWORK_PROVIDER:
                locationWifi.setText(Double.toString(getLocationMeasurement(location)));
                break;
            case LocationManager.GPS_PROVIDER:
                locationGps.setText(Double.toString(getLocationMeasurement(location)));
                break;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
