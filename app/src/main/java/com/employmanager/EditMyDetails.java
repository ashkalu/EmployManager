package com.employmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class EditMyDetails extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText my_name, my_email, my_password, confirm_my_password;
    Button update_profile, change_pass;
    boolean changePassPressed;
    TextView textView12, textView14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_details);
        mAuth = FirebaseAuth.getInstance();

        changePassPressed = false;
        my_name = findViewById(R.id.my_name);
        my_email = findViewById(R.id.my_email);
        my_password = findViewById(R.id.my_password);
        confirm_my_password = findViewById(R.id.confirm_my_password);
        update_profile = findViewById(R.id.update_btn);
        change_pass = findViewById(R.id.change_pass);
        textView12 = findViewById(R.id.textView12);
        textView14 = findViewById(R.id.textView14);

        textView12.setVisibility(View.INVISIBLE);
        textView14.setVisibility(View.INVISIBLE);
        my_password.setVisibility(View.INVISIBLE);
        confirm_my_password.setVisibility(View.INVISIBLE);

        my_name.setText(mAuth.getCurrentUser().getDisplayName());
        my_email.setText(mAuth.getCurrentUser().getEmail());

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_password.setVisibility(View.VISIBLE);
                confirm_my_password.setVisibility(View.VISIBLE);
                textView12.setVisibility(View.VISIBLE);
                textView14.setVisibility(View.VISIBLE);

                change_pass.setVisibility(View.INVISIBLE);
                changePassPressed = true;
            }
        });
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (changePassPressed)
                {
                    if (my_password.getText().toString().isEmpty())
                    {
                        my_password.setError("Can't be Empty");
                    }
                    if (confirm_my_password.getText().toString().isEmpty())
                    {
                        my_password.setError("Can't be Empty");
                    }
                    if (!my_password.getText().toString().isEmpty() && my_password.getText().toString().length() < 8)
                    {
                        my_password.setError("minimum 8 characters required");
                    }
                    if (!confirm_my_password.getText().toString().isEmpty() && confirm_my_password.getText().toString().length() < 8)
                    {
                        confirm_my_password.setError("minimum 8 characters required");
                    }
                    if (!my_password.getText().toString().isEmpty() && my_password.getText().toString().length() >= 8 && !confirm_my_password.getText().toString().isEmpty() && confirm_my_password.getText().toString().length() >= 8 && my_password.getText().toString().equals(confirm_my_password.getText().toString()))
                    {
                        updateName();
                        updateMyProfile(my_email.getText().toString(), my_password.getText().toString());
                    }
              //      if (!my_password.getText().toString().isEmpty() && my_password.getText().toString().length() >= 8 && !confirm_my_password.getText().toString().isEmpty() && confirm_my_password.getText().toString().length() >= 8 && !my_password.equals(confirm_my_password))
                //    {
               //         Toast.makeText(EditMyDetails.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
               //     }
                }
                if(!changePassPressed)
                {
                    mAuth.getCurrentUser().updateEmail(my_email.getText().toString());
                    updateName();
                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(my_email.getText().toString());
                }
            }
        });
    }


    private void updateMyProfile(final String email, final String pass)
    {
        mAuth.getCurrentUser().updateEmail(email);
        mAuth.getCurrentUser().updatePassword(pass)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(EditMyDetails.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditMyDetails.this, "Log in again with new credentials", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(email);
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("password").setValue(pass);
                mAuth.signOut();

                Intent intent = new Intent(EditMyDetails.this, Signin.class);
                startActivity(intent);
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(EditMyDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateName()
    {
        FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(my_name.getText().toString())
                .build();
        Fuser.updateProfile(profileUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("name").setValue(my_name.getText().toString());
                Toast.makeText(EditMyDetails.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditMyDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        Fuser.updateEmail(my_email.getText().toString());
    }
}
