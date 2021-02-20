package com.employmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class UserDetails extends AppCompatActivity {

    EditText newUserName, newUserID, newUserEmail, newUserPassword;
    Button UserLastLocation;
    ImageButton EditName_btn;
    int pos;

    Menu get_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        newUserName = findViewById(R.id.newUserName);
        newUserName.setEnabled(false);
        newUserID = findViewById(R.id.newID);
        newUserID.setEnabled(false);
        newUserEmail = findViewById(R.id.newEmail);
        newUserEmail.setEnabled(false);
        newUserPassword = findViewById(R.id.newPassword);
        newUserPassword.setEnabled(false);
        UserLastLocation = findViewById(R.id.lastLocation_btn);
        EditName_btn = findViewById(R.id.EditName_btn);


        final Intent intent = getIntent();

        pos = intent.getIntExtra("position", 0);

        newUserName.setText(AdminHome.usernames.get(pos));
        newUserID.setText(AdminHome.IDs.get(pos));
        newUserEmail.setText(AdminHome.emails.get(pos));
        newUserPassword.setText(AdminHome.passwords.get(pos));


        UserLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(AdminHome.location_latitudes.get(pos) != 0 && AdminHome.location_longitudes.get(pos) != 0)
                {
                    Intent maps = new Intent(UserDetails.this, MapsActivity.class);
                    maps.putExtra("latitude", AdminHome.location_latitudes.get(pos));
                    maps.putExtra("longitude", AdminHome.location_longitudes.get(pos));
                    maps.putExtra("user", AdminHome.usernames.get(pos));
                    maps.putExtra("timeAndDate", AdminHome.timeAndDates.get(pos));
                    startActivity(maps);
                }
                else
                {
                    Toast.makeText(UserDetails.this, "User has not sent any location yet !!", Toast.LENGTH_SHORT).show();
                }

                 */
                Intent intent1 = new Intent(UserDetails.this, LocationHistory.class);
                intent1.putExtra("UserPos", pos);
                startActivity(intent1);
            }
        });



        EditName_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserName.setEnabled(true);
                TextChanged(newUserName);
            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save_btn_menu, menu);

        get_menu = menu;
        get_menu.getItem(0).setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId())
        {

            case R.id.SaveBtn :

                FirebaseDatabase.getInstance().getReference().child("users").child(String.valueOf(AdminHome.dataSnapshots.get(pos).getKey())).child("name").setValue(newUserName.getText().toString());
          //      FirebaseDatabase.getInstance().getReference().child("users").child(String.valueOf(AdminHome.dataSnapshots.get(pos).getKey())).child("id").setValue(newUserID.getText().toString());
                newUserName.setEnabled(false);
                newUserID.setEnabled(false);
                get_menu.getItem(0).setEnabled(false);
                FancyToast.makeText(UserDetails.this, "Changes Saved", Toast.LENGTH_LONG, FancyToast.SUCCESS, false);
                break;
        }


        return super.onOptionsItemSelected(item);
    }



    private void TextChanged(EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                get_menu.getItem(0).setEnabled(true);
            }
        });
    }

}