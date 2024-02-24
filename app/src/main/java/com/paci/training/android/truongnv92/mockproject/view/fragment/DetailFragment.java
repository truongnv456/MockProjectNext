package com.paci.training.android.truongnv92.mockproject.view.fragment;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.paci.training.android.truongnv92.mockproject.R;
import com.paci.training.android.truongnv92.mockproject.model.Fruit;
import com.paci.training.android.truongnv92.mockproject.model.repository.FruitRepository;
import com.paci.training.android.truongnv92.mockproject.viewmodel.FruitViewModel;
import com.paci.training.android.truongnv92.mockprojectserver.IMyAidlInterface;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailFragment extends Fragment {
    private static final String AUTHORITY = "com.paci.training.android.truongnv92.provider.mockprojectprovider";

    Button btnTest;
    private Fruit selectedFruit;
    private TextView tvFruitDetail;
    private TextView tvFruitName;
    private FruitViewModel fruitViewModel;

    public DetailFragment() {
    }
    // service
    IMyAidlInterface iMyAidlInterface;
    private final String TAG = "CheckService";
    ServiceConnection mServiceConnection = new ServiceConnection() {
        // Được gọi khi kết nối với service được thiết lập.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Lấy một thể hiện của IMyAidlInterface, mà chúng ta có thể sử dụng để gọi service.
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            if (selectedFruit != null) {
                try {
                    // Gọi phương thức getFruitDetail từ dịch vụ và hiển thị kết quả trên TextView
                    String fruitDetail = iMyAidlInterface.getFruitDetail(selectedFruit.getId());
                    tvFruitDetail.setText(fruitDetail);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "Remote config Service Connected!!!");
        }
        // Được gọi khi kết nối với dịch vụ bị ngắt kết nối một cách không mong muốn.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            iMyAidlInterface = null;
        }
    };
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        btnTest = view.findViewById(R.id.btn_test);
        Intent intent = new Intent("AIDLFruitService");
        intent.setPackage("com.paci.training.android.truongnv92.mockprojectserver");
        // Kết nối với service AIDL khi Fragment được tạo
        getActivity().bindService(intent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
        Button btnBack = view.findViewById(R.id.btn_back);

        tvFruitName = view.findViewById(R.id.fruit_name);
        tvFruitDetail = view.findViewById(R.id.fruit_detail);

        // Khởi tạo FruitViewModel
        FruitRepository fruitRepository = new FruitRepository();
        fruitViewModel = new FruitViewModel(fruitRepository);

        // Lấy dữ liệu fruit được chọn từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedFruit = (Fruit) bundle.getSerializable("selectedFruit");
        }

        // Hiển thị thông tin của fruit được chọn
        if (selectedFruit != null) {
            tvFruitName.setText(selectedFruit.getName());
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Replace ListFruitsFragment with DetailFragment
                if (selectedFruit != null) {
                    fruitViewModel.setCurrentSelectedFruit(selectedFruit);
                }
                Log.d("TAG",fruitViewModel.getCurrentSelectedFruit().getSrc()+" ");
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}