package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Supplier;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;

public class AdminAllSuppliers extends AppCompatActivity {
    static final String SHARED_PREF = "PrefDemo"; // SharedPreferences file name
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String userType;
    int uid;
    RecyclerView recyclerView;
    List<Supplier> allSuppliers;
    SupplierDao supplierDao;
    LinearLayout btnDashboard, btnSupplier, btnOrder, btnCategory, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_all_suppliers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve USERID and USERTYPE
        try {
            Bundle extras = getIntent().getExtras();
            uid = extras.getInt("USERID", 0);
            userType = extras.getString("USERTYPE", "");
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving user information: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Retrieve UI components
        btnDashboard = findViewById(R.id.nav_dashboard);
        btnSupplier = findViewById(R.id.nav_supplier);
        btnOrder = findViewById(R.id.nav_order);
        btnCategory = findViewById(R.id.nav_cat);
        btnLogout = findViewById(R.id.nav_logout);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.r_view_all_suppliers);
        // Set LayoutManager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve orders from database
        try {
            supplierDao = AppDB.getInstance(this).supplierDao(); // Access database DAO
            allSuppliers = supplierDao.getAllSuppliers(); // Get all order details
            // Set adapter for RecyclerView to display orders
            SupplierRVAdapter adapter = new SupplierRVAdapter(allSuppliers,this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving orders: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminAllSuppliers.this, "Dashboard clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminAllSuppliers.this, AdminDashboard.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminAllSuppliers.this, "Error navigating to All Suppliers  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminAllSuppliers.this, "Supplier clicked!", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(AdminAllSuppliers.this, "Error navigating to All Suppliers  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminAllSuppliers.this, "Supplier clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminAllSuppliers.this, AdminAllOrders.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminAllSuppliers.this, "Error navigating to All Orders  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(AdminAllSuppliers.this, "Supplier clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminAllSuppliers.this, Category.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AdminAllSuppliers.this, "Error navigating to Category  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPref(view);
                try {
                    // Navigate back to the SignIn page after logout
                    Intent intent = new Intent(AdminAllSuppliers.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
                    startActivity(intent);
                    // Finish current activity so user can't go back to it
                    finish();
                } catch (Exception e) {
                    // Handle exception if there is an error with logout
                    Toast.makeText(AdminAllSuppliers.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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