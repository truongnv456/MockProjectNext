package com.paci.training.android.truongnv92.mockproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.paci.training.android.truongnv92.mockproject.model.Fruit;
import com.paci.training.android.truongnv92.mockproject.model.repository.FruitRepository;

import java.util.List;

public class FruitViewModel extends ViewModel {
    private final FruitRepository fruitRepository;
    private MutableLiveData<List<Fruit>> fruitListLiveData;
    private Fruit currentSelectedFruit;
    public Fruit getCurrentSelectedFruit(){
        return currentSelectedFruit;
    }
    public void setCurrentSelectedFruit(Fruit selectedFruit){
        this.currentSelectedFruit = selectedFruit;
    }
    public FruitViewModel(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
        fruitListLiveData = new MutableLiveData<>();
    }
    public LiveData<List<Fruit>> getFruitListLiveData() {
        return fruitListLiveData;
    }

    public List<Fruit> loadFruits() {
    // Gọi phương thức getFruits() từ Repository để lấy danh sách trái cây
        List<Fruit> fruitList = fruitRepository.getFruits();
    // Cập nhật LiveData với danh sách trái cây mới
        fruitListLiveData.setValue(fruitList);
        return fruitList;
    }
}
