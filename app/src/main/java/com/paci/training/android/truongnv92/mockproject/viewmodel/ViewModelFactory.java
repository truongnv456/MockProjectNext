package com.paci.training.android.truongnv92.mockproject.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.paci.training.android.truongnv92.mockproject.model.repository.FruitRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final FruitRepository fruitRepository;

    public ViewModelFactory(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FruitViewModel.class)) {
            return (T) new FruitViewModel(fruitRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
