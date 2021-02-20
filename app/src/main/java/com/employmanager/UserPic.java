package com.employmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserPic extends AppCompatActivity {

    ImageView user_Pic;
    TextView place_name_text;
    String imageLink, placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pic);
        user_Pic = findViewById(R.id.user_pic);
        place_name_text = findViewById(R.id.place_name_text);

        Intent intent = getIntent();
        imageLink = intent.getStringExtra("imageLink");
        placeName = intent.getStringExtra("placeName");


        Picasso.get().load(imageLink).into(user_Pic);
        place_name_text.setText(placeName);
    }
}
