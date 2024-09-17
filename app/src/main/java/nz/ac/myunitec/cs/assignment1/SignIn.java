package nz.ac.myunitec.cs.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.User;
import nz.ac.myunitec.cs.assignment1.db.UserDao;

public class SignIn extends AppCompatActivity {
    UserDao userDao;
    CheckBox cbRemember;
    TextView btnSignIn, btnForget,btnSignUpHere;
    EditText txtEmail, txtPassword;
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize SharedPreferences and editor
        sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        editor = sharedPref.edit();

        // Initialize DAO to interact with the database
        userDao = AppDB.getInstance(this).userDao();

        // Link UI elements to their corresponding views
        txtEmail = findViewById(R.id.sign_in_email);
        txtPassword = findViewById(R.id.sign_in_password);
        btnSignIn = findViewById(R.id.sign_in_button);
        btnForget = findViewById(R.id.text_forgot_password);
        cbRemember = findViewById(R.id.checkbox_remember_me);
        btnSignUpHere = findViewById(R.id.sign_up_here);

        // Get the application context for Toast messages
        Context context = getApplicationContext();

        // Set up a click listener for the "Sign Up Here" button to navigate to the SignUp activity
        btnSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(SignIn.this, "Navigating to Sign Up", Toast.LENGTH_SHORT).show();
                    // Navigate to the SignUp activity
                    Log.d("SignIn", "Navigating to SignUp");
                    Intent intent = new Intent(SignIn.this, SignUp.class);
                    startActivity(intent);
                } catch (Exception e) {
                    // Handle any exceptions that might occur while starting the activity
                    Toast.makeText(SignIn.this, "Failed to navigate to Sign Up: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up a click listener for the "Sign In" button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Retrieve email and password from the EditText fields
                    String email = txtEmail.getText().toString().trim().toLowerCase();
                    String password = txtPassword.getText().toString().trim();

                    // Check if email or password is empty
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get user ID (uid) based on the provided email
                    int uid = userDao.getUserIdByEmail(email);
                    if (uid <= 0) {
                        // If the email is not found, display an error message
                        Toast.makeText(SignIn.this, "Email not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Retrieve stored password for the given uid from the database
                    String storedPassword = userDao.getPassword(uid);
                    if (storedPassword == null) {
                        Toast.makeText(SignIn.this, "No password found for this user", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Compare entered password with the stored password
                    if (!storedPassword.equals(password)) {
                        // If the password is incorrect, display an error message
                        Toast.makeText(SignIn.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Login successful, display success message
                    Toast.makeText(SignIn.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Get the user type to navigate to the correct dashboard
                    userType = userDao.getUserType(uid);
                    if (userType == null) {
                        Toast.makeText(SignIn.this, "User type not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Navigate to different dashboards based on user type
                    Intent intent = null;
                    switch (userType) {
                        case "customer":
                            intent = new Intent(SignIn.this, MainActivity.class);
                            break;
                        case "admin":
                            intent = new Intent(SignIn.this, AdminDashboard.class);
                            break;
                        case "supplier":
                            intent = new Intent(SignIn.this, SupplierDashboard.class);
                            break;
                        default:
                            Toast.makeText(SignIn.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                            return;
                    }

                    if (intent != null) {
                        intent.putExtra("USERID", uid);
                        intent.putExtra("USERTYPE",userType);
                        startActivity(intent);
                    }

                    // Handle "Remember Me" functionality after successful login
                    if (cbRemember.isChecked()) {
                        saveUserToPreferences(uid);
                    }

                } catch (Exception e) {
                    // Handle any unexpected exceptions
                    Toast.makeText(SignIn.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace(); // Optionally print the stack trace for debugging
                }
            }
        });
    }

    // Method to save user ID to SharedPreferences
    private void saveUserToPreferences(int uid) {
        try {
            editor.putInt("USERID", uid);
            editor.putString("USERTYPE",userType);
            boolean success = editor.commit();
            if (!success) {
                Toast.makeText(SignIn.this, "Failed to save preferences", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(SignIn.this, "An error occurred while saving preferences: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}