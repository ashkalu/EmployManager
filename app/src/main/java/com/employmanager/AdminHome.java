package com.employmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {


    private static Dialog help;
    private static EditText addUserName, addUserEmail, addUserID, addUserPassword;
    private ListView users_list;
    public static ArrayList <String> usernames, IDs, emails, passwords, AuthIDs;
    public static ArrayList <DataSnapshot> dataSnapshots;
    private MyAdapter adapter;
    private FirebaseAuth mAuth;
    private ArrayList <OtherValues> employeeHistoryArrayList;
    public static ArrayList< ArrayList <OtherValues> > allEmployeesList;
    private int uniqueID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

            employeeHistoryArrayList = new ArrayList<>();
            mAuth = FirebaseAuth.getInstance();
            emails = new ArrayList<>();
            users_list = findViewById(R.id.regUsersList);
            usernames = new ArrayList<>();
            IDs = new ArrayList<>();
            dataSnapshots = new ArrayList<>();
            passwords = new ArrayList<>();
            AuthIDs = new ArrayList<>();
            allEmployeesList = new ArrayList<>();


            adapter = new MyAdapter(this);
            users_list.setAdapter(adapter);


            users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(AdminHome.this, UserDetails.class);
                    intent.putExtra("position", position);
                    startActivity(intent);

                }
            });


        try {


            final DatabaseReference databaseReference = null;


            FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                    dataSnapshots.add(dataSnapshot);
                    String username = (String) dataSnapshot.child("name").getValue();
              //      String lat = String.valueOf(dataSnapshot.child("Data_Sent").child("latitude").getValue());
                //    String lon = String.valueOf(dataSnapshot.child("Data_Sent").child("longitude").getValue());
                    String ID = String.valueOf(dataSnapshot.child("id").getValue());
                    String email = String.valueOf(dataSnapshot.child("email").getValue());
                    String password = String.valueOf(dataSnapshot.child("password").getValue());
                    String AuthID = String.valueOf(dataSnapshot.getKey());

    //                String timeAndDate = String.valueOf(dataSnapshot.child("Data_Sent").child("timeAndDate").getValue());


/*

                    DataSnapshot d = (DataSnapshot) dataSnapshot.child("Data_Sent").getChildren();
                    employeeHistoryArrayList.add(d.getValue(EmployeeHistory.class));



 */
/*
                    if (lat.equals("") || lon.equals(""))
                    {
                        lat = "0";
                        lon = "0";
                    }


 */
//                    location_latitudes.add(Double.parseDouble(lat));
  //                  location_longitudes.add(Double.parseDouble(lon));
                    usernames.add(username);
                    IDs.add(ID);
                    passwords.add(password);
                    emails.add(email);
                    AuthIDs.add(AuthID);
      //              timeAndDates.add(timeAndDate);
                    adapter.notifyDataSetChanged();
/*
                    EmployeeHistory employeeHistory = new EmployeeHistory();
                    employeeHistory.setTimeAndDate(String.valueOf(dataSnapshot.child("Data_Sent").child("timeAndDate").getValue()));
                    employeeHistory.setImageIdentifier(String.valueOf(dataSnapshot.child("Data_Sent").child("imageIdentifier").getValue()));
                    employeeHistory.setImageLink(String.valueOf(dataSnapshot.child("Data_Sent").child("imageLink").getValue()));
                    employeeHistory.setLatitude(String.valueOf(dataSnapshot.child("Data_Sent").child("latitude").getValue()));
                    employeeHistory.setLongitude(String.valueOf(dataSnapshot.child("Data_Sent").child("longitude").getValue()));
                    employeeHistoryArrayList.add(employeeHistory);


 */

                    Toast.makeText(AdminHome.this, String.valueOf(dataSnapshot.child("Data_Sent").getChildrenCount()), Toast.LENGTH_SHORT).show();



                    DatabaseReference databaseReference = dataSnapshot.child("Data_Sent").getRef();




                    employeeHistoryArrayList = new ArrayList<>();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                            for(DataSnapshot d: dataSnapshot1.getChildren())
                            {
                                employeeHistoryArrayList.add(d.getValue(OtherValues.class));
                            }

                            allEmployeesList.add(employeeHistoryArrayList);
                            employeeHistoryArrayList = new ArrayList<>();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(AdminHome.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        } catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);
            builder.setMessage(e.getMessage());
            builder.show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId())
        {

            case R.id.addUser :


                addUsers(this);

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void addUsers(Context context)
    {

        help = new Dialog(context);
        help.setContentView(R.layout.add_new_user);
        help.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        help.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        help.show();

        addUserID = help.findViewById(R.id.addUserID);
        uniqueID = maxID()+1;
        addUserID.setInputType(0);
        addUserID.setEnabled(false);
        addUserID.setText("ID = " + uniqueID);
    }

    private void CreateUsers()
    {


        addUserName = help.findViewById(R.id.addUserName);
        addUserEmail = help.findViewById(R.id.addUserEmail);
        addUserPassword = help.findViewById(R.id.addUserPassword);


        final String newUserName = addUserName.getText().toString();
        String newUserID = addUserID.getText().toString();
        String newUserEmail = addUserEmail.getText().toString();
        String newUserPassword = addUserPassword.getText().toString();



        if(IDAlreadyExist(newUserID))
        {
            addUserID.setError("ID already Exists");
        }
        if(newUserName.isEmpty())
        {
            addUserName.setError("Required");
        }
        if (newUserEmail.isEmpty())
        {
            addUserEmail.setError("Required");
        }
        if (newUserPassword.isEmpty())
        {
            addUserPassword.setError("Required");
        }
        if(!newUserName.isEmpty() && !newUserEmail.isEmpty() && !newUserPassword.isEmpty())
        {
            final Users user = new Users();
            user.setName(newUserName);
            user.setID(String.valueOf(uniqueID));
            user.setEmail(newUserEmail);
            user.setPassword(newUserPassword);
            final OtherValues otherValues = new OtherValues();
            otherValues.setImageIdentifier("N/A");
            otherValues.setImageLink("N/A");
            otherValues.setLatitude("0");
            otherValues.setLongitude("0");
            otherValues.setTimeAndDate("N/A");
            otherValues.setPlaceName("Not Entered");

            mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        help.dismiss();

                        FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newUserName).build();

                        Fuser.updateProfile(profileUpdates);

                        FancyToast.makeText(AdminHome.this, "User Created Successfully", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                        mAuth.signOut();
                    //    FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Data_Sent").setValue(otherValues);

                    } else {
                        FancyToast.makeText(AdminHome.this, task.getException().getMessage(), Toast.LENGTH_LONG, FancyToast.WARNING, false).show();
                    }
                }
            });

        }

    }

    public void CreateNewUser(View view) {
       // try {



            CreateUsers();
        //}catch (Exception e)
        //{
          //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //}
    }

    private boolean IDAlreadyExist(String id)
    {
        for(String checkID: IDs)
        {
            if (checkID.equals(id))
            {
                return true;
            }
        }

        return false;
    }

    private int maxID()
    {
        int mMaxID = 0;

        for(String checkID: IDs)
        {
            if (!checkID.isEmpty())
            {
                if (Integer.parseInt(checkID) > mMaxID)
                {
                    mMaxID = Integer.parseInt(checkID);
                }
            }
        }

        return mMaxID;
    }
}