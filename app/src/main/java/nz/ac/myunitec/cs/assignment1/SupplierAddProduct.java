package nz.ac.myunitec.cs.assignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SupplierAddProduct extends AppCompatActivity {
    TextView btnAdd;
    ImageView btnBack, btnAddImg;
    Spinner spRegion, spCat;
    EditText etProductName, etOriginalPrice, etDiscountPercentage, etDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnBack = findViewById(R.id.back);
        btnAdd = findViewById(R.id.btn_add_product);
        btnAddImg = findViewById(R.id.btn_add_product_pic);
        spRegion = findViewById(R.id.sp_new_product_region);
        spCat = findViewById(R.id.sp_new_product_category);
        etProductName = findViewById(R.id.et_new_product_name);
        etOriginalPrice = findViewById(R.id.et_new_product_original_price);
        etDiscountPercentage = findViewById(R.id.et_new_product_discount_percentage);
        etDescription = findViewById(R.id.et_new_product_description);

        // Set up region and category spinners
        List<String> regionList = new ArrayList<>();
        regionList.add("Auckland");
        regionList.add("Wellington");
        regionList.add("Christchurch");
        regionList.add("Queenstown");
        regionList.add("Wanaka");
        regionList.add("Rotorua");

        List<String> categoryList = new ArrayList<>();
        categoryList.add("Tours");
        categoryList.add("Hotels");
        categoryList.add("Restaurant");
        categoryList.add("Tickets");

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regionList);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRegion.setAdapter(regionAdapter);
        spCat.setAdapter(categoryAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the data from EditText fields
                String productName = etProductName.getText().toString().trim();
                String originalPriceStr = etOriginalPrice.getText().toString().trim();
                String discountPercentageStr = etDiscountPercentage.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                // Read the selected region and category from Spinner
                String selectedRegion = spRegion.getSelectedItem().toString();
                String selectedCategory = spCat.getSelectedItem().toString();

                // Validate inputs and show the entered data
                if (productName.isEmpty() || originalPriceStr.isEmpty() || discountPercentageStr.isEmpty() || description.isEmpty()) {
                    Toast.makeText(SupplierAddProduct.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Convert originalPrice and discountPercentage to double
                        double originalPrice = Double.parseDouble(originalPriceStr);
                        double discountPercentage = Double.parseDouble(discountPercentageStr);

                        // Process the data or show a Toast with the entered information
                        Toast.makeText(SupplierAddProduct.this,
                                "Product: " + productName + "\n" +
                                        "Price: $" + originalPrice + "\n" +
                                        "Discount: " + discountPercentage + "%\n" +
                                        "Category: " + selectedCategory + "\n" +
                                        "Region: " + selectedRegion + "\n" +
                                        "Description: " + description,
                                Toast.LENGTH_LONG).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(SupplierAddProduct.this, "Please enter valid numeric values for price and discount", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Handle back button action
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onBackPressed();
                } catch (Exception e) {
                    Toast.makeText(SupplierAddProduct.this, "Error navigating back: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle image adding action
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SupplierAddProduct.this, "Add image clicked, new feature coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}