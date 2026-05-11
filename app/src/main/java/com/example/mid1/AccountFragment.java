package com.example.mid1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

/**
 * Account fragment displaying user information from Firebase.
 * Also provides a logout button that clears the login state and returns to the splash screen.
 */
public class AccountFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvAddress, tvGender, tvDob, tvCountry, tvAccountType;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tv_account_name);
        tvEmail = view.findViewById(R.id.tv_account_email);
        tvPhone = view.findViewById(R.id.tv_account_phone);
        tvAddress = view.findViewById(R.id.tv_account_address);
        tvGender = view.findViewById(R.id.tv_account_gender);
        tvDob = view.findViewById(R.id.tv_account_dob);
        tvCountry = view.findViewById(R.id.tv_account_country);
        tvAccountType = view.findViewById(R.id.tv_account_type);
        progressBar = view.findViewById(R.id.progressBar_account);

        MaterialButton btnLogout = view.findViewById(R.id.btn_account_logout);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Load user data from SharedPreferences
        loadUserData();

        // Observe user data from Firebase
        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                displayUserData(user);
                // Save to SharedPreferences
                SharedPreferences prefs = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user.name", user.getName());
                editor.putString("user.phone", user.getPhone());
                editor.putString("user.address", user.getAddress());
                editor.putString("user.gender", user.getGender());
                editor.putString("user.dob", user.getDob());
                editor.putString("user.country", user.getCountry());
                editor.apply();
            }
        });

        // Logout: clear login state and navigate back to splash
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        String uid = prefs.getString("user.uid", "");
        String name = prefs.getString("user.name", "Name not available");
        String email = prefs.getString("user.email", "Email not available");
        String phone = prefs.getString("user.phone", "Phone not available");
        String address = prefs.getString("user.address", "Address not available");
        String gender = prefs.getString("user.gender", "Gender not available");
        String dob = prefs.getString("user.dob", "DOB not available");
        String country = prefs.getString("user.country", "Country not available");
        String accountType = prefs.getString("user.accountType", "Buyer");

        // Display data
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvGender.setText(gender);
        tvDob.setText(dob);
        tvCountry.setText(country);
        tvAccountType.setText(accountType);

        // Fetch fresh data from Firebase if uid exists
        if (!uid.isEmpty()) {
            authViewModel.fetchUserData(uid);
        }
    }

    private void displayUserData(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        tvAddress.setText(user.getAddress());
        tvGender.setText(user.getGender());
        tvDob.setText(user.getDob());
        tvCountry.setText(user.getCountry());
        tvAccountType.setText(user.getAccountType());
    }

    /**
     * Clears the user.isLogin flag and restarts the app from the splash screen.
     */
    private void logout() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().putBoolean("user.isLogin", false).apply();

        // Sign out from Firebase
        authViewModel.signOut();

        startActivity(new Intent(requireContext(), splash.class));
        requireActivity().finish();
    }
}

