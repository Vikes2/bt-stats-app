package com.github.vikes2.bt_stat;


import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class FavouriteActivity extends AppCompatActivity implements AddDialogFragment.AddDialogListener, EditDialogFragment.EditDialogListener{

    private BTDatabase db;
    private ArrayList<DeviceBT> deviceList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room.databaseBuilder(getApplicationContext(),
                BTDatabase.class, "database-name").build();

        db.deviceDao().getAll().observe(this, new Observer<List<DeviceBT>>() {
            @Override
            public void onChanged(@Nullable List<DeviceBT> devices) {
                if (!deviceList.isEmpty()) {
                    deviceList.clear();
                }
                deviceList.addAll(devices);
                mAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddDialogFragment();
                dialog.show(getSupportFragmentManager(), "AddDialogFragment");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerFavouriteView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new DeviceAdapter(deviceList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        addAdapterListeners();
    }

    private void addAdapterListeners(){
        mAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                DialogFragment dialog = new EditDialogFragment();

                Bundle b = new Bundle();
                b.putString("name", deviceList.get(position).name);
                b.putString("core_name", deviceList.get(position).core_name);

                b.putString("mac", deviceList.get(position).mac);
                b.putInt("position", position);
                dialog.setArguments(b);

                dialog.show(getSupportFragmentManager(), "EditDialogFragment");

            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
                builder.setMessage(R.string.confirm_quest);
                builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                db.deviceDao().delete(deviceList.get(position));
                                mAdapter.notifyItemRemoved(position);
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, final BoundedDevice selectedDevice) {
        EditText name = dialog.getDialog().findViewById(R.id.btName);
        final String nameText = name.getText().toString();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.deviceDao().insert(new DeviceBT(nameText, selectedDevice.getMac(), selectedDevice.getName()));
            }
        });
    }

    @Override
    public void onEditDialogPositiveClick(DialogFragment dialog) {

        EditText name = dialog.getDialog().findViewById(R.id.btName);
        final String nameText = name.getText().toString();

        int position = ((EditDialogFragment)dialog).position;
        final DeviceBT device = deviceList.get(position);
        device.name = nameText;
        //device.mac = selectedDevice.getMac();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.deviceDao().update(device);

            }
        });
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
}
