package com.github.vikes2.bt_stat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>{

    private OnItemClickListener mListener;
    private ArrayList<DeviceBT> mDeviceList;
    public DeviceAdapter( ArrayList<DeviceBT> list){mDeviceList = list;}


    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.devices_favourite_item, viewGroup, false);
        DeviceViewHolder rvh = new DeviceViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i) {
        DeviceBT currentItem = mDeviceList.get(i);

        deviceViewHolder.mNameView.setText(currentItem.getName());
        deviceViewHolder.mMacView.setText(currentItem.getMac());
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder{

        public TextView mNameView;
        public TextView mMacView;
        public Button mEditButton;
        public Button mDeleteButton;

        public DeviceViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.nameTV);
            mMacView = itemView.findViewById(R.id.macTV);
            mEditButton = itemView.findViewById(R.id.editBTN);
            mDeleteButton = itemView.findViewById(R.id.deleteBTN);

            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
