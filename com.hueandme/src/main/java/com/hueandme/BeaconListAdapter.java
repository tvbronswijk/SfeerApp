package com.hueandme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

public class BeaconListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Beacon> mBeacons = new ArrayList<>();

    private class BeaconListItem {
        private TextView mId;
        private TextView mDistance;
    }

    public BeaconListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        BeaconListItem item;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);

            item = new BeaconListItem();
            item.mId = (TextView) convertView.findViewById(android.R.id.text1);
            item.mDistance = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(item);
        } else {
            item = (BeaconListItem) convertView.getTag();
        }
        Beacon beacon = mBeacons.get(position);
        item.mId.setTextColor(Color.BLACK);
        item.mId.setText(beacon.getId1().toString());
        item.mDistance.setTextColor(Color.DKGRAY);
        item.mDistance.setText(String.valueOf(beacon.getDistance()));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mBeacons.size();
    }

    @Override
    public Beacon getItem(int position) {
        return mBeacons.get(position);
    }

    public void addItem(Beacon beacon) {
        mBeacons.remove(beacon);
        mBeacons.add(beacon);
        notifyDataSetChanged();
    }

    public void removeItem(Beacon beacon) {
        mBeacons.remove(beacon);
        notifyDataSetChanged();
    }

}