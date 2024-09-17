package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Product;
import nz.ac.myunitec.cs.assignment1.db.ProductDao;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;

public class AllProduct extends AppCompatActivity {
    String category, city,userType;
    int uid,sid;
    ProductDao productDao;
    SupplierDao supplierDao;
    RecyclerView recyclerView;
    ProductRVAdapter productRVAdapter;
    List<Product> productList;
    TextView btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try{
            // Initialize DAO
            productDao = AppDB.getInstance(this).productDao();
            supplierDao = AppDB.getInstance(this).supplierDao();

            // Retrieve category and city from the intent extras
            Bundle extras = getIntent().getExtras();
            category = extras.getString("CATEGORY", "");
            city = extras.getString("CITY", "");
            // Retrieve uid and usertype
            uid = extras.getInt("USERID", 0);
            userType = extras.getString("USERTYPE", "");

            btnAdd = findViewById(R.id.btn_add_product);
            //only supplier can see this button
            if(userType.equals("supplier")){
                btnAdd.setVisibility(View.VISIBLE);
            }else{
                btnAdd.setVisibility(View.GONE);
            }

            // Retrieve sid for supplier
            sid =supplierDao.getSupplierIDByUid(uid);

            Log.d("AllProduct", "Did I get category " + category);
            // Fetch product list based on the provided category or city
            if (!category.isEmpty()) {
                // Get products by category
                productList = productDao.getAllProductsByCategory(category);
            } else if (!city.isEmpty()) {
                // Get products by city
                productList = productDao.getAllProductsByCity(city);
            }  else if (sid!=0) {
                // Get products by supplier
                productList = productDao.getAllProductsBySid(sid);
            } else {
                // If no category or city is provided, get all products
                productList = productDao.getAllProducts();
            }

            // Initialize and set up the RecyclerView with the product list
            recyclerView = findViewById(R.id.r_view_all_product);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productRVAdapter = new ProductRVAdapter(productList, this,uid,userType);
            recyclerView.setAdapter(productRVAdapter);

            // If no products found, show a toast message
            if (productList.isEmpty()) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
            }

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent intent = new Intent(AllProduct.this, SupplierAddProduct.class);
                        intent.putExtra("USERTYPE", userType);
                        intent.putExtra("USERID", uid);
                        startActivity(intent);
                    }catch (Exception e) {
                        Toast.makeText(AllProduct.this, "Error opening Add Product Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (NullPointerException e) {
            // Handle cases where the extras or DAO might be null
            Toast.makeText(this, "Failed to retrieve product information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle general exceptions
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
