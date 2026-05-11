Convert the current SharedPreferences-based authentication system into a proper Firebase Authentication system.

Requirements:
1. Use FirebaseAuth.
2. Implement:
    - createUserWithEmailAndPassword
    - signInWithEmailAndPassword
3. During signup:
    - Create firebase auth account
    - Store additional user information in Firebase Realtime Database
4. Store:
    - uid
    - name
    - phone
    - address
    - gender
    - dob
    - country
    - accountType
5. After login:
    - Fetch accountType from Firebase
    - Redirect user based on role:
        - Buyer → Buyer Dashboard
        - Seller → Seller Dashboard
6. Store in SharedPreferences:
    - uid
    - username
    - accountType
    - isLoggedIn
7. Auto-login user on app reopen.
8. Add proper validation and loading states.
9. Use MVVM architecture.
10. Add repository layer for auth operations.