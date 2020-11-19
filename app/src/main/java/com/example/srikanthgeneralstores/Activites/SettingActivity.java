package com.example.srikanthgeneralstores.Activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.srikanthgeneralstores.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        drawerLayout=findViewById(R.id.drawer);
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

    @Override
    protected void onPause() {
        super.onPause();
        CategoryActivity.closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view)
    {
        CategoryActivity.openDrawer(drawerLayout);
    }
    public void ClickSettings(View view)
    {
        recreate();
    }
    public void ClickLogo(View view)
    {
        CategoryActivity.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view)
    {
        CategoryActivity.redirectActivity(this, CategoryActivity.class);
    }

    public void ClickHelp(View view)
    {
        CategoryActivity.redirectActivity(this,HelpActivity.class);
    }
}