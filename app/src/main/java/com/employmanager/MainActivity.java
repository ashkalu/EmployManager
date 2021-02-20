package com.employmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {

    private EditText signup_email, signup_password, signup_firstname, signup_lastname;
    private Button signup_btn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    public static int ID;

    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_firstname = findViewById(R.id.signup_firstname);
        signup_lastname = findViewById(R.id.signup_lastname);
        signup_btn = findViewById(R.id.signup_btn);
        user = new Users();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setTitle("Sign up");


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email, password, first_name, last_name;
                email = signup_email.getText().toString().trim();
                password = signup_password.getText().toString().trim();
                first_name = signup_firstname.getText().toString();
                last_name = signup_lastname.getText().toString();

                if(email.isEmpty())
                {
                    signup_email.setError("Email Required");
                }
                if (password.isEmpty())
                {
                    signup_password.setError("Password Required");
                }
                if (first_name.isEmpty())
                {
                    signup_firstname.setError("First Name Required");
                }
                if (last_name.isEmpty())
                {
                    signup_lastname.setError("Last Name Required");
                }

                try {
                    if(!email.isEmpty() && !password.isEmpty() && !last_name.isEmpty() && !first_name.isEmpty())
                    {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful())
                                {

                                   try
                                   {

                                       FancyToast.makeText(MainActivity.this, "User Registered and Logged in Successfully", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                       user.setEmail(signup_email.getText().toString());
                                       user.setName(signup_firstname.getText().toString() + " " + signup_lastname.getText().toString());
                                       user.setPassword(signup_password.getText().toString());

                                       FirebaseDatabase.getInstance().getReference().child(task.getResult().getUser().getUid()).setValue(user);

                                       UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                               .setDisplayName(user.getName())
                                               .build();

                                       FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               FancyToast.makeText(MainActivity.this, "Account Created for user : '" + user.getName() + "' Successfully.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false);

                                           }
                                       });


                                       AlreadyLoggedIn();

                                   }catch (Exception e)
                                   {
                                       Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                                }
                                else
                                {
                                    FancyToast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            }
                        });


                    }
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

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
        Intent intent = new Intent(MainActivity.this, LoggedIn.class);
        startActivity(intent);
        finish();
    }


    public void Already_Registered(View view) {
        Intent intent = new Intent(this, Signin.class);
        startActivity(intent);
    }
}