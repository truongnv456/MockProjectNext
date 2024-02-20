package com.paci.training.android.truongnv92.mockproject.view.fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.paci.training.android.truongnv92.mockprojectserver.IMyAidlInterface;

public class DetailFragment extends Fragment {
    private Fruit selectedFruit;
    private TextView tvFruitDetail;
    private TextView tvFruitName;
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
                    Log.e("FruitDetail",fruitDetail);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = new Intent("AIDLFruitService");
        intent.setPackage("com.paci.training.android.truongnv92.mockprojectserver");
        // Kết nối với service AIDL khi Fragment được tạo
        getActivity().bindService(intent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
        Button btnBack = view.findViewById(R.id.btn_back);
        tvFruitName = view.findViewById(R.id.fruit_name);
        tvFruitDetail = view.findViewById(R.id.fruit_detail);
        // Nhận thông tin fruit từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedFruit = (Fruit) bundle.getSerializable("selectedFruit");
        // Hiển thị thông tin fruit
            if (selectedFruit != null) {
                tvFruitName.setText(selectedFruit.getName());
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // Replace ListFruitsFragment with DetailFragment
                ListFruitFragment listFruitFragment = new ListFruitFragment();
                replaceFragment(listFruitFragment);
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}