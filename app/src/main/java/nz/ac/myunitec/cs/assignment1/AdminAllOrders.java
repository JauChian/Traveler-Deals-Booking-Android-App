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
import nz.ac.myunitec.cs.assignment1.db.OrderDetail;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;

public class AdminAllOrders extends AppCompatActivity {
    static final String SHARED_PREF = "PrefDemo"; // SharedPreferences file name
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    LinearLayout btnDashboard, btnSupplier, btnOrder, btnCategory, btnLogout,btnNav;
    String userType;
    int uid;
    RecyclerView recyclerView;
    List<OrderDetail> allOrders;
    OrderDetailDao orderDetailDao;
    SupplierDao supplierDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge layout (for system bars)
        setContentView(R.layout.activity_admin_all_orders);
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
        btnNav = findViewById(R.id.bottom_navigation);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.r_view_all_orders);
        // Set LayoutManager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve orders from database
        try {
            orderDetailDao = AppDB.getInstance(this).orderDetailDao(); // Access database DAO
            if(userType.equals("supplier")){
                supplierDao = AppDB.getInstance(this).supplierDao();
                int sid = supplierDao.getSupplierIDByUid(uid);
                allOrders = orderDetailDao.getOrdersBySupplierId(sid);
                btnNav.setVisibility(View.GONE);

            }else{
                allOrders = orderDetailDao.getAllOrders(); // Get all order details
            }
            // Set adapter for RecyclerView to display orders
            OrderDetailRVAdaptor adapter = new OrderDetailRVAdaptor(allOrders, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving orders: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btnDashboard.setOnClickListener(view -> {
            try {
                Toast.makeText(AdminAllOrders.this, "Dashboard clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAllOrders.this, AdminDashboard.class);
                intent.putExtra("USERTYPE", userType);
                intent.putExtra("USERID", uid);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(AdminAllOrders.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSupplier.setOnClickListener(view -> {
            try {
                Toast.makeText(AdminAllOrders.this, "Supplier clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAllOrders.this, AdminAllSuppliers.class);
                intent.putExtra("USERTYPE", userType);
                intent.putExtra("USERID", uid);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(AdminAllOrders.this, "Error navigating to All Suppliers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnOrder.setOnClickListener(view -> {
            try {
                Toast.makeText(AdminAllOrders.this, "All Orders clicked!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(AdminAllOrders.this, "Error navigating to All Orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnCategory.setOnClickListener(view -> {
            try {
                Toast.makeText(AdminAllOrders.this, "Categories clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAllOrders.this, Category.class);
                intent.putExtra("USERTYPE", userType);
                intent.putExtra("USERID", uid);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(AdminAllOrders.this, "Error navigating to Categories: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(view -> {
            clearPref(view); // Clear preferences on logout
            try {
                // Navigate back to the SignIn page after logout
                Intent intent = new Intent(AdminAllOrders.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
                startActivity(intent);
                // Finish current activity so user can't go back to it
                finish();
            } catch (Exception e) {
                Toast.makeText(AdminAllOrders.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear SharedPreferences
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
