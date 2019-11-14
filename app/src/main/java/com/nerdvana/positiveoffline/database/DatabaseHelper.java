package com.nerdvana.positiveoffline.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseHelper {

    private static volatile PosDatabase INSTANCE;


    public static PosDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PosDatabase.class, "pos_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
