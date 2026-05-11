package com.example.mid1;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Repository layer for authentication operations
 * Handles Firebase Auth and Realtime Database operations
 */
public class AuthRepository {
    private static AuthRepository instance;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    // LiveData for UI updates
    public MutableLiveData<String> loadingStateLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> authStateLiveData = new MutableLiveData<>();

    private AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Register/Signup user with Firebase Auth and store user data in Realtime Database
     */
    public void signUp(String email, String password, String name, String phone,
                       String address, String gender, String dob, String country, String accountType) {
        loadingStateLiveData.setValue("Registering...");
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            
                            // Create User object with all data
                            User user = new User(email, name, phone, address, gender, dob, country, accountType);
                            user.setUid(uid);
                            
                            // Store in Firebase Realtime Database
                            databaseReference.child(uid).setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        loadingStateLiveData.setValue("Success");
                                        userLiveData.setValue(user);
                                        authStateLiveData.setValue(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        errorMessageLiveData.setValue("Failed to save user data: " + e.getMessage());
                                        loadingStateLiveData.setValue("Error");
                                    });
                        }
                    } else {
                        String errorMsg = task.getException() != null ? 
                                task.getException().getMessage() : "Signup failed";
                        errorMessageLiveData.setValue(errorMsg);
                        loadingStateLiveData.setValue("Error");
                    }
                });
    }

    /**
     * Login user with Firebase Auth
     */
    public void signIn(String email, String password) {
        loadingStateLiveData.setValue("Logging in...");
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            // Fetch user data from database
                            fetchUserData(uid);
                        }
                    } else {
                        String errorMsg = task.getException() != null ? 
                                task.getException().getMessage() : "Login failed";
                        errorMessageLiveData.setValue(errorMsg);
                        loadingStateLiveData.setValue("Error");
                    }
                });
    }

    /**
     * Fetch user data from Firebase Realtime Database
     */
    public void fetchUserData(String uid) {
        databaseReference.child(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = task.getResult().getValue(User.class);
                        if (user != null) {
                            user.setUid(uid);
                            loadingStateLiveData.setValue("Success");
                            userLiveData.setValue(user);
                            authStateLiveData.setValue(true);
                        } else {
                            errorMessageLiveData.setValue("User data not found");
                            loadingStateLiveData.setValue("Error");
                        }
                    } else {
                        errorMessageLiveData.setValue("Failed to fetch user data: " + task.getException().getMessage());
                        loadingStateLiveData.setValue("Error");
                    }
                });
    }

    /**
     * Get current logged-in user
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Check if user is already logged in
     */
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * Logout user
     */
    public void signOut() {
        firebaseAuth.signOut();
        authStateLiveData.setValue(false);
    }
}

