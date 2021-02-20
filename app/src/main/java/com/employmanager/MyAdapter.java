package com.employmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return AdminHome.usernames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        convertView = inflater.inflate(R.layout.item_layout, null);

        TextView regUserName = convertView.findViewById(R.id.regUserName);
        TextView regUserID = convertView.findViewById(R.id.regUserID);

        regUserName.setText(AdminHome.usernames.get(position));
        regUserID.setText(AdminHome.IDs.get(position));

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.my_animation);
        animation.setDuration(300);
        convertView.startAnimation(animation);
        animation = null;


        return convertView;
    }
}
