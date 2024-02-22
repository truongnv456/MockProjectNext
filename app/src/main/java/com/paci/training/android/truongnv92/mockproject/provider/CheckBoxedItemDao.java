// CheckBoxedItemDao.java
package com.paci.training.android.truongnv92.mockproject.provider;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CheckBoxedItemDao {
    /**
     * select all person
     * @return A {@link Cursor} of all person in the table
     */
    @Query("SELECT * FROM check_boxed_items")
    Cursor findAll();

    @Insert
    void insertAll(List<CheckBoxedItem> checkBoxedItems);

    @Query("UPDATE check_boxed_items SET fruit_valid = :fruitValid WHERE fruit_id = :fruitId")
    int updateFruitValid(int fruitId, int fruitValid);

    @Update
    int update(CheckBoxedItem checkBoxedItem);

    @Insert(onConflict = OnConflictStrategy.IGNORE)// bỏ qua fruit mới nếu nó đã có trong danh sách
    long insert(CheckBoxedItem checkBoxedItem);
}
