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
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.provider.mockprojectprovider";
    private static final String TABLE_NAME = "check_boxed_items";
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
        uriMatcher.addURI(AUTHORITY , TABLE_NAME + "/*", URI_ONE_ITEM_CODE);
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
        Log.d("TAG", "update");
        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)){
            case URI_ONE_ITEM_CODE:
                if (getContext() != null) {
                    String fruitId = uri.getLastPathSegment(); // Lấy ra fruitId từ URI
                    if (values != null && values.containsKey(COLUMN_FRUIT_VALID)) {
                        int fruitValid = values.getAsInteger(COLUMN_FRUIT_VALID);
                        rowsUpdated = checkBoxedItemDao.updateFruitValid(Integer.parseInt(fruitId), fruitValid);
                        if (rowsUpdated != 0) {
                            getContext().getContentResolver().notifyChange(uri, null);
                        }
                    }
                    return rowsUpdated;
                }
                throw new IllegalArgumentException("Invalid URI: cannot update");
            case URI_ALL_ITEMS_CODE:
                if (selection != null && selection.equals("fruit_id=?") && selectionArgs != null && selectionArgs.length > 0) {
                    String fruitId = selectionArgs[0];
                    rowsUpdated = checkBoxedItemDao.updateFruitValid(Integer.parseInt(fruitId), values.getAsInteger(COLUMN_FRUIT_VALID));
                    Log.d("checkcp","fruit_id: "+fruitId+", fruit_valid: "+values.getAsInteger(COLUMN_FRUIT_VALID));
                    if (rowsUpdated != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return rowsUpdated;
                } else {
                    throw new IllegalArgumentException("Invalid URI: cannot update");
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
