package com.paci.training.android.truongnv92.mockproject.view.adapter;

import android.annotation.SuppressLint;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {
    private List<Fruit> fruitList = new ArrayList<>();
    private Context context;
    public static final Uri CONTENT_URI = Uri.parse( "content://com.paci.training.android.truongnv92.provider.mockprojectprovider/check_boxed_items");

    // Khai báo một Executor mới
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private OnItemClickListener listener;
    private ContentResolver contentResolver;

    public interface OnItemClickListener {
        void onItemClick(int position, Fruit fruit);
    }

    public FruitAdapter(Context context, List<Fruit> fruitList, ContentResolver contentResolver) {
        this.context = context;
        this.fruitList.addAll(fruitList);
        this.contentResolver = contentResolver;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Fruit item = fruitList.get(position);
        holder.checkBoxTextView.setText(item.getName());
        holder.checkBoxTextView.setEnabled(item.isChecked());

        // Tạo ContentResolver từ context
        ContentResolver contentResolver = context.getContentResolver();

        holder.checkBoxItem.setOnCheckedChangeListener(null); // Remove listener to avoid triggering listener during initialization
        holder.checkBoxItem.setChecked(item.isChecked());
        Log.e("isCheck",item.isChecked()+"");

        holder.checkBoxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Lưu trạng thái mới vào danh sách fruits khi checkbox thay đổi
            item.setChecked(isChecked);
            holder.checkBoxTextView.setEnabled(isChecked);

            // Highlight item nếu được chọn
            if (isChecked) {
                holder.itemView.setBackgroundColor(Color.GREEN);
            } else {
                // Khôi phục màu nền ban đầu nếu không được chọn
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            // Thực hiện cập nhật fruitValid dựa trên isChecked trên một luồng nền
            executor.execute(() -> {
                ContentValues updateValues = new ContentValues();
                updateValues.put("fruit_valid", isChecked ? 1 : 0); // Nếu isChecked, fruitValid = 1, ngược lại là 0
                String selection = "fruit_id=?";
                int fruitId = item.getId();
                String[] selectionArgs = {Integer.toString(fruitId)};
                contentResolver.update(CONTENT_URI, updateValues, selection, selectionArgs);
            });
        });

        holder.checkBoxTextView.setOnClickListener(v -> {
            if (listener != null && item.isChecked()) {
                listener.onItemClick(position, item);
            }
        });

        // Thêm logic để highlight item dựa trên trạng thái của isChecked
        if (item.isChecked()) {
            // Thực hiện highlight item ở đây, ví dụ:
            holder.itemView.setBackgroundColor(Color.GREEN);
        } else {
            // Đảm bảo rằng mọi item không được chọn sẽ không được highlight, ví dụ:
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    public static class FruitViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxItem;
        TextView checkBoxTextView;

        public FruitViewHolder(@NonNull View view) {
            super(view);
            checkBoxItem = view.findViewById(R.id.checkbox);
            checkBoxTextView = view.findViewById(R.id.text_name);
        }
    }

}

