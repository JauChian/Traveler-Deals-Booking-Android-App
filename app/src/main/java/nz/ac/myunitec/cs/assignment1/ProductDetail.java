package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Product;
import nz.ac.myunitec.cs.assignment1.db.ProductDao;

public class ProductDetail extends AppCompatActivity {
    int pid,uid;
    String userType;
    ProductDao productDao;
    ImageView btnBack, btnSave, btnShare, btnAddToCart, btnMinus, btnAdd, imgProduct;
    TextView discount, region, category, name, description, price, discountPrice, qty, total, totalSave, btnBookNow;
    int quantity = 1; // Initialize quantity to 1 by default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Apply insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btnBack = findViewById(R.id.back);
        btnSave = findViewById(R.id.save);
        btnShare = findViewById(R.id.share);
        btnAddToCart = findViewById(R.id.add_to_cart);
        btnMinus = findViewById(R.id.btn_product_detail_minus_one);
        btnAdd = findViewById(R.id.btn_product_detail_add_one);
        imgProduct = findViewById(R.id.img_product_detail);
        btnBookNow = findViewById(R.id.btn_product_detail_book_now);

        discount = findViewById(R.id.tv_product_detail_discount);
        region = findViewById(R.id.tv_product_detail_region);
        category = findViewById(R.id.tv_product_detail_cat);
        name = findViewById(R.id.t_product_detail_product_name);
        description = findViewById(R.id.tv_product_detail_description_detail);
        price = findViewById(R.id.tv_product_detail_original_price);
        discountPrice = findViewById(R.id.tv_product_detail_price);
        qty = findViewById(R.id.btn_product_detail_qty);
        total = findViewById(R.id.tv_product_detail_total_price);
        totalSave = findViewById(R.id.tv_product_detail_you_have_saved);

        // Initialize the DAO
        try {
            productDao = AppDB.getInstance(this).productDao();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Retrieve USERID and USERTYPE
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                uid = extras.getInt("USERID", 0);
                userType = extras.getString("USERTYPE", "");

                Log.d("ProductDetail", "Received USERID: " + uid);
                Log.d("ProductDetail", "Received USERTYPE: " + userType);
            } else {
                Log.d("ProductDetail", "No extras found in the intent");
                Toast.makeText(this, "No extras found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("ProductDetail", "Error retrieving user information: " + e.getMessage());
        }

        if (userType.equals("admin")||userType.equals("supplier")){
            btnAdd.setEnabled(false);
            btnMinus.setEnabled(false);
            btnBookNow.setEnabled(false);
        }

        // Get product ID from Intent extras
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                pid = extras.getInt("PRODUCT_ID", 0);
            } else {
                pid = 0; // Set default if not provided
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving product ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Retrieve product details from database
        try {
            Product product = productDao.getProduct(pid);
            if (product == null) {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Populate the product details on the screen
            imgProduct.setImageResource(product.getP_img());
            name.setText(product.getName());
            discount.setText(String.format("%d%% OFF", (int) product.getDiscount()));
            region.setText(product.getRegion());
            category.setText(product.getCategory());
            description.setText(product.getDescription());
            price.setText(String.format("$%.2f", product.getPrice()));
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // Calculate discount price and savings
            double originalPrice = product.getPrice();
            double discountPriceValue = originalPrice * ((100 - product.getDiscount()) / 100.0);
            discountPrice.setText(String.format("$%.2f", discountPriceValue));
            total.setText(String.format("$%.2f", discountPriceValue));
            totalSave.setText(String.format("You have saved $%.2f", originalPrice - discountPriceValue));

            qty.setText(String.valueOf(quantity));

            // Subtract quantity action
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Prevent quantity from being less than 1
                    if (quantity > 1) {
                        quantity--;
                        updateTotalPrice(discountPriceValue, originalPrice);
                    } else {
                        Toast.makeText(ProductDetail.this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Add quantity action
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity++;
                    updateTotalPrice(discountPriceValue, originalPrice);
                }
            });

            // Booking action
            btnBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(ProductDetail.this, "Booking successful!", Toast.LENGTH_SHORT).show();
                        // You can add booking logic here (e.g., storing booking in the database)
                    } catch (Exception e) {
                        Toast.makeText(ProductDetail.this, "Error during booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Back button action to return to the product list
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Added to cart Clicked, new feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Share Clicked, new feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Save Clicked, new feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving product details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to update total price based on quantity
    private void updateTotalPrice(double discountPriceValue, double originalPrice) {
        try {
            qty.setText(String.valueOf(quantity));
            double totalPrice = discountPriceValue * quantity;
            double totalSaved = (originalPrice - discountPriceValue) * quantity;

            total.setText(String.format("$%.2f", totalPrice));
            totalSave.setText(String.format("You have saved $%.2f", totalSaved));
        } catch (Exception e) {
            Toast.makeText(this, "Error calculating total price: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
