package edu.murraystate.gnssaccuracy;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private TextView labelStart;
    private TextView locationGps;
    private TextView locationWifi;
    private TextView errorGps;
    private TextView errorWifi;
    private LocationManager locationManager;

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

        this.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
    }

    public void onStartClick(View view) {
        setStartLabel();
        getGpsLocation();
        getWifiLocation();
    }

    public void setStartLabel() {
        String currentDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy 'at' hh:mm a"));
        String labelText = "Started on " + currentDateString;
        labelStart.setText(labelText);
    }

    public void getGpsLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationGps.setText(formatMeasurement(getLocationMeasurement(gpsLocation)));
    }

    public void getWifiLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location wifiLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationWifi.setText(formatMeasurement(getLocationMeasurement(wifiLocation)));
    }

    private double getLocationMeasurement(Location location) {
        return Math.abs(location.getLongitude()) + Math.abs(location.getLatitude()) + Math.abs(location.getTime());
    }

    private String formatMeasurement(double measurement) {
        return new DecimalFormat("#.######").format(measurement);
    }
}
