package com.employmanager.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.employmanager.R;
import com.employmanager.Signin;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Signin.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

//    public void OpenLinkInBrowser(View view) {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cslearners.com"));
//        startActivity(browserIntent);
//    }
}
