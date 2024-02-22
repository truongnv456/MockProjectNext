// CheckBoxedItemDatabase.java
package com.paci.training.android.truongnv92.mockproject.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {CheckBoxedItem.class}, version = 1)
public abstract class CheckBoxedItemDatabase extends RoomDatabase {
    private static volatile CheckBoxedItemDatabase INSTANCE;

    public abstract CheckBoxedItemDao checkBoxedItemDao();

    public static synchronized CheckBoxedItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CheckBoxedItemDatabase.class, "fruit_item_db")
                    .build();
        }
        return INSTANCE;
    }

}
