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
import com.google.android.material.textfield.TextInputEditText;

import androidx.lifecycle.ViewModelProvider;

public class AddProductActivity extends AppCompatActivity {

    private TextInputEditText etProductName;
    private TextInputEditText etProductDescription;
    private TextInputEditText etProductCategory;
    private TextInputEditText etProductPrice;
    private TextInputEditText etProductStock;
    private TextInputEditText etProductImageUrl;
    private ImageView ivProductPreview;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;
    private ProgressBar pbLoading;
    private TextView tvError;

    private ProductViewModel productViewModel;
    private Product currentProduct;
    private String productId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        init();
        setupListeners();

        // Check if we're editing an existing product
        if (getIntent().hasExtra("product_id")) {
            isEditMode = true;
            productId = getIntent().getStringExtra("product_id");
            loadProduct(productId);
        }
    }

    private void init() {
        etProductName = findViewById(R.id.et_product_name);
        etProductDescription = findViewById(R.id.et_product_description);
        etProductCategory = findViewById(R.id.et_product_category);
        etProductPrice = findViewById(R.id.et_product_price);
        etProductStock = findViewById(R.id.et_product_stock);
        etProductImageUrl = findViewById(R.id.et_product_image_url);
        ivProductPreview = findViewById(R.id.iv_product_preview);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        pbLoading = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveProduct());
        btnCancel.setOnClickListener(v -> finish());

        etProductImageUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                loadImagePreview();
            }
        });
    }

    private void loadProduct(String productId) {
        productViewModel.getProductById(productId);
        productViewModel.getCurrentProduct().observe(this, product -> {
            if (product != null) {
                currentProduct = product;
                populateFields();
            }
        });

        productViewModel.getLoading().observe(this, isLoading -> {
            pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        productViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                showError(error);
            }
        });
    }

    private void populateFields() {
        if (currentProduct != null) {
            etProductName.setText(currentProduct.getName());
            etProductDescription.setText(currentProduct.getDescription());
            etProductCategory.setText(currentProduct.getCategory());
            etProductPrice.setText(String.valueOf(currentProduct.getPrice()));
            etProductStock.setText(String.valueOf(currentProduct.getStock()));
            etProductImageUrl.setText(currentProduct.getImageUrl());

            // Load image preview
            if (currentProduct.getImageUrl() != null && !currentProduct.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(currentProduct.getImageUrl())
                        .placeholder(R.drawable.ic_menu)
                        .error(R.drawable.ic_menu)
                        .into(ivProductPreview);
            }
        }
    }

    private void loadImagePreview() {
        String imageUrl = etProductImageUrl.getText().toString().trim();
        if (imageUrl.isEmpty()) {
            ivProductPreview.setImageResource(R.drawable.ic_menu);
            return;
        }

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(ivProductPreview);
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();
        String category = etProductCategory.getText().toString().trim();
        String imageUrl = etProductImageUrl.getText().toString().trim();

        // Validation
        if (!validateInputs(name, description, category, imageUrl)) {
            return;
        }

        try {
            double price = Double.parseDouble(etProductPrice.getText().toString().trim());
            int stock = Integer.parseInt(etProductStock.getText().toString().trim());

            if (price <= 0) {
                showError("Price must be greater than 0");
                return;
            }

            if (stock < 0) {
                showError("Stock cannot be negative");
                return;
            }

            Product product = new Product(name, description, category, price, stock, imageUrl);

            pbLoading.setVisibility(View.VISIBLE);

            if (isEditMode) {
                // Update existing product
                productViewModel.updateProduct(productId, product,
                        new ProductRepository.OnProductUpdatedListener() {
                            @Override
                            public void onSuccess() {
                                pbLoading.setVisibility(View.GONE);
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(String error) {
                                pbLoading.setVisibility(View.GONE);
                                showError(error);
                            }
                        });
            } else {
                // Add new product
                productViewModel.addProduct(product,
                        new ProductRepository.OnProductAddedListener() {
                            @Override
                            public void onSuccess(String productId) {
                                pbLoading.setVisibility(View.GONE);
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(String error) {
                                pbLoading.setVisibility(View.GONE);
                                showError(error);
                            }
                        });
            }
        } catch (NumberFormatException e) {
            showError("Invalid price or stock value");
        }
    }

    private boolean validateInputs(String name, String description, String category, String imageUrl) {
        if (name.isEmpty()) {
            showError("Product name is required");
            return false;
        }

        if (description.isEmpty()) {
            showError("Product description is required");
            return false;
        }

        if (category.isEmpty()) {
            showError("Category is required");
            return false;
        }

        if (etProductPrice.getText().toString().trim().isEmpty()) {
            showError("Price is required");
            return false;
        }

        if (etProductStock.getText().toString().trim().isEmpty()) {
            showError("Stock quantity is required");
            return false;
        }

        if (imageUrl.isEmpty()) {
            showError("Image URL is required");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}




