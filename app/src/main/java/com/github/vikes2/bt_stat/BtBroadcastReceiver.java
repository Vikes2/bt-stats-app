package com.github.vikes2.bt_stat;

import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BtBroadcastReceiver extends BroadcastReceiver {
    private BTDatabase db;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        db = Room.databaseBuilder(context.getApplicationContext(),
                BTDatabase.class, "database-name").build();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d("pawelski", "found");
        }
        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            Log.d("pawelski", "Device is now connected");



            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> deviceList = db.deviceDao().getList();
                    if(deviceList != null && deviceList.contains(device.getAddress())){
                        final Action actionDB = new Action(device.getAddress(),true,Calendar.getInstance().getTimeInMillis());
                        //db.actionDao().insert(actionDB);


                        Action lastAction = db.actionDao().getLastAction(actionDB.network_id);

                        if(lastAction == null) {
                            Log.d("elo321", "con");
                            db.actionDao().insert(actionDB);
                        }else if(lastAction.connected) {
                            return;
                        }
                        else if(!lastAction.connected) {
                            //db.actionDao().insert(new Action(action.network_id, false, action.time));
                            Log.d("elo321", "con");
                            db.actionDao().insert(actionDB);
                        }


                    }


                }
            });
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.d("pawelski", "Done searching");
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            Log.d("pawelski", "Device is about to disconnect");
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            Log.d("pawelski", "Device has disconnected");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> deviceList = db.deviceDao().getList();
                    if(deviceList != null && deviceList.contains(device.getAddress())){

                        Action[] lastConnected = db.actionDao().getLastConnected();

                        if(lastConnected.length > 0){
                            if(!device.getAddress().equals(lastConnected[0].network_id)){
                                Log.d("pawelski", "tu zly dc");
                            }
                            db.actionDao().insert(new Action(lastConnected[0].network_id, false, Calendar.getInstance().getTimeInMillis()));
                        }
                    }
                }
            });
        }
        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            Log.d("pawelski", "ACTION BOND STATE CHANGED");
        }
    }

}
