// CheckBoxedItem.java
package com.paci.training.android.truongnv92.mockproject.provider;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "check_boxed_items")
public class CheckBoxedItem {
    @PrimaryKey
    @ColumnInfo(name = "fruit_id")
    private int fruitId;

    @ColumnInfo(name = "fruit_valid")
    private int fruitValid;

    public CheckBoxedItem(int tmpFruitId, int tmpFruitValid) {
        this.fruitId = tmpFruitId;
        this.fruitValid = tmpFruitValid;
    }

    public CheckBoxedItem(){}
    public static CheckBoxedItem fromContentValues(ContentValues contentValues) {
        CheckBoxedItem checkBoxedItem = new CheckBoxedItem();
        if (contentValues.containsKey("fruit_id")) {
            checkBoxedItem.setFruitId(contentValues.getAsInteger("fruit_id"));
        }
        if (contentValues.containsKey("fruit_valid")) {
            checkBoxedItem.setFruitValid(contentValues.getAsInteger("fruit_valid"));
        }
        return checkBoxedItem;
    }
    // Getters and setters
    public int getFruitId () {
        return fruitId;
    }

    public void setFruitId(int fruitId){
        this.fruitId = fruitId;
    }

    public int getFruitValid () {
        return fruitValid;
    }

    public void setFruitValid ( int fruitValid){
        this.fruitValid = fruitValid;
    }
}
