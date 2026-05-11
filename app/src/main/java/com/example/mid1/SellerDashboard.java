package com.example.mid1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

/**
 * Seller Dashboard Activity
 * Main activity for sellers with drawer navigation and theme support
 */
public class SellerDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private ThemeManager themeManager;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize theme manager before setContentView
        themeManager = ThemeManager.getInstance(this);
        
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setupDrawerNavigation();
        loadSellerProfile();
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new SellerHomeFragment(), "Home");
        }
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        
        setSupportActionBar(toolbar);
        sPref = getSharedPreferences("user", MODE_PRIVATE);
    }

    private void setupDrawerNavigation() {
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                loadFragment(new SellerHomeFragment(), "Home");
            } else if (itemId == R.id.nav_order_history) {
                loadFragment(new OrderHistoryFragment(), "Order History");
            } else if (itemId == R.id.nav_account) {
                loadFragment(new SellerAccountFragment(), "Account");
            } else if (itemId == R.id.nav_theme_toggle) {
                toggleTheme();
            } else if (itemId == R.id.nav_logout) {
                logout();
            }
            
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        toolbar.setTitle(title);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadSellerProfile() {
        String sellerName = sPref.getString("user.name", "Seller");
        String sellerEmail = sPref.getString("user.email", "seller@example.com");
        
        // Update drawer header
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView != null && navView.getHeaderCount() > 0) {
            TextView tvName = navView.getHeaderView(0).findViewById(R.id.tv_drawer_name);
            TextView tvEmail = navView.getHeaderView(0).findViewById(R.id.tv_drawer_email);
            
            if (tvName != null) tvName.setText(sellerName);
            if (tvEmail != null) tvEmail.setText(sellerEmail);
        }
    }

    private void toggleTheme() {
        themeManager.toggleTheme();
        // Recreate activity to apply theme
        recreate();
    }

    private void logout() {
        // Clear login state
        sPref.edit().putBoolean("user.isLogin", false).apply();
        
        // Navigate to splash screen
        startActivity(new Intent(this, splash.class));
        finish();
    }
}


