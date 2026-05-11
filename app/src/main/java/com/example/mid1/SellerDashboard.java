package com.example.mid1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Seller Dashboard Activity
 * Displays seller-specific features and information
 */
public class SellerDashboard extends AppCompatActivity {

    TextView tvSellerName, tvSellerEmail;
    Button btnLogout, btnAddProduct, btnViewProducts;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        tvSellerName = findViewById(R.id.tv_seller_name);
        tvSellerEmail = findViewById(R.id.tv_seller_email);
        btnLogout = findViewById(R.id.btn_seller_logout);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnViewProducts = findViewById(R.id.btn_view_products);

        sPref = getSharedPreferences("user", MODE_PRIVATE);

        // Display seller information from SharedPreferences
        String sellerName = sPref.getString("user.name", "Seller");
        String sellerEmail = sPref.getString("user.email", "seller@example.com");

        tvSellerName.setText("Welcome, " + sellerName);
        tvSellerEmail.setText("Email: " + sellerEmail);

        // Add Product button - placeholder
        btnAddProduct.setOnClickListener(v -> {
            // TODO: Implement add product functionality
        });

        // View Products button - placeholder
        btnViewProducts.setOnClickListener(v -> {
            // TODO: Implement view products functionality
        });

        // Logout button
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Clear login state
        sPref.edit().putBoolean("user.isLogin", false).apply();

        // Navigate to splash screen
        startActivity(new Intent(this, splash.class));
        finish();
    }
}

