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

public class SupplierDashboard extends AppCompatActivity {
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    LinearLayout btnDashboard, btnProduct,btnOrder,btnProfile;
    String userType;
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnDashboard = findViewById(R.id.nav_dashboard);
        btnProduct = findViewById(R.id.nav_product);
        btnOrder = findViewById(R.id.nav_order);
        btnProfile = findViewById(R.id.nav_profile);

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
            userType = "supplier";
            // Log userType and uid
            Log.d("SupplierDashboard", "UserType: " + userType);
            Log.d("SupplierDashboard", "UserID: " + uid);
        }catch (Exception e) {
            Toast.makeText(this, "Error getting user information " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SupplierDashboard.this, "Dashboard clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(SupplierDashboard.this, "Order clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SupplierDashboard.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(SupplierDashboard.this, "Error navigating to All Orders  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(SupplierDashboard.this, "Order clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SupplierDashboard.this, AdminAllOrders.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(SupplierDashboard.this, "Error navigating to All Orders  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Intent intent= new Intent(SupplierDashboard.this, SupplierDetail.class);
                        intent.putExtra("USERID", uid);
                        intent.putExtra("USERTYPE", userType);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SupplierDashboard.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(SupplierDashboard.this, "Error navigating to Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}