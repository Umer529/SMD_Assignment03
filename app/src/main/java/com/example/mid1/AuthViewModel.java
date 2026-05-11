package com.example.mid1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for authentication operations
 * Provides data to UI while surviving configuration changes
 */
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = AuthRepository.getInstance();
    }

    // LiveData fields
    public LiveData<String> getLoadingState() {
        return authRepository.loadingStateLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return authRepository.errorMessageLiveData;
    }

    public LiveData<User> getUser() {
        return authRepository.userLiveData;
    }

    public LiveData<Boolean> getAuthState() {
        return authRepository.authStateLiveData;
    }

    /**
     * Perform signup
     */
    public void signUp(String email, String password, String name, String phone,
                       String address, String gender, String dob, String country, String accountType) {
        authRepository.signUp(email, password, name, phone, address, gender, dob, country, accountType);
    }

    /**
     * Perform login
     */
    public void signIn(String email, String password) {
        authRepository.signIn(email, password);
    }

    /**
     * Fetch user data
     */
    public void fetchUserData(String uid) {
        authRepository.fetchUserData(uid);
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }

    /**
     * Logout
     */
    public void signOut() {
        authRepository.signOut();
    }

    /**
     * Get current user
     */
    public String getCurrentUserId() {
        return authRepository.getCurrentUser() != null ? authRepository.getCurrentUser().getUid() : null;
    }
}

