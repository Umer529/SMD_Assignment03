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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Signup Fragment with Firebase Authentication
 * Users can create account and select account type (Buyer/Seller)
 */
public class signupFragment extends Fragment {
    Button btn_signup;
    TextInputEditText tiet_email, tiet_password, tiet_Cpassword;
    TextInputEditText tiet_name, tiet_phone, tiet_address, tiet_dob, tiet_country;
    Spinner spinner_gender, spinner_accountType;
    ProgressBar progressBar;

    AuthViewModel authViewModel;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    public signupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        btn_signup = view.findViewById(R.id.btn_signup);
        tiet_email = view.findViewById(R.id.tiet_username);
        tiet_password = view.findViewById(R.id.tiet_password);
        tiet_Cpassword = view.findViewById(R.id.tiet_Cpassword);
        progressBar = view.findViewById(R.id.progressBar);
        
        // Additional fields
        tiet_name = view.findViewById(R.id.tiet_name);
        tiet_phone = view.findViewById(R.id.tiet_phone);
        tiet_address = view.findViewById(R.id.tiet_address);
        tiet_dob = view.findViewById(R.id.tiet_dob);
        tiet_country = view.findViewById(R.id.tiet_country);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        spinner_accountType = view.findViewById(R.id.spinner_accountType);
        
        sPref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        editor = sPref.edit();

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Setup spinners
        setupSpinners();

        // Observe loading state
        authViewModel.getLoadingState().observe(getViewLifecycleOwner(), state -> {
            if ("Registering...".equals(state)) {
                progressBar.setVisibility(View.VISIBLE);
                btn_signup.setEnabled(false);
            } else if ("Success".equals(state)) {
                progressBar.setVisibility(View.GONE);
                btn_signup.setEnabled(true);
            } else if ("Error".equals(state)) {
                progressBar.setVisibility(View.GONE);
                btn_signup.setEnabled(true);
            }
        });

        // Observe user data
        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Save to SharedPreferences
                editor.putString("user.uid", user.getUid());
                editor.putString("user.email", user.getEmail());
                editor.putString("user.accountType", user.getAccountType());
                editor.putBoolean("user.isLogin", true);
                editor.apply();

                Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Redirect based on account type
                if ("Seller".equalsIgnoreCase(user.getAccountType())) {
                    startActivity(new Intent(requireContext(), SellerDashboard.class));
                } else {
                    startActivity(new Intent(requireContext(), MainActivity2.class));
                }
                getActivity().finish();
            }
        });

        // Observe error messages
        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        btn_signup.setOnClickListener(v -> signup());

        view.setOnTouchListener((v, event) -> {
            hideSoftKeyboard(view);
            return false;
        });
    }

    private void setupSpinners() {
        // Gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(genderAdapter);

        // Account type spinner
        ArrayAdapter<CharSequence> accountTypeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.account_type_options, android.R.layout.simple_spinner_item);
        accountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_accountType.setAdapter(accountTypeAdapter);
    }

    void signup() {
        String email = tiet_email.getText().toString().trim();
        String password = tiet_password.getText().toString().trim();
        String confirmPassword = tiet_Cpassword.getText().toString().trim();
        String name = tiet_name.getText().toString().trim();
        String phone = tiet_phone.getText().toString().trim();
        String address = tiet_address.getText().toString().trim();
        String dob = tiet_dob.getText().toString().trim();
        String country = tiet_country.getText().toString().trim();
        String gender = spinner_gender.getSelectedItem().toString();
        String accountType = spinner_accountType.getSelectedItem().toString();

        // Validation
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            name.isEmpty() || phone.isEmpty() || address.isEmpty() || 
            dob.isEmpty() || country.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }

        // Perform signup
        authViewModel.signUp(email, password, name, phone, address, gender, dob, country, accountType);
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}