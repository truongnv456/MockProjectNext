package com.paci.training.android.truongnv92.mockproject.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {
    private List<Fruit> fruits;
    private Context context;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position, Fruit fruit);
    }
    public FruitAdapter(Context context, List<Fruit> fruits) {
        this.context = context;
        this.fruits = fruits;
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
        Fruit item = fruits.get(position);
        holder.checkBoxTextView.setText(item.getName());
        holder.checkBoxTextView.setEnabled(item.isChecked());
        holder.checkBoxItem.setChecked(item.isChecked());
        holder.checkBoxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
        // Lưu trạng thái mới vào danh sách fruits khi checkbox thay đổi
            Fruit fruit = fruits.get(position);
            fruit.setChecked(isChecked);
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
        return fruits.size();
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
