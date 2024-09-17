package nz.ac.myunitec.cs.assignment1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Category extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView btnAdd;
    String userType;
    int uid;
    LinearLayout btnHome,btnCities,btnCart,btnProfile,nav;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //SET + CITY BUTTON VISIBLE
        btnAdd = findViewById(R.id.btn_add_cat);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getInt("USERID",0);
            userType = extras.getString("USERTYPE", "");
        } else {
            userType = "";
        }
        nav = findViewById(R.id.bottom_navigation);
        if (userType.equals("admin")) {
            nav.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.INVISIBLE);
        }

        recyclerView = findViewById(R.id.r_view_category);
        recyclerView.setHasFixedSize(true);
        // 使用 GridLayoutManager 顯示 2 列網格
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //CategoryData
        CategoryData [] categoryData = new CategoryData[]{
                new CategoryData("Tours",R.drawable.icn_tours),
                new CategoryData("Hotels",R.drawable.icn_accommodation),
                new CategoryData("Deals",R.drawable.pic_discount),
                new CategoryData("Tickets",R.drawable.icn_ticket),
                new CategoryData("Restaurant",R.drawable.icn_restaurant)
        };
        CategoryRVAdapter adapter = new CategoryRVAdapter(categoryData, Category.this,uid,userType);
        recyclerView.setAdapter(adapter);

        btnHome = findViewById(R.id.nav_home);
        btnCities = findViewById(R.id.nav_cities);
        btnCart = findViewById(R.id.nav_cart);
        btnProfile = findViewById(R.id.nav_profile);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Category.this, AdminAddCategory.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(Category.this, "Error opening Home Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Category.this, MainActivity.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(Category.this, "Error opening Home Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Category.this, City.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(Category.this, "Error opening City Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Toast.makeText(Category.this, "Cart feature coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Category.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(Category.this, "Error opening Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Intent intent;
                        switch (userType) {
                            case "customer":
                                intent = new Intent(Category.this, CustomerProfile.class);
                                break;
                            case "admin":
                                intent = new Intent(Category.this, AdminDashboard.class);
                                break;
                            case "supplier":
                                intent = new Intent(Category.this, SupplierDashboard.class);
                                break;
                            default:
                                Toast.makeText(Category.this, "Unknown user type, please login again.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(Category.this, SignIn.class);
                                break;
                        }
                        intent.putExtra("USERTYPE", userType);
                        intent.putExtra("USERID", uid);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Category.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(Category.this, "Error navigating to Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}