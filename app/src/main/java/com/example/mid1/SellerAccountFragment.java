package com.example.mid1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Seller Account Fragment
 * Displays seller account details
 */
public class SellerAccountFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvAddress, tvCountry;
    private AuthViewModel authViewModel;

    public SellerAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tv_seller_name_detail);
        tvEmail = view.findViewById(R.id.tv_seller_email_detail);
        tvPhone = view.findViewById(R.id.tv_seller_phone_detail);
        tvAddress = view.findViewById(R.id.tv_seller_address_detail);
        tvCountry = view.findViewById(R.id.tv_seller_country_detail);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Load data from SharedPreferences
        loadSellerData();

        // Observe user data from Firebase
        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                displaySellerData(user);
            }
        });
    }

    private void loadSellerData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        String name = prefs.getString("user.name", "Name not available");
        String email = prefs.getString("user.email", "Email not available");
        String phone = prefs.getString("user.phone", "Phone not available");
        String address = prefs.getString("user.address", "Address not available");
        String country = prefs.getString("user.country", "Country not available");

        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvCountry.setText(country);

        // Fetch fresh data
        String uid = prefs.getString("user.uid", "");
        if (!uid.isEmpty()) {
            authViewModel.fetchUserData(uid);
        }
    }

    private void displaySellerData(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        tvAddress.setText(user.getAddress());
        tvCountry.setText(user.getCountry());
    }
}

