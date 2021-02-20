package com.employmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoggedIn1 extends AppCompatActivity {

    private Button send_btn, edit_btn, logout_btn;
    FusedLocationProviderClient client;
    private TextView my_name;

    private static final int REQUEST_LOCATION = 1;
    public static double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in1);

        send_btn = findViewById(R.id.post_btn);
        edit_btn = findViewById(R.id.edit_btn);
        logout_btn = findViewById(R.id.signout_btn);

        latitude = longitude = 0;

        my_name = findViewById(R.id.profile_name);
        my_name.setText("Hi, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoggedIn1.this, LoggedIn.class);
                startActivity(intent);
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoggedIn1.this, EditMyDetails.class);
                startActivity(intent);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoggedIn1.this);
                builder
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FancyToast.makeText(LoggedIn1.this, "Logged Out", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(LoggedIn1.this, Signin.class);
                        startActivity(intent);
                        finish();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setMessage("Do you really want to logout")
                        .setTitle("Confirm Logout")
                        .show();
            }
        });
    }


    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(LoggedIn1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (LoggedIn1.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions(LoggedIn1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//            return false;
        }
        else
        {
            final boolean[] decision = new boolean[1];

            client.getLastLocation().addOnSuccessListener(LoggedIn1.this, new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location) {
                    if (location != null)
                    {
                        latitude =  location.getLatitude();
                        longitude = location.getLongitude();
                        decision[0] =  true;
                    }
                    else
                    {
                        Toast.makeText(LoggedIn1.this, "Unable to get Location", Toast.LENGTH_SHORT).show();
                        decision[0] = false;
                    }
                }
            });

//            return decision[0];
        }
    }
}