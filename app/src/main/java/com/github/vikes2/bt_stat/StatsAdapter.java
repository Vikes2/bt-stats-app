package com.github.vikes2.bt_stat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private ArrayList<DeviceBT> mDeviceList;
    private ArrayList<Action> mActionList;
    private OnItemLongClickListener mListener;
    public HashMap<String, String> mData;
    public StatsAdapter(ArrayList<DeviceBT> routerList, ArrayList<Action> actionList, HashMap<String, String> stringLongHashMap){
        mDeviceList = routerList; mActionList = actionList;  mData = stringLongHashMap;}

    public interface OnItemLongClickListener {
        void onItemClick(int position);
    }

    public void setOnItemLongClickListener(StatsAdapter.OnItemLongClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public StatsAdapter.StatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stat_item, viewGroup, false);
        StatsAdapter.StatsViewHolder rvh = new StatsAdapter.StatsViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatsAdapter.StatsViewHolder holder, int position) {
        DeviceBT currentItem = mDeviceList.get(position);
        if(mData == null){
            holder.mTimeView.setText("unknown");
        }else{

            if(mData.containsKey(currentItem.mac)){

                holder.mTimeView.setText(mData.get(currentItem.mac));
            }else{
                holder.mTimeView.setText("unknown2");
            }
        }
        holder.mNameView.setText(currentItem.name);
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }


    public static class StatsViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public TextView mTimeView;
        public RelativeLayout mCardItem;

        public StatsViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.nameTV);
            mTimeView = itemView.findViewById(R.id.timeTV);
            mCardItem = itemView.findViewById(R.id.stat_single_item);

            mCardItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}