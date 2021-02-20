package com.employmanager;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String user, timeAndDate, imageLink, placeName;
    private double latitude, longitude;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = findViewById(R.id.floatingActionButton);

        Intent intent = getIntent();
        longitude = intent.getDoubleExtra("longitude", 0);
        latitude = intent.getDoubleExtra("latitude", 0);
        user = intent.getStringExtra("user");
        timeAndDate = intent.getStringExtra("timeAndDate");
        imageLink = intent.getStringExtra("imageLink");
        placeName = intent.getStringExtra("placeName");
       // Bitmap bitmap = getBitmapFromURL(imageLink);
        // imageView.setImageBitmap(bitmap);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(MapsActivity.this, UserPic.class);
                intent1.putExtra("imageLink", imageLink);
                intent1.putExtra("placeName", placeName);
                startActivity(intent1);
            }
        });
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng user_loc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(user_loc).title(user + "'s location").snippet("at " + timeAndDate));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_loc));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(user_loc);
        circleOptions.radius(100);
        circleOptions.strokeWidth(20.0f);
        circleOptions.strokeColor(Color.GREEN);
        mMap.addCircle(circleOptions);


    }
/*
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

 */
}
