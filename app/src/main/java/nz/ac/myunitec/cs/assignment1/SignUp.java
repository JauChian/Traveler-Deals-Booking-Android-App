package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Customer;
import nz.ac.myunitec.cs.assignment1.db.CustomerDao;
import nz.ac.myunitec.cs.assignment1.db.Supplier;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;
import nz.ac.myunitec.cs.assignment1.db.User;
import nz.ac.myunitec.cs.assignment1.db.UserDao;

public class SignUp extends AppCompatActivity {
    UserDao userDao;
    CustomerDao customerDao;
    SupplierDao supplierDao;
    RadioGroup userTypeRadioGroup;
    RadioButton selectedBtn;
    EditText tv_email;
    TextInputEditText tv_password, tv_confirmPassword;
    TextView btnSignUp,btnLogInHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize DAO to interact with the database
        userDao = AppDB.getInstance(this).userDao();
        customerDao = AppDB.getInstance(this).customerDao();
        supplierDao = AppDB.getInstance(this).supplierDao();

        tv_email = findViewById(R.id.ed_email);
        tv_password = findViewById(R.id.new_password_edit_text);
        tv_confirmPassword = findViewById(R.id.confirm_password_edit_text);
        userTypeRadioGroup = findViewById(R.id.user_type_radio_group);
        btnSignUp = findViewById(R.id.sign_up_button);
        btnLogInHere = findViewById(R.id.log_in_here);

        // Set up a click listener for the "LOGIN Here" button to navigate to the SignIn activity
        btnLogInHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Navigate to the SignIn activity
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);

                } catch (Exception e) {
                    // Handle any exceptions that might occur while starting the activity
                    Toast.makeText(SignUp.this, "Failed to navigate to Sign In: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Sign Up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Retrieve email and password from the EditText fields
                    String email = tv_email.getText().toString().trim().toLowerCase();
                    String password1 = tv_password.getText().toString();
                    String password2 = tv_confirmPassword.getText().toString();

                    // Get the selected user type
                    int selectedRadioButtonId = userTypeRadioGroup.getCheckedRadioButtonId();
                    if (selectedRadioButtonId == -1) {
                        // User type not selected
                        Toast.makeText(SignUp.this, "Please select a user type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get the selected RadioButton
                    selectedBtn = findViewById(selectedRadioButtonId);
                    String userType = selectedBtn.getText().toString().toLowerCase();

                    // Validate if all fields are filled
                    if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                        Toast.makeText(SignUp.this, "Please fill all information", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate if both passwords match
                    if (!password1.equals(password2)) {
                        Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        // Create a new User object
                        User user = new User(userType, email, password1);
                        userDao.insertUser(user);

                        // Retrieve the uid of the newly inserted user using the email
                        int uid = userDao.getUserIdByEmail(email);
                        if (userType.equals("customer")) {
                            Customer customer = new Customer(userType, email, password1, null, null, null, uid);
                            customerDao.insertCustomer(customer);
                        } else if (userType.equals("supplier")) {
                            Supplier supplier = new Supplier(userType,email,password1,null,null,null,null,uid);
                            supplierDao.insertSupplier(supplier);
                        }
                        // Show a message indicating that sign-up was successful
                        Toast.makeText(SignUp.this, "Sign up successful!", Toast.LENGTH_LONG).show();

                        // After registration success, navigate to the Sign In page
                        Intent intent = new Intent(SignUp.this, SignIn.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        // Handle any exception that might occur during the database insertion
                        Toast.makeText(SignUp.this, "Failed to sign up: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {
                    // Handle any potential NullPointerException
                    Toast.makeText(SignUp.this, "An unexpected error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Handle any other general exceptions
                    Toast.makeText(SignUp.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}