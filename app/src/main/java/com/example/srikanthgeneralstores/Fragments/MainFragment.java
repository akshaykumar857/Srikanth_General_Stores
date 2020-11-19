package com.example.srikanthgeneralstores.Fragments;

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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.srikanthgeneralstores.Activites.MapsActivity;
import com.example.srikanthgeneralstores.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.srikanthgeneralstores.Activites.CategoryActivity.newAdd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView CurrLoc;
    FirebaseUser user;
    Boolean geo=false;
    LocationManager locationManager;
    int counter=0;
    LocationListener locationListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private DrawerLayout draw;
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    public void findLoc(Location location)
    {
        if(geo) {
            try {
                Geocoder geocoder = new Geocoder(Objects.requireNonNull(getActivity()).getBaseContext(), Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList != null && addressList.size() > 0) {
                    String address = "";


                    if (addressList.get(0).getAddressLine(0) != null) {
                        address += addressList.get(0).getAddressLine(0) + " ";
                    }
                    Log.i("Loc", address);

                    CurrLoc.setText(address);
                    geo=false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
    }

    public void goToMaps(View view) {
        Intent intent=new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_main, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        CurrLoc=(TextView)view.findViewById(R.id.CurrLoc);
        // locs.add(new LatLng(0,0));
        Intent intent=getActivity().getIntent();
        if(intent.getIntExtra("newLoc",0)==0) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (counter < 2) {
                        counter++;
                        Log.i("LocationMy", location.toString());
                        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        geo=true;
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

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                geo=true;
                findLoc(lastKnownLoc);
            }
        }
        else {

            CurrLoc.setText(newAdd);
        }

        return view;
    }
}