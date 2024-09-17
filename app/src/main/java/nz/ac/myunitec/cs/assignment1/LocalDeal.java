package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Product;
import nz.ac.myunitec.cs.assignment1.db.ProductDao;

public class LocalDeal extends AppCompatActivity {
    String city,userType;
    int uid;
    ProductDao productDao;
    ImageView btnBack, imgCity;
    TextView name;
    RecyclerView productRecyclerView;
    ProductRVAdapter productRVAdapter;
    LinearLayout btnHotel, btnTour, btnDeals, btnTicket, btnRestaurant;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_local_deal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.local_deal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Product DAO
        productDao = AppDB.getInstance(this).productDao();

        name = findViewById(R.id.title_city);
        imgCity = findViewById(R.id.image_city);
        btnBack = findViewById(R.id.btn_back);

        // Get city name and image from intent extras
        try {
            Bundle extras = getIntent().getExtras();
            city = extras.getString("CITY_NAME", "");  // 读取传递的城市名称
            name.setText(city);
            imgCity.setImageResource(extras.getInt("CITY_IMAGE", 0));

        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving city information: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Retrieve USERID and USERTYPE
        try {
            Bundle extras = getIntent().getExtras();
            uid = extras.getInt("USERID", 0);
            userType = extras.getString("USERTYPE", "");
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving user information: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Initially load all products for the specified city
        try {
            productList = productDao.getAllProductsByCity(city);
        } catch (Exception e) {
            Toast.makeText(LocalDeal.this, "Error loading products for city: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("LocalDeal", "Error loading products", e);
        }

        // Set up RecyclerView
        productRecyclerView = findViewById(R.id.r_view_product);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRVAdapter = new ProductRVAdapter(productList, this,uid,userType);
        productRecyclerView.setAdapter(productRVAdapter);

        // Back button
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(LocalDeal.this, MainActivity.class);
            startActivity(intent);
        });

        // Filter by categories when a button is clicked
        btnTour = findViewById(R.id.cat_tour);
        btnHotel = findViewById(R.id.cat_hotel);
        btnDeals = findViewById(R.id.cat_deals);
        btnTicket = findViewById(R.id.cat_tickets);
        btnRestaurant = findViewById(R.id.cat_restaurant);

        // Category filters
        btnTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList = productDao.getAllProductsByCityAndCategory(city,"Tours");
                productRVAdapter.setFilteredList(productList);
                productRVAdapter.notifyDataSetChanged();
            }
        });
        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList = productDao.getAllProductsByCityAndCategory(city,"Hotels");
                productRVAdapter.setFilteredList(productList);
                productRVAdapter.notifyDataSetChanged();
            }
        });

        btnDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList = productDao.getAllProductsByCity(city);
                productRVAdapter.setFilteredList(productList);
                productRVAdapter.notifyDataSetChanged();
            }
        });

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList = productDao.getAllProductsByCityAndCategory(city,"Tickets");
                productRVAdapter.setFilteredList(productList);
                productRVAdapter.notifyDataSetChanged();
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList = productDao.getAllProductsByCityAndCategory(city,"Restaurant");
                productRVAdapter.setFilteredList(productList);
                productRVAdapter.notifyDataSetChanged();
            }
        });
    }
}
