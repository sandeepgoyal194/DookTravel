package com.softmine.dooktravel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.SideMenuItem;
import com.softmine.dooktravel.util.Utils;

import java.util.ArrayList;

public class AdapterSideMenu extends BaseAdapter {


    private final LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<SideMenuItem> sideMenuItems;

    public AdapterSideMenu(Activity activity, ArrayList<SideMenuItem> sideMenuItems) {
        this.activity = activity;
        this.sideMenuItems = sideMenuItems;
        mInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return sideMenuItems.size();
    }

    @Override
    public SideMenuItem getItem(int position) {
        return sideMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.side_item_menu, parent, false);
        }

        TextView tvMenuName = (TextView)convertView.findViewById(R.id.tvMenuName);
        tvMenuName.setText(activity.getResources().getString(getItem(position).getNameResourse()));
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        ivIcon.setImageResource((getItem(position).getImageNameResource()));
        tvMenuName.setTypeface(Utils.getRegularTypeFace(activity));
        return convertView;
    }
}
