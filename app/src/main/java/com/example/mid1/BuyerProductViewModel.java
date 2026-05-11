package com.example.mid1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BuyerProductViewModel extends ViewModel {
    private BuyerProductRepository repository;
    private LiveData<List<items>> productsLiveData;
    private LiveData<String> errorLiveData;
    private LiveData<Integer> loadingStateLiveData;

    public BuyerProductViewModel() {
        repository = new BuyerProductRepository();
        productsLiveData = repository.getProducts();
        errorLiveData = repository.getError();
        loadingStateLiveData = repository.getLoadingState();
    }

    public LiveData<List<items>> getProducts() {
        return productsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Integer> getLoadingState() {
        return loadingStateLiveData;
    }

    public void attachListener() {
        repository.attachProductListener();
    }

    public void detachListener() {
        repository.detachProductListener();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachListener();
    }
}

