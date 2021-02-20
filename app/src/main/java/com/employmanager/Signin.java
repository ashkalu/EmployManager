package com.employmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Signin extends AppCompatActivity {

    EditText signin_email, signin_password;
    Button signin_btn, admin_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);
        signin_email = findViewById(R.id.signin_email);
        signin_password = findViewById(R.id.singin_password);
        signin_btn = findViewById(R.id.singnin_btn);
        admin_btn = findViewById(R.id.admin_btn);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = signin_email.getText().toString();
                password = signin_password.getText().toString();

                if(signin_email.getText().toString().isEmpty())
                {
                    signin_email.setError("Email Required");
                }
                if (signin_password.getText().toString().isEmpty())
                {
                    signin_password.setError("Password Required");
                }

                if(!email.isEmpty() && !password.isEmpty())
                {
                   mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful())
                           {
                               FancyToast.makeText(Signin.this, "Logged in", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                               Intent intent = new Intent(Signin.this, LoggedIn1.class);
                               finishAffinity();
                               startActivity(intent);
                               finish();

                           }
                           else
                           {
                               FancyToast.makeText(Signin.this, task.getException().getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                           }
                       }
                   });


                }


            }
        });
        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, AdminLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            AlreadyLoggedIn();
        }
    }

    private void AlreadyLoggedIn()
    {
        Intent intent = new Intent(Signin.this, LoggedIn1.class);
        startActivity(intent);
        finish();
    }


}
