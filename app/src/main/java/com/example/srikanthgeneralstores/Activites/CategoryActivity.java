package com.example.srikanthgeneralstores.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.srikanthgeneralstores.Fragments.MainFragment;
import com.example.srikanthgeneralstores.Fragments.MyCartFragment;
import com.example.srikanthgeneralstores.Fragments.MyOrdersFragment;
import com.example.srikanthgeneralstores.Fragments.SearchFragment;
import com.example.srikanthgeneralstores.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryActivity extends AppCompatActivity {
TextView CurrLoc;
   FirebaseUser user;
    LocationManager locationManager;
    int counter=0;
    LocationListener locationListener;
   public static LatLng newLoc=null;
    public static String newAdd=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
  DrawerLayout draw;
    Toolbar mToolbar;
    private MainFragment mainFragment;
    private MyCartFragment myCartFragment;
    private MyOrdersFragment myOrdersFragment;
    private SearchFragment searchFragment;


    //  private ActionBarDrawerToggle action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        draw=findViewById(R.id.drawer);
        mainFragment=new MainFragment();
        myCartFragment=new MyCartFragment();
        searchFragment=new SearchFragment();
        myOrdersFragment=new MyOrdersFragment();
        setFragment(mainFragment);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        bottomNavigationView.setItemBackgroundResource(R.color.color);
                        setFragment(mainFragment);
                        return true;
                    case R.id.menu_search:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(searchFragment);
                        return true;
                    case R.id.my_cart:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(myCartFragment);
                        return true;
                    case R.id.menu_order:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(myOrdersFragment);
                        return true;
                    default:bottomNavigationView.setItemBackgroundResource(R.color.colors);
                        return false;
                }
            }
        });

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        draw=findViewById(R.id.drawer);
//        action=new ActionBarDrawerToggle(this,draw,R.string.open,R.string.close);
//        draw.addDrawerListener(action);
//        action.syncState();
        setActionBar(mToolbar);
       // draw.isDrawerOpen(Gravity.LEFT);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
public void ClickMenu(View view)
{
    openDrawer(draw);
}

    public static void openDrawer(DrawerLayout draw) {
        draw.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view)
    {
        closeDrawer(draw);
    }

    public static void closeDrawer(DrawerLayout draw) {
if(draw.isDrawerOpen(GravityCompat.START))
{
    draw.closeDrawer(GravityCompat.START);
}
    }
    public void ClickHome(View view)
    {
        recreate();
    }
    public void ClickSettings(View view)
    {
       redirectActivity(this,SettingActivity.class);
    }
    public void ClickHelp(View view)
    {
        redirectActivity(this,HelpActivity.class);
    }

    public static  void redirectActivity(Activity activity, Class aclass) {
        Intent intent=new Intent(activity,aclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(draw);
    }

    public void Logout(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (action.onOptionsItemSelected(item)) {
//            switch (item.getItemId()) {
//
//                case R.id.help:
//                    Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
//
//                    return true;
//                case R.id.logout:
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.home:
//                    Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.settings:
//                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//                    Toast.makeText(getApplicationContext(), item.getItemId()+"inside"+R.id.help, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public void goToMaps(View view) {
        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }
}