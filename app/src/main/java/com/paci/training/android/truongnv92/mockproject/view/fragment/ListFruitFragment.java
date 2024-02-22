package com.paci.training.android.truongnv92.mockproject.view.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;
import com.paci.training.android.truongnv92.mockproject.model.repository.FruitRepository;
import com.paci.training.android.truongnv92.mockproject.view.adapter.FruitAdapter;
import com.paci.training.android.truongnv92.mockproject.viewmodel.FruitViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListFruitFragment extends Fragment implements FruitAdapter.OnCheckedChangeListener {
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.mockprojectprovider";
    private static final String TABLE_NAME = "check_boxed_items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final String COLUMN_FRUIT_ID = "fruit_id";
    public static final String COLUMN_FRUIT_VALID = "fruit_valid";

    private List<Fruit> fruitList;
    private ImageView imageView;
    private Button btnDetail;
    private FruitViewModel fruitViewModel;
    private FruitAdapter fruitAdapter;

    public ListFruitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fruit, container, false);
        btnDetail = view.findViewById(R.id.btn_detail);
        imageView = view.findViewById(R.id.imageView);

        RecyclerView recyclerView = view.findViewById(R.id.rcv_list_fruit);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        FruitRepository fruitRepository = new FruitRepository();
        fruitViewModel = new FruitViewModel(fruitRepository);

        fruitAdapter = new FruitAdapter(requireActivity(),
                fruitViewModel.updateDataFollowInteracting(getContext().getContentResolver()));
        recyclerView.setAdapter(fruitAdapter);
        fruitAdapter.setOnCheckedChangeListener(this);

        fruitAdapter.setOnItemClickListener(new FruitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Fruit fruit) {
                imageView.setImageResource(fruit.getSrc());
                fruitViewModel.setCurrentSelectedFruit(fruit);
                fruitViewModel.updateCheckBoxedItemFruitValid(1, fruitViewModel.getCurrentSelectedFruit().getId()
                        , getContext().getContentResolver());
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fruit lastSelectedFruit = fruitViewModel.getCurrentSelectedFruit();
                if (lastSelectedFruit != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedFruit", lastSelectedFruit);
                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setArguments(bundle);
                    replaceFragment(detailFragment);
                }
            }
        });
        return view;
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        Fruit item = fruitViewModel.getRawFruits().get(position);
        item.setChecked(isChecked);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
/*
public class ListFruitFragment extends Fragment {

    private ImageView imageView;
    private Button btnDetail;
    private FruitViewModel fruitViewModel;
    private FruitAdapter fruitAdapter;

    public ListFruitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fruit, container, false);
        btnDetail = view.findViewById(R.id.btn_detail);
        imageView = view.findViewById(R.id.imageView);

        RecyclerView recyclerView = view.findViewById(R.id.rcv_list_fruit);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        FruitRepository fruitRepository = new FruitRepository();
        fruitViewModel = new FruitViewModel(fruitRepository);

        fruitAdapter = new FruitAdapter(requireActivity()
                , fruitViewModel.updateDataFollowInteracting(getContext().getContentResolver()));
        recyclerView.setAdapter(fruitAdapter);

        fruitAdapter.setOnItemClickListener(new FruitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Fruit fruit) {
                imageView.setImageResource(fruit.getSrc());
                fruitViewModel.setCurrentSelectedFruit(fruit);

            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fruit lastSelectedFruit = fruitViewModel.getCurrentSelectedFruit();
                if (lastSelectedFruit != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedFruit", lastSelectedFruit);
                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setArguments(bundle);
                    replaceFragment(detailFragment);
                }
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}*/
