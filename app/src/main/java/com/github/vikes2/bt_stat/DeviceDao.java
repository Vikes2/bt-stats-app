package com.github.vikes2.bt_stat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM devicebt")
    LiveData<List<DeviceBT>> getAll();

    @Query("SELECT mac FROM devicebt")
    List<String> getList();

    @Insert
    void insert(DeviceBT device);

    @Update
    void update(DeviceBT device);

    @Delete
    void delete(DeviceBT device);
}

