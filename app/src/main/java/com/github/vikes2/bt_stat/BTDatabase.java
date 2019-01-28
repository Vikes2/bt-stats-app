package com.github.vikes2.bt_stat;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

@Database(entities = {DeviceBT.class, Action.class}, version = 1)
public abstract class BTDatabase extends RoomDatabase{
    public abstract DeviceDao deviceDao();
    public abstract ActionDao actionDao();

}
