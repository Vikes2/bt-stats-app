package com.github.vikes2.bt_stat;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DeviceBT {
    @ColumnInfo(name= "name")
    public String name;

    @ColumnInfo(name= "core_name")
    public String core_name;

    @NonNull
    @PrimaryKey()
    public String mac;

    public DeviceBT(){}

    public DeviceBT(String _name, String _mac, String core_name){
        this.name = _name;
        this.mac = _mac;
        this.core_name = core_name;
    }

    public String getName(){
        return name;
    }

    public String getMac(){
        return mac;
    }

    public String getCoreName(){
        return core_name;
    }

}
