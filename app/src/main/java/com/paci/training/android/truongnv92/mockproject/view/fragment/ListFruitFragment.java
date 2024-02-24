package com.paci.training.android.truongnv92.mockproject.view.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ListFruitFragment extends Fragment {

    private static final String AUTHORITY = "com.paci.training.android.truongnv92.provider.mockprojectprovider";
    private static final String TABLE_NAME = "check_boxed_items";

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
                , fruitViewModel.updateDataFollowInteracting(getContext().getContentResolver())
                , getContext().getContentResolver());
        recyclerView.setAdapter(fruitAdapter);

        fruitAdapter.setOnItemClickListener(new FruitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Fruit fruit) {
                imageView.setImageResource(fruit.getSrc());
                fruitViewModel.setCurrentSelectedFruit(fruit); // Lưu trạng thái của mục fruit được chọn
                imageView.setImageResource(fruit.getSrc()); // Hiển thị ảnh của fruit item cuối cùng
                Log.d("TAG",fruitViewModel.getCurrentSelectedFruit().getSrc()+"");
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fruit lastSelectedFruit = fruitViewModel.getCurrentSelectedFruit();
                if (lastSelectedFruit != null) {
                    fruitViewModel.setCurrentSelectedFruit(lastSelectedFruit); // Cập nhật lastSelectedFruit trước khi chuyển sang DetailFragment

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Kiểm tra xem FruitViewModel có chứa dữ liệu của fruit item cuối cùng hay không
        Fruit lastSelectedFruit = fruitViewModel.getCurrentSelectedFruit();
        if (lastSelectedFruit != null) {
            imageView.setImageResource(lastSelectedFruit.getSrc()); // Hiển thị ảnh của fruit item cuối cùng
            Log.e("ImageView",""+lastSelectedFruit.getSrc());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Lấy trạng thái của mục trái cây hiện tại từ FruitViewModel và lưu vào Bundle
        Fruit lastSelectedFruit = fruitViewModel.getCurrentSelectedFruit();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lastSelectedFruit", lastSelectedFruit);
        getParentFragmentManager().setFragmentResult("lastSelectedFruit", bundle);
    }
}
