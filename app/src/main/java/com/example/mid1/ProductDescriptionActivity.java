package com.example.mid1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import androidx.lifecycle.ViewModelProvider;

/**
 * Activity for displaying product details
 */
public class ProductDescriptionActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductCategory;
    private TextView tvProductPrice;
    private TextView tvProductStock;
    private TextView tvProductDescription;
    private MaterialButton btnBack;
    private MaterialButton btnEdit;
    private ProgressBar pbLoading;

    private ProductViewModel productViewModel;
    private Product currentProduct;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_description);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        init();
        setupListeners();
        loadProduct();
    }

    private void init() {
        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductStock = findViewById(R.id.tv_product_stock);
        tvProductDescription = findViewById(R.id.tv_product_description);
        btnBack = findViewById(R.id.btn_back);
        btnEdit = findViewById(R.id.btn_edit);
        pbLoading = findViewById(R.id.pb_loading);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productId = getIntent().getStringExtra("product_id");
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnEdit.setOnClickListener(v -> {
            if (currentProduct != null) {
                Intent intent = new Intent(this, AddProductActivity.class);
                intent.putExtra("product_id", currentProduct.getId());
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadProduct() {
        if (productId == null) {
            finish();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        productViewModel.getProductById(productId);

        productViewModel.getCurrentProduct().observe(this, product -> {
            if (product != null) {
                currentProduct = product;
                displayProduct();
                pbLoading.setVisibility(View.GONE);
            }
        });

        productViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                pbLoading.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void displayProduct() {
        if (currentProduct == null) {
            return;
        }

        tvProductName.setText(currentProduct.getName());
        tvProductCategory.setText(currentProduct.getCategory());
        tvProductPrice.setText(String.format("$%.2f", currentProduct.getPrice()));
        tvProductStock.setText(String.valueOf(currentProduct.getStock()));
        tvProductDescription.setText(currentProduct.getDescription());

        // Load image using Glide
        if (currentProduct.getImageUrl() != null && !currentProduct.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentProduct.getImageUrl())
                    .placeholder(R.drawable.ic_menu)
                    .error(R.drawable.ic_menu)
                    .into(ivProductImage);
        } else {
            ivProductImage.setImageResource(R.drawable.ic_menu);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Reload product after edit
            pbLoading.setVisibility(View.VISIBLE);
            productViewModel.getProductById(productId);
        }
    }
}



