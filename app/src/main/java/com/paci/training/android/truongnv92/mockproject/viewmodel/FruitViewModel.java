package com.paci.training.android.truongnv92.mockproject.viewmodel;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;
import com.paci.training.android.truongnv92.mockproject.model.repository.FruitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;


public class FruitViewModel extends ViewModel {
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.mockprojectprovider";
    private static final String TABLE_NAME = "check_boxed_items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final String COLUMN_FRUIT_ID = "fruit_id";
    public static final String COLUMN_FRUIT_VALID = "fruit_valid";
    private final FruitRepository fruitRepository;
    private MutableLiveData<List<Fruit>> fruitListLiveData;
    private Fruit currentSelectedFruit;

    public Fruit getCurrentSelectedFruit() {
        return currentSelectedFruit;
    }

    public void setCurrentSelectedFruit(Fruit selectedFruit) {
        this.currentSelectedFruit = selectedFruit;
    }

    public FruitViewModel(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
        fruitListLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Fruit>> getFruitListLiveData() {
        return fruitListLiveData;
    }

    public List<Fruit> loadFruits() {
        // Gọi phương thức getFruits() từ Repository để lấy danh sách trái cây
        List<Fruit> fruitList = fruitRepository.getFruits();
        // Cập nhật LiveData với danh sách trái cây mới
        fruitListLiveData.setValue(fruitList);

        return fruitList;

    }

    public List<Fruit> getRawFruits() {
        List<Fruit> list = new ArrayList<>();
        list.add(new Fruit(1, "Tomato", "No information", R.drawable.tomato, false));
        list.add(new Fruit(2, "Banana", "No information", R.drawable.bananas, false));
        list.add(new Fruit(3, "Apple", "No information", R.drawable.apples, false));
        list.add(new Fruit(4, "Watermelon", "No information", R.drawable.watermelons, false));
        list.add(new Fruit(5, "Orange", "No information", R.drawable.oranges, false));
        list.add(new Fruit(6, "Mango", "No information", R.drawable.mangoes, false));
        list.add(new Fruit(7, "Pear", "No information", R.drawable.pears, false));
        list.add(new Fruit(8, "Avocado", "No information", R.drawable.avocadoes, false));
        list.add(new Fruit(9, "Grape", "No information", R.drawable.grapes, false));
        list.add(new Fruit(10, "Pineapple", "No information", R.drawable.pineapple, false));
        list.add(new Fruit(11, "Peach", "No information", R.drawable.peach, false));
        list.add(new Fruit(12, "Pumpkin", "No information", R.drawable.pumpkin, false));
        list.add(new Fruit(13, "Dates", "No information", R.drawable.dates, false));
        list.add(new Fruit(14, "Papaya", "No information", R.drawable.papaya, false));
        list.add(new Fruit(15, "Strawberry", "No information", R.drawable.strawberry, false));
        return list;
    }

    public List<Integer> getCheckedItems(ContentResolver contentResolver) {
        List<Integer> result = new ArrayList<>();
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        Callable<String> callableTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<Integer> fruitValids = new ArrayList<>();
                Uri uri = Uri.parse("content://com.paci.training.android.truongnv92.mockprojectprovider/check_boxed_items");
                String[] projection = {"fruit_id", "fruit_valid"};
                Cursor cursor = contentResolver.query(uri, projection, null, null, null);
                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            do {
                                @SuppressLint("Range") int fruitValid = cursor.getInt(cursor.getColumnIndex("fruit_valid"));
                                fruitValids.add(fruitValid);
                            } while (cursor.moveToNext());
                        }
                    } finally {
                        cursor.close();
                    }
                }
                return fruitValids.toString();
            }
        };
        //get data from future
        Future<String> future = executor1.submit(callableTask);
        try {
            String _result = future.get();
            String removeBracket = _result.substring(1, _result.length() - 1);
            String[] elements = removeBracket.split(", ");
            for (String e : elements) {
                result.add(Integer.parseInt(e));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // Đóng ExecutorService
        executor1.shutdown();
        return result;
    }

    public List<Fruit> updateDataFollowInteracting(ContentResolver contentResolver) {
        List<Fruit> rawFruits = this.getRawFruits();
        List<Integer> itemValids = this.getCheckedItems(contentResolver);
        for (Fruit fruit : rawFruits) {
            for (int i = 0; i < itemValids.size(); i++) {
                if (fruit.getId() == i + 1) {
                    if (itemValids.get(i) == 1) {
                        fruit.setChecked(true);
                        Log.d("isChecked",""+ fruit.isChecked());
                    } else {
                        fruit.setChecked(false);
                        Log.d("isChecked",""+ fruit.isChecked());
                    }
                }
            }
        }
        return rawFruits;
    }

    public void updateCheckBoxedItemFruitValid(int fruitValid, int fruitId, ContentResolver contentResolver) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRUIT_VALID, fruitValid);

        // Xây dựng URI với ID của trái cây
        Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(fruitId));

        // Update trạng thái của checkbox item trong ContentProvider
        contentResolver.update(uri, values, null, null);
    }


}

