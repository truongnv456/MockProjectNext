package com.paci.training.android.truongnv92.mockproject.view.adapter;

import android.annotation.SuppressLint;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;
import com.paci.training.android.truongnv92.mockproject.provider.CheckBoxedItemContentProvider;
import com.paci.training.android.truongnv92.mockproject.viewmodel.FruitViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {
    private List<Fruit> fruitList = new ArrayList<>();
    private Context context;
    private CheckBoxedItemContentProvider contentProvider;
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.mockprojectprovider";
    private static final String PATH_ITEMS = "check_boxed_items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_ITEMS);
    // Khai báo một Executor mới
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private OnItemClickListener listener;
    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position, boolean isChecked);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Fruit fruit);
    }

    public FruitAdapter(Context context, List<Fruit> fruitList) {
        this.context = context;
        this.fruitList.clear();
        this.contentProvider = new CheckBoxedItemContentProvider();
        this.fruitList.addAll(fruitList);
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

        holder.checkBoxItem.setOnCheckedChangeListener(null); // Remove listener to avoid triggering listener during initialization
        holder.checkBoxItem.setChecked(item.isChecked());
        holder.checkBoxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Lưu trạng thái mới vào danh sách fruits khi checkbox thay đổi
            item.setChecked(isChecked);
            holder.checkBoxTextView.setEnabled(isChecked);

        });

        holder.checkBoxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && item.isChecked()) {
                    listener.onItemClick(position, item);
                }
            }
        });
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

