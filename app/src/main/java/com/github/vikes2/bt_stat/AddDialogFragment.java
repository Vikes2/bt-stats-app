package com.github.vikes2.bt_stat;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddDialogFragment extends DialogFragment {

    public interface AddDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, BoundedDevice selectedDevice);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    AddDialogListener mListener;
    public EditText name;
    public Spinner macSpinner;
    BoundedDevice selectedDevice;

    private Spinner boundedDeviesSpinner;
    private BoundedDevicesAdapter mBoundedDevicesAdapter;
    private List<BoundedDevice> mBoundedDeviceList;


    private void setDevicesSpinner(View view){

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
                }
            }
        }

        mBoundedDevicesAdapter = new BoundedDevicesAdapter(mBoundedDeviceList);
        //macSpinner = view.findViewById(R.id.bounded_devices_spinner);

        boundedDeviesSpinner = view.findViewById(R.id.bounded_devices_spinner);
        boundedDeviesSpinner.setAdapter(mBoundedDevicesAdapter);
        boundedDeviesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDevice = (BoundedDevice)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        setDevicesSpinner(view);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        name = getActivity().findViewById(R.id.btName);
                        if(selectedDevice != null){
                            mListener.onDialogPositiveClick(AddDialogFragment.this, selectedDevice);
                        }else{
                            Log.d("buggy", "selected jest nullem");
                        }
                    }
                })
                .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(AddDialogFragment.this);
                    }
                });
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(" must implement AddDialogListener");
        }
    }
}
