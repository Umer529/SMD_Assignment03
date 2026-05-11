package com.example.mid1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Seller Home Fragment
 * Displays seller home dashboard
 */
public class SellerHomeFragment extends Fragment {

    private TextView tvWelcome;
    private Button btnAddProduct, btnViewProducts;

    public SellerHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWelcome = view.findViewById(R.id.tv_seller_welcome);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        btnViewProducts = view.findViewById(R.id.btn_view_products);

        btnAddProduct.setOnClickListener(v -> {
            // TODO: Implement add product
        });

        btnViewProducts.setOnClickListener(v -> {
            // TODO: Implement view products
        });
    }
}

