package com.github.vikes2.bt_stat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
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

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("pawelski", "found");
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.d("pawelski", "Device is now connected");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("pawelski", "Done searching");
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Log.d("pawelski", "Device is about to disconnect");
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.d("pawelski", "Device has disconnected");
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.d("pawelski", "ACTION BOND STATE CHANGED");
            }
        }
    };

//    BluetoothHeadset mBluetoothHeadset;
//
//    // Get the default adapter
//    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
//        public void onServiceConnected(int profile, BluetoothProfile proxy) {
//            if (profile == BluetoothProfile.HEADSET) {
//                mBluetoothHeadset = (BluetoothHeadset) proxy;
//                Log.d("pawelski", "con");
//            }
//        }
//        public void onServiceDisconnected(int profile) {
//            if (profile == BluetoothProfile.HEADSET) {
//                mBluetoothHeadset = null;
//                Log.d("pawelski", "dc");
//            }
//        }
//    };



}
