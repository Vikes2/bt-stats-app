package com.github.vikes2.bt_stat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BoundedDevicesAdapter extends BaseAdapter {

    private List<BoundedDevice> mBoundedDeviceList;

    public BoundedDevicesAdapter(List<BoundedDevice> mBoundedDeviceList) {
        this.mBoundedDeviceList = mBoundedDeviceList;

    }

    @Override
    public int getCount() {
        return mBoundedDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBoundedDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoundedDeviceHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bounded_device_spinner, parent, false);

            holder = new BoundedDeviceHolder();
            holder.mName = convertView.findViewById(R.id.name_bd_spinner);
            holder.mMac = convertView.findViewById(R.id.mac_bd_spinner);
            convertView.setTag(holder);
        } else {
            holder = (BoundedDeviceHolder)convertView.getTag();
        }


        BoundedDevice boundedDevice = mBoundedDeviceList.get(position);
        holder.mName.setText(boundedDevice.getName());
        holder.mMac.setText(boundedDevice.getMac());

        return convertView;
    }

    class BoundedDeviceHolder{
        private TextView mName;
        private TextView mMac;
    }
}
