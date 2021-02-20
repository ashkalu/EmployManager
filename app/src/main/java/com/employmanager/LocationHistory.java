package com.employmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationHistory extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> list;
    int UserPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        list = new ArrayList<>();

        Intent intent = getIntent();

        UserPos = intent.getIntExtra("UserPos", 0);


//        Toast.makeText(this, AdminHome.allEmployeesList.get(UserPos).size() + "", Toast.LENGTH_SHORT).show();

        for(int i=0; i<AdminHome.allEmployeesList.get(UserPos).size(); i++)
        {
            list.add(AdminHome.allEmployeesList.get(UserPos).get(i).getTimeAndDate());
        }

//        Toast.makeText(this, list.size() + "", Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.historyListView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);



        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(LocationHistory.this, MapsActivity.class);

                intent1.putExtra("latitude", Double.parseDouble(AdminHome.allEmployeesList.get(UserPos).get(position).getLatitude()));
                intent1.putExtra("longitude", Double.parseDouble(AdminHome.allEmployeesList.get(UserPos).get(position).getLongitude()));
                intent1.putExtra("user", AdminHome.usernames.get(UserPos));
                intent1.putExtra("timeAndDate", AdminHome.allEmployeesList.get(UserPos).get(position).getTimeAndDate());
                intent1.putExtra("imageLink", AdminHome.allEmployeesList.get(UserPos).get(position).getImageLink());
                intent1.putExtra("placeName", AdminHome.allEmployeesList.get(UserPos).get(position).getPlaceName());
                startActivity(intent1);
            }
        });

    }
}
