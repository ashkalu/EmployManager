package com.employmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class LoggedIn extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    protected LocationManager locationManager;
    private Button send_btn;
    private ImageView captured_image;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap bitmap;
    private EditText place_name;
    String imageIdentifier;
    String imageLink;
    private double latitude, longitude;
    FirebaseAuth mAuth;
    FusedLocationProviderClient client;
    private boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in2);
        mAuth = FirebaseAuth.getInstance();
        latitude = longitude = 0;
        bitmap = null;
        done = false;
        client = LocationServices.getFusedLocationProviderClient(this);

        send_btn = findViewById(R.id.send_btn);
        captured_image = findViewById(R.id.captured_image);
        place_name = findViewById(R.id.place_name);

        captured_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap == null)
                {
                    Toast.makeText(LoggedIn.this, "No image captured yet", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!place_name.getText().toString().isEmpty())
                    {
                        try {

                            checkForLocation();
                        }catch (Exception e)
                        {
                            Toast.makeText(LoggedIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(LoggedIn.this, "Correct place name must be entered.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {


        switch (item.getItemId())
        {
            case R.id.logout_btn :

                //    mAuth.signOut();
                FancyToast.makeText(LoggedIn.this, "Logged Out", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                mAuth.signOut();

                Intent intent = new Intent(this, Signin.class);
                startActivity(intent);
                finish();
                break;

            case R.id.editProfile :
                Intent intent1 = new Intent(this, EditMyDetails.class);
                startActivity(intent1);
                break;
       }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            captured_image.setImageBitmap(bitmap);
            place_name.setVisibility(View.VISIBLE);
        }
    }


    private void UploadToServer()
    {


        if(bitmap != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(LoggedIn.this);
            progressDialog.setMessage("Sending image");
            progressDialog.setCancelable(false);
            progressDialog.show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] data = baos.toByteArray();
            imageIdentifier = mAuth.getCurrentUser().getEmail() + "_" + UUID.randomUUID().toString() + ".png";

            final UploadTask uploadTask = FirebaseStorage.getInstance().getReference()
                    .child("Uploaded_Images")
                    .child(mAuth.getCurrentUser().getEmail())
                    .child(imageIdentifier).putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoggedIn.this, "Data Sent", Toast.LENGTH_LONG).show();
                                imageLink = task.getResult().toString();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_dd/MM/yyyy");
                                String timeAndDate = sdf.format(new Date());
                                HashMap<String, String> dataMap = new HashMap<>();

                                dataMap.put("imageIdentifier", imageIdentifier);
                                dataMap.put("imageLink", imageLink);
                                dataMap.put("latitude", latitude + "");
                                dataMap.put("longitude", longitude + "");
                                dataMap.put("timeAndDate", timeAndDate);
                                dataMap.put("placeName", place_name.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("Data_Sent").push().setValue(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void Void) {
//                                        send_btn.setEnabled(false);
                                        finish();
                                        progressDialog.cancel();
                                    }
                                });


                            }

                            else
                            {
                                FancyToast.makeText(LoggedIn.this, task.getException().getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, false);
                            }
                        }
                    });

                }
            });
        }

    }






    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }






    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(LoggedIn.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (LoggedIn.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions(LoggedIn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//            return false;
        }
        else
        {
            final boolean[] decision = new boolean[1];

            client.getLastLocation().addOnSuccessListener(LoggedIn.this, new OnSuccessListener<Location>()
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
                        Toast.makeText(LoggedIn.this, "Unable to get Location", Toast.LENGTH_SHORT).show();
                        decision[0] = false;
                    }
                }
            });

//            return decision[0];
        }
    }


    private void checkForLocation()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {

            getLocation();
//            getLocation2();

//            Toast.makeText(this, latitude + "", Toast.LENGTH_SHORT).show();
                UploadToServer();
        }
    }



}