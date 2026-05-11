package com.example.mid1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;
    private LiveData<List<Product>> productsLiveData;
    private LiveData<String> errorLiveData;
    private LiveData<Boolean> loadingLiveData;
    private MutableLiveData<Product> currentProductLiveData;

    public ProductViewModel() {
        productRepository = new ProductRepository();
        productsLiveData = productRepository.getProducts();
        errorLiveData = productRepository.getError();
        loadingLiveData = productRepository.getLoading();
        currentProductLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public MutableLiveData<Product> getCurrentProduct() {
        return currentProductLiveData;
    }

    public void loadSellerProducts() {
        productRepository.loadSellerProducts();
    }

    public void addProduct(Product product, ProductRepository.OnProductAddedListener listener) {
        productRepository.addProduct(product, listener);
    }

    public void updateProduct(String productId, Product product, 
                             ProductRepository.OnProductUpdatedListener listener) {
        productRepository.updateProduct(productId, product, listener);
    }

    public void deleteProduct(String productId, ProductRepository.OnProductDeletedListener listener) {
        productRepository.deleteProduct(productId, listener);
    }

    public void getProductById(String productId) {
        productRepository.getProductById(productId, currentProductLiveData);
    }
}







