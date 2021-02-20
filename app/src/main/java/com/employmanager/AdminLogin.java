package com.employmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AdminLogin extends AppCompatActivity {

    EditText admin_email, admin_password;
    Button admin_signin_btn, userLogin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        admin_email = findViewById(R.id.admin_email);
        admin_password = findViewById(R.id.admin_password);
        admin_signin_btn = findViewById(R.id.admin_signin_btn);
        userLogin_btn = findViewById(R.id.login_btn1);

        admin_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(admin_email.getText().toString(), admin_password.getText().toString()))
                {
                    Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                    startActivity(intent);
                }
            }
        });

        userLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminLogin.this, Signin.class);
                startActivity(intent);
                finish();
            }
        });


    }

    boolean check(String email, String password)
    {
        if (email.equals("admin") && password.equals("admin"))
        {
            return true;
        }
        if (!email.equals("admin"))
        {
            admin_email.setError("Incorrect Email");
        }
        if (!password.equals("admin"))
        {
            admin_password.setError("Incorrect Password");
        }
        return false;
    }
}
