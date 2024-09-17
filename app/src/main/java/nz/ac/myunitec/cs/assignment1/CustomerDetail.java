package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CustomerDetail extends AppCompatActivity {
    int uid;
    CustomerDao customerDao;
    ImageView img, btnEditImg, btnBack;
    EditText name, email, mobile;
    TextView  btnSaveChange;
    TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views for displaying customer information
        img = findViewById(R.id.img);
        name = findViewById(R.id.et_change_name);
        email = findViewById(R.id.et_change_email);
        mobile = findViewById(R.id.et_change_mobile);
        password = findViewById(R.id.new_password);
        btnBack = findViewById(R.id.back_button);
        btnEditImg = findViewById(R.id.btn_edit_profile_pic);
        btnSaveChange = findViewById(R.id.btn_save_changes);

        // Disable password and email fields
        password.setEnabled(false);
        password.setFocusable(false);
        password.setFocusableInTouchMode(false);
        email.setEnabled(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);

        try{
            // Get user ID from intent
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                uid = extras.getInt("USERID", 0); // Retrieve user ID if available
            } else {
                uid = 0; // Default to 0 if no user ID is provided
            }
        }catch (Exception e) {
            Toast.makeText(CustomerDetail.this, "Failed to retrieve user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            uid = 0;
        }
        try{
            // Get customer data from database
            customerDao = AppDB.getInstance(this).customerDao();
            Customer customer = customerDao.getCustomer(uid);

            // Display customer details (profile picture, name, email, mobile number)
            if (customer.getC_img() != null) {
                img.setImageResource(customer.getC_img()); // Set customer's profile picture if available
            } else {
                img.setImageResource(R.drawable.pic_profile); // Set default profile picture if not available
            }
            name.setText(customer.getName());
            email.setText(customer.getEmail());
            if (customer.getMobile_num() != null) {
                mobile.setText(customer.getMobile_num());
            } else {
                mobile.setText("null");
            }

            btnSaveChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get updated information
                    String newName = name.getText().toString().trim();
                    String newMobile = mobile.getText().toString().trim();

                    // Check if any fields are empty
                    if (newName.isEmpty() || newMobile.isEmpty()) {
                        Toast.makeText(CustomerDetail.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            customerDao.updateCustomerDetail(newName, newMobile, customer.getCid());
                            Toast.makeText(CustomerDetail.this, "Changes saved successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CustomerDetail.this, CustomerProfile.class);
                            intent.putExtra("USERID", uid);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(CustomerDetail.this, "Error saving changes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (NullPointerException e) {
            // Handle exception if user ID could not be retrieved
            Toast.makeText(this, "Failed to retrieve customer information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            uid = 0;
        } catch (Exception e) {
            Toast.makeText(CustomerDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Handle back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    onBackPressed();
                }catch (Exception e) {
                    // Handle exception if navigation to Edit Customer Profile fails
                    Toast.makeText(CustomerDetail.this, "Error navigating to Edit Customer Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Handle edit image button click
        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(CustomerDetail.this, "Edit Image feature coming soon", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    // Handle exception if navigation to Edit Customer Profile fails
                    Toast.makeText(CustomerDetail.this, "Error navigating to Edit Image " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}