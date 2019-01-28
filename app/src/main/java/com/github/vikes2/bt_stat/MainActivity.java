package com.github.vikes2.bt_stat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.floor;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BTDatabase db;
    private ArrayList<Action> actionList = new ArrayList<>();
    private ArrayList<DeviceBT> deviceList = new ArrayList<>();
    private StatsAdapter mAdapter;
    public HashMap<String, String> mTimeData;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BtBroadcastReceiver mReceiver = new BtBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

        db = Room.databaseBuilder(getApplicationContext(),
                BTDatabase.class, "database-name").build();

        mRecyclerView = findViewById(R.id.statsRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StatsAdapter(deviceList, actionList, processTimeData(deviceList, actionList));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        addAdapterListeners();
    }

    private void addAdapterListeners(){
        mAdapter.setOnItemLongClickListener(new StatsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("pawelski", "on Long click");
            }
        });
    }

    @Override
    public void onResume() {
        db.deviceDao().getAll().observe(this, new Observer<List<DeviceBT>>() {
            @Override
            public void onChanged(@Nullable List<DeviceBT> routers) {
                if (!deviceList.isEmpty()) {
                    deviceList.clear();
                }
                deviceList.addAll(routers);

                Log.d("wifi-timer", "MainActivity.onCreate(): Pobrano routery w ilości: " + routers.size());

                db.actionDao().getAll().observe(MainActivity.this, new Observer<List<Action>>() {
                    @Override
                    public void onChanged(@Nullable List<Action> actions) {
                        if (!actionList.isEmpty()) {
                            actionList.clear();
                        }
                        actionList.addAll(actions);

                        processData();
                        mAdapter.notifyDataSetChanged();
                        Log.d("pawelski", ": Pobrano akcje w ilości: " + actions.size());
                    }
                });
            }
        });
        super.onResume();
    }

    private void processData() {
        if(deviceList != null && deviceList.size() > 0 ) {
            mTimeData = processTimeData(deviceList, actionList);
            mAdapter.mData = mTimeData;

            mAdapter.notifyDataSetChanged();
        }
    }

    public HashMap<String, String> processTimeData(ArrayList<DeviceBT> mDeviceList, ArrayList<Action> mActionList){
        HashMap<String, Long> resultMap = new HashMap<>();

        for(DeviceBT device : mDeviceList){
            resultMap.put(device.mac, 0l);
        }

        if(mActionList != null && mActionList.size() > 0){

            String last_connection_id = "0";
            long last_connection_time = 0;
            Boolean isEmpty = true;
            for(Action action : mActionList){
                if(action.connected == true && isEmpty){
                    last_connection_id = action.network_id;
                    last_connection_time = action.time;
                    isEmpty = false;
                }else if(action.connected == false ){
                    if(!isEmpty){
                        long currentTime = resultMap.get(last_connection_id);
                        long toAddTime = action.time - last_connection_time;

                        for(DeviceBT curDevice : mDeviceList){
                            if(curDevice.mac.equals(last_connection_id)){
                                resultMap.put(last_connection_id, currentTime + toAddTime);
                            }
                        }
                    }
                    isEmpty = true;
                }
            }

            if(mActionList.size()>0){
                Action  lastAction = mActionList.get( mActionList.size() - 1 );
                if( lastAction.connected == true){
                    long now = Calendar.getInstance().getTimeInMillis();
                    long toAddTime = now - lastAction.time;
                    long currentTime = resultMap.get(lastAction.network_id);

                    resultMap.put(lastAction.network_id, currentTime + toAddTime);
                }
            }
        }



        HashMap<String, String> resultStringMap = new HashMap<>();
        for(String currentKey : resultMap.keySet()){
            resultStringMap.put(currentKey, miliToString(resultMap.get(currentKey)));
        }

        return resultStringMap;
    }

    private String miliToString(Long mil){
        int sec = (int) floor((mil / 1000) % 60);
        int min = (int) floor(((mil / 1000) / 60) % 60);
        int hour = (int) floor((((mil / 1000) / 60) / 60) % 24);
        int day = (int) floor(((((mil / 1000) / 60) / 60) / 24) % 30);

        String out = "";

        String dayStr = getResources().getQuantityString(R.plurals.day, day, day);

        if(day> 0){
            out = dayStr +" "+ hour +" "+ getString(R.string.hour)+ " " + min + " " + getString(R.string.minutes);
        }else if(hour > 0){
            out = hour +" "+ getString(R.string.hour)+ " " + min + " " + getString(R.string.minutes) + " " + sec +" "+ getString(R.string.seconds);
        }else if(min > 0){
            out = min + " " + getString(R.string.minutes) + " " + sec +" "+ getString(R.string.seconds);
        }else{
            out = sec +" "+ getString(R.string.seconds);
        }
        return out;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent myIntent = new Intent(this, FavouriteActivity.class);
                startActivityForResult(myIntent, 1);
                return true;
            case R.id.reset:
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {
                        for( int i = 0; i< actionList.size(); i++){
                            db.actionDao().delete(actionList.get(i));
                        }

                    }
                });
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Log.d("pawelski", "zakonczono");
                processData();
            }
        }
    }

}
