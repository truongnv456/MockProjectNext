package com.paci.training.android.truongnv92.mockproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CheckBoxedItemContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.mockprojectprovider";
    private static final String TABLE_NAME = "check_boxed_items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final String COLUMN_FRUIT_ID = "fruit_id";
    public static final String COLUMN_FRUIT_VALID = "fruit_valid";
    //The match code for some items in CheckedItem table
    public static final int URI_ALL_ITEMS_CODE = 1;
    //The match code for an items in CheckedItem table
    public static final int URI_ONE_ITEM_CODE = 2;
    // to match the content URI, every time user access table under content provider
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
    // to access whole table
        uriMatcher.addURI(AUTHORITY , TABLE_NAME, URI_ALL_ITEMS_CODE);
    // to access a particular row of the table
        uriMatcher.addURI(AUTHORITY , TABLE_NAME , URI_ONE_ITEM_CODE);
    }
    private CheckBoxedItemDao checkBoxedItemDao;

    private static final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public boolean onCreate() {
        CheckBoxedItemDatabase database = CheckBoxedItemDatabase.getInstance(getContext().getApplicationContext());
        checkBoxedItemDao = database.checkBoxedItemDao();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("TAG", "query");
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case URI_ALL_ITEMS_CODE:
                cursor = checkBoxedItemDao.findAll();
                if(getContext() != null){
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                }
            case URI_ONE_ITEM_CODE:
                cursor = checkBoxedItemDao.findAll();
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final long[] newRowId = {0};
        int fruitId = values.getAsInteger("fruit_id");
        int fruitValid = values.getAsInteger("fruit_valid");
        CheckBoxedItem checkBoxedItem = new CheckBoxedItem(fruitId, fruitValid);

        executor.execute(() -> {
            newRowId[0] = checkBoxedItemDao.insert(checkBoxedItem);
            Log.d("CheckBoxedItemProvider", "Inserted CheckBoxedItem with fruitId: " + fruitId + " and fruitValid: " + fruitValid);
        });

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int fruitId = Integer.parseInt(uri.getLastPathSegment());
        int fruitValid = values.getAsInteger("fruit_valid");

        // Kiểm tra nếu fruitValid đã được cập nhật từ 1 xuống 0, không thực hiện cập nhật lại
        if (fruitValid == 0) {
            values.put("fruit_valid", fruitValid);
            int finalFruitValid = fruitValid;
            executor.execute(() -> {
                checkBoxedItemDao.updateFruitValid(fruitId, finalFruitValid);
                checkBoxedItemDao.insert(new CheckBoxedItem(fruitId, finalFruitValid));
                Log.d("CheckBoxedItemProvider", "Updated fruitValid " + fruitValid + " for fruitId: " + fruitId);
            });
        } else {
            Log.d("CheckBoxedItemProvider", "Updated fruitValid" + fruitValid + "for fruitId: " + fruitId);
        }
        return 1;
    }
}
