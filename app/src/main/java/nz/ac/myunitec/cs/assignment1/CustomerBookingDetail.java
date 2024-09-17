package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.OrderDetail;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;

public class CustomerBookingDetail extends AppCompatActivity {
    OrderDetailDao orderDetailDao;
    int oid;
    ImageView btnBack;
    TextView name, book_id,supplier_id,expiryDate,orderDate,qty,price,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_booking_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        name = findViewById(R.id.booking_detail_name);
        book_id = findViewById(R.id.tv_booking_detail_book_id);
        supplier_id = findViewById(R.id.booking_detail_supplier_num);
        expiryDate = findViewById(R.id.booking_detail_expiry_date);
        orderDate  = findViewById(R.id.booking_detail_order_date);
        qty = findViewById(R.id.booking_detail_order_qty);
        price = findViewById(R.id.booking_detail_order_price);
        total = findViewById(R.id.booking_detail_order_total);
        btnBack = findViewById(R.id.back_button);
        // Initialize the DAO
        try {
            orderDetailDao = AppDB.getInstance(this).orderDetailDao();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            // Get order ID from Intent extras
            Bundle extras = getIntent().getExtras();
            oid = extras.getInt("ORDERID", 0);
            OrderDetail orderDetail = orderDetailDao.getOrder(oid);

            if (orderDetail == null) {
                Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            name.setText(orderDetail.getName());
            book_id.setText(String.valueOf(orderDetail.getOid()));
            supplier_id.setText(String.valueOf(orderDetail.getSid()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = dateFormat.format(orderDetail.getPurchaseDate());
            orderDate.setText(dateString);
            String dateString2 = dateFormat.format(orderDetail.getExpiryDate());
            expiryDate.setText(dateString2);
            qty.setText(String.valueOf(orderDetail.getQty()));
            price.setText(String.format(Locale.getDefault(), "$%.2f", orderDetail.getPrice()));
            total.setText(String.format(Locale.getDefault(), "$%.2f", orderDetail.getTotal()));

        } catch (NullPointerException e) {
            // Handle exception if user ID could not be retrieved
            Toast.makeText(this, "Failed to retrieve order information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CustomerBookingDetail.this, CustomerProfile.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            // Catch any other exceptions that might occur
            Toast.makeText(this, "An unexpected error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}