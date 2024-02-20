package com.paci.training.android.truongnv92.mockproject.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ListFruitFragment extends Fragment {
    private ImageView imageView;
    private Button btnDetail;
    private FruitViewModel fruitViewModel;
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
        FruitViewModel fruitViewModel = new FruitViewModel(fruitRepository);
        // Load Fruits từ model
        List<Fruit> fruitList = fruitViewModel.loadFruits();
        FruitAdapter fruitAdapter = new FruitAdapter(requireActivity(), fruitList);
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
                    bundle.putSerializable("selectedFruit", fruitViewModel.getCurrentSelectedFruit());
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
}