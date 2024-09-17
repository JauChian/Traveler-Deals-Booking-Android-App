package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import nz.ac.myunitec.cs.assignment1.db.UserDao;

public class AdminDashboard extends AppCompatActivity {
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    LinearLayout btnDashboard, btnSupplier,btnOrder,btnCategory,btnLogout;
    String userType;
    int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admindashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnDashboard = findViewById(R.id.nav_dashboard);
        btnSupplier = findViewById(R.id.nav_supplier);
        btnOrder = findViewById(R.id.nav_order);
        btnCategory = findViewById(R.id.nav_cat);
        btnLogout = findViewById(R.id.nav_logout);

        try{
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
                if (extras != null) {
                    uid = extras.getInt("USERID", 0); // Get USERID from Intent
                }
            }
            userType = "admin";
        }catch (Exception e) {
            Toast.makeText(this, "Error getting user information " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminDashboard.this, "Dashboard clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminDashboard.this, "Supplier clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminDashboard.this, AdminAllSuppliers.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminDashboard.this, "Error navigating to All Suppliers  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminDashboard.this, "Order clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminDashboard.this, AdminAllOrders.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminDashboard.this, "Error navigating to All Orders  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminDashboard.this, "Category clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminDashboard.this, Category.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminDashboard.this, "Error navigating to Category  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPref(view);
                try {
                    // Navigate back to the SignIn page after logout
                    Intent intent = new Intent(AdminDashboard.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
                    startActivity(intent);
                    // Finish current activity so user can't go back to it
                    finish();
                } catch (Exception e) {
                    // Handle exception if there is an error with logout
                    Toast.makeText(AdminDashboard.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void clearPref(View view) {
        try {
            editor.clear();
            editor.apply();
            Toast.makeText(getApplicationContext(), "All Preferences Cleared", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error clearing preferences: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}