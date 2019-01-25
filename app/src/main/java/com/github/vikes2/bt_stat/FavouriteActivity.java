package com.github.vikes2.bt_stat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavouriteActivity extends AppCompatActivity {
    private Spinner boundedDeviesSpinner;
    private BoundedDevicesAdapter mBoundedDevicesAdapter;
    private List<BoundedDevice> mBoundedDeviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoutite);

        mBoundedDeviceList = new ArrayList<>();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // device doesn't support bluetooth
        }
        else {
            // bluetooth is off, ask user to on it.
            if(!bluetoothAdapter.isEnabled()) {
                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter, 0);
            }

            // Do whatever you want to do with your bluetoothAdapter
            Set<BluetoothDevice> all_devices = bluetoothAdapter.getBondedDevices();
            if (all_devices.size() > 0) {
                for (BluetoothDevice currentDevice : all_devices) {
                    mBoundedDeviceList.add(new BoundedDevice(currentDevice.getName(), currentDevice.getAddress()));
                    //log.i("Device Name " + currentDevice.getName());
                }
            }
        }

        mBoundedDevicesAdapter = new BoundedDevicesAdapter(mBoundedDeviceList);
        boundedDeviesSpinner = findViewById(R.id.bounded_devices_spinner);
        boundedDeviesSpinner.setAdapter(mBoundedDevicesAdapter);

    }
}
