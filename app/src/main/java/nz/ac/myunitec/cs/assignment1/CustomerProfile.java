package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Date;
import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Customer;
import nz.ac.myunitec.cs.assignment1.db.CustomerDao;
import nz.ac.myunitec.cs.assignment1.db.OrderDetail;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;

public class CustomerProfile extends AppCompatActivity {
    int uid, cid;
    String userType;
    CustomerDao customerDao;
    OrderDetailDao orderDetailDao;
    LinearLayout btnHome, btnCities, btnCart, btnCat;
    ImageView img,btnLogout;
    TextView title_name, name, email, mobile, btnEditAccount,btnUpComing,btnCompleted;
    RecyclerView recyclerView;
    List<OrderDetail> customerOrders;
    List<OrderDetail> filteredOrderList;
    Date currentDate;
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize bottom navigation buttons
        btnHome = findViewById(R.id.nav_home);
        btnCities = findViewById(R.id.nav_cities);
        btnCart = findViewById(R.id.nav_cart);
        btnCat = findViewById(R.id.nav_cat);
        btnEditAccount = findViewById(R.id.tv_edit_account);
        btnLogout= findViewById(R.id.btn_logout);
        btnUpComing = findViewById(R.id.up_coming);
        btnCompleted = findViewById(R.id.completed);

        // Initialize views for displaying customer information
        img = findViewById(R.id.img);
        title_name = findViewById(R.id.tv_title_customer_name);
        name = findViewById(R.id.tv_customer_name);
        email = findViewById(R.id.tv_customer_email);
        mobile = findViewById(R.id.tv_customer_mobile);

        // 初始化 SharedPreferences 和 Editor
        sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        editor = sharedPref.edit();

        //get userType and uid from share preference
        userType = sharedPref.getString("USERTYPE","");
        uid = sharedPref.getInt("USERID",0);

        //if user didn't save it in share preference
        //get it from Intent extra
        if (uid == 0) {
            // 如果 SharedPreferences 中没有 USERID，尝试从 Intent Extras 获取
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                uid = extras.getInt("USERID", 0); // 从 Intent 中获取 USERID
            }
        }

        // If user ID is 0, show a message and redirect to Sign In
        if (uid == 0) {
            Toast.makeText(this, "Customer not found, please log in again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CustomerProfile.this, SignIn.class);
            startActivity(intent);
            finish();
        }

        userType="customer";

        try {
            // Retrieve order detail from the database
            orderDetailDao = AppDB.getInstance(this).orderDetailDao();
            // Retrieve customer information from the database
            customerDao = AppDB.getInstance(this).customerDao();
            Customer customer = customerDao.getCustomer(uid);
            cid = customer.getCid(); // Get Customer ID

            if (cid == 0) {
                // Handle the case where customer ID could not be found
                Toast.makeText(this, "Error retrieving customer data: ", Toast.LENGTH_SHORT).show();
            }

            // Initialize RecyclerViews
            recyclerView = findViewById(R.id.r_view);
            // Set up LayoutManager for the RecyclerViews
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Get current date to categorize orders as upcoming or completed
            customerOrders = new ArrayList<>();
            filteredOrderList = new ArrayList<>();
            currentDate = new Date();

            // Retrieve customer's orders
            customerOrders = orderDetailDao.getOrdersByCustomerId(cid);

            // 默认显示 "Upcoming" 的订单
            filterUpcomingOrders();

            // Set adapters for the RecyclerViews to display
            OrderDetailRVAdaptor adapter= new OrderDetailRVAdaptor(filteredOrderList, this);
            recyclerView.setAdapter(adapter);


            // Define Button Click Listeners
            btnUpComing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterUpcomingOrders();
                    adapter.notifyDataSetChanged();
                    // Set underline for "Upcoming"
                    btnUpComing.setPaintFlags(btnUpComing.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    // Remove underline for "Completed"
                    btnCompleted.setPaintFlags(btnCompleted.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                }
            });

            btnCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterCompletedOrders();
                    adapter.notifyDataSetChanged();
                    // Set underline for "Completed"
                    btnCompleted.setPaintFlags(btnCompleted.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    // Remove underline for "Upcoming"
                    btnUpComing.setPaintFlags(btnUpComing.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                }
            });

            // Display customer details (profile picture, name, email, mobile number)
            if (customer.getC_img() != null) {
                img.setImageResource(customer.getC_img()); // Set customer's profile picture if available
            } else {
                img.setImageResource(R.drawable.pic_profile); // Set default profile picture if not available
            }
            title_name.setText(customer.getName());
            name.setText(customer.getName());
            email.setText(customer.getEmail());
            if (customer.getMobile_num() != null) {
                mobile.setText(customer.getMobile_num());
            } else {
                mobile.setText("null");
            }

        } catch (Exception e) {
            // Handle any exceptions that occur while retrieving customer data or orders
            Toast.makeText(this, "Error retrieving customer data or orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPref(view);
                try {
                    // Navigate back to the SignIn page after logout
                    Intent intent = new Intent(CustomerProfile.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
                    startActivity(intent);
                    // Finish current activity so user can't go back to it
                    finish();
                } catch (Exception e) {
                    // Handle exception if there is an error with logout
                    Toast.makeText(CustomerProfile.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Home button
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(CustomerProfile.this, MainActivity.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(CustomerProfile.this, "Error opening Home Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Cities button
        btnCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Navigate to Cities screen
                    Intent intent = new Intent(CustomerProfile.this, City.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                } catch (Exception e) {
                    // Handle exception if navigation to Cities fails
                    Toast.makeText(CustomerProfile.this, "Error navigating to Cities: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Cart button
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Toast.makeText(CustomerProfile.this, "Cart feature coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(CustomerProfile.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerProfile.this, "Error opening Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Category button
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Navigate to Category screen
                    Intent intent = new Intent(CustomerProfile.this, Category.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                } catch (Exception e) {
                    // Handle exception if navigation to Category fails
                    Toast.makeText(CustomerProfile.this, "Error navigating to Category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for Edit Account button
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Navigate to Edit Customer Profile screen
                    Intent intent = new Intent(CustomerProfile.this, CustomerDetail.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                } catch (Exception e) {
                    // Handle exception if navigation to Edit Customer Profile fails
                    Toast.makeText(CustomerProfile.this, "Error navigating to Edit Customer Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Filter upcoming orders (including today)
    private void filterUpcomingOrders() {
        filteredOrderList.clear();
        for (OrderDetail order : customerOrders) {
            // If order is upcoming (expiry date is after current date or today)
            if (order.getExpiryDate().after(currentDate) || order.getExpiryDate().compareTo(currentDate) == 0) {
                filteredOrderList.add(order);
            }
        }
    }

    //Filter completed orders
    private void filterCompletedOrders() {
        filteredOrderList.clear();
        for (OrderDetail order : customerOrders) {
            if (order.getExpiryDate().before(currentDate)) {
                filteredOrderList.add(order);
            }
        }
    }
}
