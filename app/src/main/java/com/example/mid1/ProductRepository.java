package com.example.mid1;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference productsRef;
    private MutableLiveData<List<Product>> productsLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;

    public ProductRepository() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.productsRef = firebaseDatabase.getReference("products");
        this.productsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.loadingLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    public MutableLiveData<String> getError() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public void loadSellerProducts() {
        loadingLiveData.setValue(true);
        String sellerId = firebaseAuth.getCurrentUser().getUid();

        productsRef.orderByChild("sellerId").equalTo(sellerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        loadingLiveData.setValue(false);
                        List<Product> products = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Product product = snapshot.getValue(Product.class);
                            if (product != null) {
                                product.setId(snapshot.getKey());
                                products.add(product);
                            }
                        }

                        productsLiveData.setValue(products);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(databaseError.getMessage());
                    }
                });
    }

    public void addProduct(Product product, OnProductAddedListener listener) {
        loadingLiveData.setValue(true);
        String sellerId = firebaseAuth.getCurrentUser().getUid();
        product.setSellerId(sellerId);

        String productId = productsRef.push().getKey();
        if (productId != null) {
            productsRef.child(productId).setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        loadingLiveData.setValue(false);
                        if (listener != null) {
                            listener.onSuccess(productId);
                        }
                    })
                    .addOnFailureListener(e -> {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(e.getMessage());
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    });
        }
    }

    public void updateProduct(String productId, Product product, OnProductUpdatedListener listener) {
        loadingLiveData.setValue(true);
        product.setId(productId);

        productsRef.child(productId).setValue(product)
                .addOnSuccessListener(aVoid -> {
                    loadingLiveData.setValue(false);
                    if (listener != null) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    loadingLiveData.setValue(false);
                    errorLiveData.setValue(e.getMessage());
                    if (listener != null) {
                        listener.onFailure(e.getMessage());
                    }
                });
    }

    public void deleteProduct(String productId, OnProductDeletedListener listener) {
        loadingLiveData.setValue(true);

        productsRef.child(productId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    loadingLiveData.setValue(false);
                    if (listener != null) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    loadingLiveData.setValue(false);
                    errorLiveData.setValue(e.getMessage());
                    if (listener != null) {
                        listener.onFailure(e.getMessage());
                    }
                });
    }

    public void getProductById(String productId, MutableLiveData<Product> productLiveData) {
        loadingLiveData.setValue(true);

        productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingLiveData.setValue(false);
                Product product = dataSnapshot.getValue(Product.class);
                if (product != null) {
                    product.setId(dataSnapshot.getKey());
                }
                productLiveData.setValue(product);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(databaseError.getMessage());
            }
        });
    }
    
    public interface OnProductAddedListener {
        void onSuccess(String productId);
        void onFailure(String error);
    }

    public interface OnProductUpdatedListener {
        void onSuccess();
        void onFailure(String error);
    }

    public interface OnProductDeletedListener {
        void onSuccess();
        void onFailure(String error);
    }
}








