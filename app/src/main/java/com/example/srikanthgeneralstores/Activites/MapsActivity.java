package com.example.srikanthgeneralstores.Activites;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.srikanthgeneralstores.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    int counter=0;

    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
public void CenterMapOnLoc(Location location,String title)
{
    if(location!=null) {
        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLoc).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 15));
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    CenterMapOnLoc(lastKnownLoc,"Your Location");
                }
            }
        }
    }
    public void setCurrLoc(View view)
    {
        Intent intent=new Intent(this,CategoryActivity.class);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&  CategoryActivity.newLoc!=null &&  CategoryActivity.newAdd!=null) {
                      intent.putExtra("newLoc",1);

        }else
        {
            intent.putExtra("newLoc",0);
        }
        startActivity(intent);
        finish();

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(counter<2) {
                    counter++;
                    Log.i("LocationMy", location.toString());
                    LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    CenterMapOnLoc(location, "Your Location");
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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLoc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            CenterMapOnLoc(lastKnownLoc,"Your Location");
        }
        // Add a marker in Sydney and move the camera

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        String address="";
        Geocoder geocoder=new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList!=null && addressList.size()>0)
            {

                if(addressList.get(0).getAddressLine(0)!=null)
                {
                    address+=addressList.get(0).getAddressLine(0)+" ";
                }

                Log.i("Loc",address);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     CategoryActivity.newLoc= new LatLng(latLng.latitude,latLng.longitude);
        CategoryActivity.newAdd=address;
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}