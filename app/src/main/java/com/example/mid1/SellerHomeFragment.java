package com.example.mid1;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SellerHomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private FloatingActionButton fabAddProduct;
    private TextView tvTotalProducts;
    private TextView tvTotalStock;
    private ProgressBar pbLoading;
    private LinearLayout llEmptyState;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

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

        init(view);
        setupRecyclerView();
        setupProductAdapter();
        setupObservers();
        setupFABListener();
        
        // Load products
        productViewModel.loadSellerProducts();
    }

    private void init(View view) {
        rvProducts = view.findViewById(R.id.rv_products);
        fabAddProduct = view.findViewById(R.id.fab_add_product);
        tvTotalProducts = view.findViewById(R.id.tv_total_products);
        tvTotalStock = view.findViewById(R.id.tv_total_stock);
        pbLoading = view.findViewById(R.id.pb_loading);
        llEmptyState = view.findViewById(R.id.ll_empty_state);
        
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvProducts.setLayoutManager(layoutManager);
    }

    private void setupProductAdapter() {
        productAdapter = new ProductAdapter(getContext());
        rvProducts.setAdapter(productAdapter);

        // Handle product click
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(getContext(), ProductDescriptionActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onProductEdit(Product product) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivityForResult(intent, 1);
            }
        });

        // Handle product delete
        productAdapter.setOnProductDeleteListener(product -> {
            showDeleteConfirmation(product);
        });
    }

    private void setupObservers() {
        // Products observer
        productViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                updateUI(products);
            }
        });

        // Loading observer
        productViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Error observer
        productViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showErrorDialog(error);
            }
        });
    }

    private void updateUI(List<Product> products) {
        if (products.isEmpty()) {
            rvProducts.setVisibility(View.GONE);
            llEmptyState.setVisibility(View.VISIBLE);
            tvTotalProducts.setText("0");
            tvTotalStock.setText("0");
        } else {
            rvProducts.setVisibility(View.VISIBLE);
            llEmptyState.setVisibility(View.GONE);
            
            // Update stats
            tvTotalProducts.setText(String.valueOf(products.size()));
            int totalStock = 0;
            for (Product product : products) {
                totalStock += product.getStock();
            }
            tvTotalStock.setText(String.valueOf(totalStock));
            
            // Update adapter
            productAdapter.setProducts(products);
        }
    }

    private void setupFABListener() {
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProductActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            // Reload products after adding/editing
            productViewModel.loadSellerProducts();
        }
    }

    private void showDeleteConfirmation(Product product) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteProduct(product);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteProduct(Product product) {
        productViewModel.deleteProduct(product.getId(),
                new ProductRepository.OnProductDeletedListener() {
                    @Override
                    public void onSuccess() {
                        productViewModel.loadSellerProducts();
                    }

                    @Override
                    public void onFailure(String error) {
                        showErrorDialog("Failed to delete product: " + error);
                    }
                });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}






