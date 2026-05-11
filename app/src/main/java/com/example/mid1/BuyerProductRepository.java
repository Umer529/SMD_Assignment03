package com.example.mid1;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerProductRepository {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productsRef;
    private MutableLiveData<List<items>> productsLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Integer> loadingStateLiveData;
    private ValueEventListener valueEventListener;

    public static final int LOADING_STATE_LOADING = 0;
    public static final int LOADING_STATE_SUCCESS = 1;
    public static final int LOADING_STATE_ERROR = 2;
    public static final int LOADING_STATE_EMPTY = 3;

    public BuyerProductRepository() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.productsRef = firebaseDatabase.getReference("products");
        this.productsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.loadingStateLiveData = new MutableLiveData<>(LOADING_STATE_LOADING);
    }

    public MutableLiveData<List<items>> getProducts() {
        return productsLiveData;
    }

    public MutableLiveData<String> getError() {
        return errorLiveData;
    }

    public MutableLiveData<Integer> getLoadingState() {
        return loadingStateLiveData;
    }

    public void attachProductListener() {
        if (valueEventListener != null) {
            return;
        }

        loadingStateLiveData.setValue(LOADING_STATE_LOADING);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<items> products = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        String name = snapshot.child("name").getValue(String.class);
                        String price = snapshot.child("price").getValue(String.class);
                        String description = snapshot.child("description").getValue(String.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        int imageResource = R.drawable.sony_premium_1;

                        if (name != null && price != null && description != null) {
                            items item = new items(name, price, imageResource, description, "", false);
                            products.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (products.isEmpty()) {
                    loadingStateLiveData.setValue(LOADING_STATE_EMPTY);
                } else {
                    loadingStateLiveData.setValue(LOADING_STATE_SUCCESS);
                }
                productsLiveData.setValue(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingStateLiveData.setValue(LOADING_STATE_ERROR);
                errorLiveData.setValue(databaseError.getMessage());
            }
        };

        productsRef.addValueEventListener(valueEventListener);
    }

    public void detachProductListener() {
        if (valueEventListener != null) {
            productsRef.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }
}

