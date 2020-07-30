package com.example.srikanthgeneralstores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class CategoryActivity extends AppCompatActivity {
TextView CurrLoc;
   FirebaseUser user;
    LocationManager locationManager;
    int counter=0;
    LocationListener locationListener;
    static LatLng newLoc=null;
    static String newAdd=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private DrawerLayout draw;
    Toolbar mToolbar;
    private ActionBarDrawerToggle action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
        draw=findViewById(R.id.drawer);
        action=new ActionBarDrawerToggle(this,draw,R.string.open,R.string.close);
        draw.addDrawerListener(action);
        action.syncState();
        setActionBar(mToolbar);
       // draw.isDrawerOpen(Gravity.LEFT);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user=FirebaseAuth.getInstance().getCurrentUser();
        CurrLoc=findViewById(R.id.CurrLoc);
       // locs.add(new LatLng(0,0));
        Intent intent=getIntent();
        if(intent.getIntExtra("newLoc",0)==0) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (counter < 2) {
                        counter++;
                        Log.i("LocationMy", location.toString());
                        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        findLoc(location);
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
                Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                findLoc(lastKnownLoc);
            }
        }
        else {
            CurrLoc.setText(newAdd);
        }

    }

public void findLoc(Location location)
{
    Geocoder geocoder=new Geocoder(getBaseContext(), Locale.getDefault());

    try {
        List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        if(addressList!=null && addressList.size()>0)
        {
            String address="";


            if(addressList.get(0).getAddressLine(0)!=null)
            {
                address+=addressList.get(0).getAddressLine(0)+" ";
            }

            Log.i("Loc",address);
           CurrLoc.setText(address);
           // SpannableStringBuilder ssb = new SpannableStringBuilder(address);
          //  ssb.setSpan(new ImageSpan(this, R.drawable.pencil), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
          //  CurrLoc.setText(ssb, TextView.BufferType.SPANNABLE);
         //   CurrLoc.setCompoundDrawables(R.drawable.pencil,0,0,0);
        }
    } catch (Exception e) {
        e.printStackTrace();
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
                }
            }
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.sidebar,menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(action.onOptionsItemSelected(item))
        {
            return true;
        }
    /*    switch(item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            return  true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void goToMaps(View view) {
        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }
}