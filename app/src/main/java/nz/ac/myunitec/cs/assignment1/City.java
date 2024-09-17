package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class City extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView btnAdd;
    int uid;
    String userType;
    LinearLayout btnHome,btnCart,btnCat,btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //SET + CITY BUTTON VISIBLE
        btnAdd = findViewById(R.id.btn_open_add_city);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getInt("USERID",0);
            userType = extras.getString("USERTYPE", "");
        } else {
            userType = "";
        }

        if(userType.equals("admin")){
            btnAdd.setVisibility(View.VISIBLE);
        }else{
            btnAdd.setVisibility(View.INVISIBLE);
        }

        recyclerView = findViewById(R.id.r_view_city);
        recyclerView.setHasFixedSize(true);
        // 使用 GridLayoutManager 顯示 2 列網格
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //CityData
        CityData[] cityData = new CityData[]{
                new CityData("Auckland", R.drawable.pic_auckland),
                new CityData("Wanaka", R.drawable.pic_wanaka),
                new CityData("Christchurch", R.drawable.pic_christchurch),
                new CityData("Queenstown", R.drawable.pic_queenstown),
                new CityData("Wellington", R.drawable.pic_wellingtion),
                new CityData("Rotorua", R.drawable.pic_rotorua)
        };
        CityRVAdapter adapter = new CityRVAdapter(cityData, City.this,uid,userType);
        recyclerView.setAdapter(adapter);

        btnHome = findViewById(R.id.nav_home);
        btnCart = findViewById(R.id.nav_cart);
        btnCat = findViewById(R.id.nav_cat);
        btnProfile = findViewById(R.id.nav_profile);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(City.this, MainActivity.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(City.this, "Error opening Home Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Toast.makeText(City.this, "Cart feature coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(City.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(City.this, "Error opening Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(City.this, Category.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(City.this, "Error opening Category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                intent = new Intent(City.this, CustomerProfile.class);
                                break;
                            case "admin":
                                intent = new Intent(City.this, AdminDashboard.class);
                                break;
                            case "supplier":
                                intent = new Intent(City.this, SupplierDashboard.class);
                                break;
                            default:
                                Toast.makeText(City.this, "Unknown user type, please login again.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(City.this, SignIn.class);
                                break;
                        }
                        intent.putExtra("USERTYPE", userType);
                        intent.putExtra("USERID", uid);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(City.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(City.this, "Error navigating to Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}