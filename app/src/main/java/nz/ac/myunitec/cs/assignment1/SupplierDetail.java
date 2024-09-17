package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import nz.ac.myunitec.cs.assignment1.db.Supplier;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;

public class SupplierDetail extends AppCompatActivity {
    ImageView btnBack,btnAddImg,img,btnLogout;
    TextView btnEdit;
    EditText companyName,email, personName, mobile;
    int uid,sid;
    TextInputEditText password;
    String userType;
    SupplierDao supplierDao;
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnBack = findViewById(R.id.back);
        btnEdit = findViewById(R.id.btn_edit);
        btnAddImg = findViewById(R.id.btn_edit_profile_pic);
        btnLogout = findViewById(R.id.logout);
        img = findViewById(R.id.img_profile);
        password = findViewById(R.id.new_password);
        companyName = findViewById(R.id.et_change_name);
        email = findViewById(R.id.et_change_email);
        personName = findViewById(R.id.et_representative_name);
        mobile = findViewById(R.id.et_change_mobile);

        // Disable password and email fields
        password.setEnabled(false);
        password.setFocusable(false);
        password.setFocusableInTouchMode(false);
        email.setEnabled(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);

        try {
            // Initialize SharedPreferences and Editor
            sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            editor = sharedPref.edit();

            // Get userType and uid from SharedPreferences
            userType = sharedPref.getString("USERTYPE", "");
            uid = sharedPref.getInt("USERID", 0);

            // If user didn't save it in SharedPreferences, get it from Intent extras
            if (uid == 0) {
                // If no USERID in SharedPreferences, try to get it from Intent Extras
                Bundle extras = getIntent().getExtras();
                uid = extras.getInt("USERID", 0);
                sid = extras.getInt("SID", 0);
                userType = extras.getString("USERTYPE", "");
            }

            // Log userType and uid
            Log.d("SupplierDetail", "UserType: " + userType);
            Log.d("SupplierDetail", "UserID: " + uid);
            Log.d("SupplierDetail", "Received SID: " + sid);

            // Initialize the SupplierDao
            supplierDao = AppDB.getInstance(this).supplierDao();

            //Check userType
            enableEdit(userType);
            Supplier supplier;
            if(userType.equals("supplier")&&(sid!=0)){
                supplier = supplierDao.getSupplierbySid(sid);
            }else{
                // Get supplier data from the database
                supplier = supplierDao.getSupplierByUid(uid);
            }

            if (supplier != null) {
                // Display supplier details
                if (supplier.getS_img() != null) {
                    img.setImageResource(supplier.getS_img()); // Display supplier's profile image
                } else {
                    img.setImageResource(R.drawable.pic_profile); // Default profile image
                }

                companyName.setText(supplier.getCompanyName());
                personName.setText(supplier.getRepresentativeName());
                email.setText(supplier.getEmail());
                if (supplier.getContactNum() != null) {
                    mobile.setText(supplier.getContactNum());
                } else {
                    mobile.setText("null");
                }
            } else {
                Toast.makeText(SupplierDetail.this, "No supplier data found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle errors while fetching data
            Toast.makeText(SupplierDetail.this, "Error loading supplier data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Event handler for Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the previous activity
                onBackPressed();
            }
        });

        // Event handler for Edit Profile button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    // Read input data from EditText fields
                    String updatedCompanyName = companyName.getText().toString().trim();
                    String updatedPersonName = personName.getText().toString().trim();
                    String updatedMobile = mobile.getText().toString().trim();

                    // Validate input and save the updated data
                    if (updatedCompanyName.isEmpty() || updatedPersonName.isEmpty() || updatedMobile.isEmpty()) {
                        Toast.makeText(SupplierDetail.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(SupplierDetail.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupplierDetail.this, SupplierDashboard.class);
                        intent.putExtra("USERID", uid);
                        startActivity(intent);
                    }
                }catch (Exception e) {
                    Toast.makeText(SupplierDetail.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Event handler for Edit Profile Picture button
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SupplierDetail.this, "Edit profile picture clicked, new feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPref(view);
                try {
                    // Navigate back to the SignIn page after logout
                    Intent intent = new Intent(SupplierDetail.this, MainActivity.class);
                    // Clear activity stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // Finish current activity so user can't go back to it
                    finish();
                } catch (Exception e) {
                    // Handle exception if there is an error with logout
                    Toast.makeText(SupplierDetail.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void clearPref(View view) {
        editor.clear();
        editor.apply();
        Toast.makeText(getApplicationContext(),
                "All Preferences Cleared",
                Toast.LENGTH_LONG).show();
    }

    void enableEdit(String userType) {
        if (userType.equals("supplier")) {
            companyName.setEnabled(true);
            personName.setEnabled(true);
            mobile.setEnabled(true);
            btnLogout.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnAddImg.setVisibility(View.VISIBLE);
        } else {
            companyName.setEnabled(false);
            personName.setEnabled(false);
            mobile.setEnabled(false);
            btnLogout.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnAddImg.setVisibility(View.GONE);
        }
    }

}