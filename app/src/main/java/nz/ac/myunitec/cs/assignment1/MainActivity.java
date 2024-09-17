package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import nz.ac.myunitec.cs.assignment1.db.Admin;
import nz.ac.myunitec.cs.assignment1.db.AdminDao;
import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Customer;
import nz.ac.myunitec.cs.assignment1.db.CustomerDao;
import nz.ac.myunitec.cs.assignment1.db.OrderDetail;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;
import nz.ac.myunitec.cs.assignment1.db.Product;
import nz.ac.myunitec.cs.assignment1.db.ProductDao;
import nz.ac.myunitec.cs.assignment1.db.Supplier;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;
import nz.ac.myunitec.cs.assignment1.db.User;
import nz.ac.myunitec.cs.assignment1.db.UserDao;

public class MainActivity extends AppCompatActivity {
    static final String SHARED_PREF = "PrefDemo";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    UserDao userDao;
    CustomerDao customerDao;
    AdminDao adminDao;
    SupplierDao supplierDao;
    OrderDetailDao orderDetailDao;
    ProductDao productDao;
    RecyclerView productRecyclerView;
    SearchView searchView;
    String userType;
    int uid;
    List<Product> productList;
    ProductRVAdapter productRVAdapter;
    TextView btnMoreDetail;
    LinearLayout btnHome,btnCities,btnCart,btnCat,btnProfile,btnHotel,btnTour,btnDeals,btnTicket,btnRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = AppDB.getInstance(this).userDao();
        customerDao = AppDB.getInstance(this).customerDao();
        adminDao = AppDB.getInstance(this).adminDao();
        supplierDao = AppDB.getInstance(this).supplierDao();
        productDao = AppDB.getInstance(this).productDao();
        orderDetailDao = AppDB.getInstance(this).orderDetailDao();

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });


        try {
            // Initialize SharedPreferences and Editor
            sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

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

            // Try to get userType using the uid from the database
            userType = userDao.getUserType(uid);
            //Log.e("MainActivity", "User Type:  " + userType);

        } catch (Exception e) {
            // Log the error in case of an exception and show a Toast message
            Log.e("MainActivity", "Error initializing SharedPreferences or getting userType: " + e.getMessage(), e);
            Toast.makeText(this, "Error retrieving user information.", Toast.LENGTH_SHORT).show();
        }

        //Please Enter data before run the application, and remove it before opening other views
        //dummyData();

        productList = productDao.getAllProducts();
        //Log.d("AllProduct", "All Products Size: " + productList.size());

        productRecyclerView = findViewById(R.id.r_view_product);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRVAdapter = new ProductRVAdapter(productList,this,uid,userType);
        productRecyclerView.setAdapter(productRVAdapter);

        btnHome = findViewById(R.id.nav_home);
        btnCities = findViewById(R.id.nav_cities);
        btnCart = findViewById(R.id.nav_cart);
        btnCat = findViewById(R.id.nav_cat);
        btnProfile = findViewById(R.id.nav_profile);
        btnTour = findViewById(R.id.cat_tour);
        btnHotel = findViewById(R.id.cat_hotel);
        btnDeals = findViewById(R.id.cat_deals);
        btnTicket= findViewById(R.id.cat_tickets);
        btnRestaurant = findViewById(R.id.cat_restaurant);
        btnMoreDetail = findViewById(R.id.tv_more_deals);

        btnTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "Tour clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "Tours");
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });
        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "Hotel clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "Hotels");
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });
        btnMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "More Details clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "");
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });
        btnDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "Deal clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "");
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });
        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "Ticket clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "Tickets");
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(MainActivity.this, "Restaurant clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AllProduct.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("CATEGORY", "Restaurant");
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error filtering product list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error filtering products", e);
                }
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Home navigating clicked", Toast.LENGTH_SHORT).show();
            }
        });
        btnCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivity.this, City.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error navigating to Cities: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (uid>0) {
                        Toast.makeText(MainActivity.this, "Cart feature coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error opening Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivity.this, Category.class);
                    intent.putExtra("USERTYPE", userType);
                    intent.putExtra("USERID", uid);
                    startActivity(intent);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error opening Category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                intent = new Intent(MainActivity.this, CustomerProfile.class);
                                break;
                            case "admin":
                                intent = new Intent(MainActivity.this, AdminDashboard.class);
                                break;
                            case "supplier":
                                intent = new Intent(MainActivity.this, SupplierDashboard.class);
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Unknown user type, please login again.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MainActivity.this, SignIn.class);
                                break;
                        }
                        intent.putExtra("USERID", uid);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, SignIn.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error navigating to Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to filter product list based on search query
    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        // Use global productList to avoid re-querying the database
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(text.toLowerCase()) ||
                    product.getRegion().toLowerCase().contains(text.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }

        // Update RecyclerView with the filtered list
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
        } else {
            productRVAdapter.setFilteredList(filteredList);
        }
    }

    private void dummyData(){

        User newUser = new User("customer", "user@gmail.com", "1234");
        userDao.insertUser(newUser);
        User newUser2 = new User("admin", "admin@gmail", "1234");
        userDao.insertUser(newUser2);
        User newUser3 = new User("supplier", "supplier@gmail.com", "1234");
        userDao.insertUser(newUser3);
        User newUser4 = new User("supplier", "supplier2@gmail.com", "1234");
        userDao.insertUser(newUser4);
        User newUser5 = new User("supplier", "supplier3@gmail.com", "1234");
        userDao.insertUser(newUser5);
        User newUser6 = new User("supplier", "supplier4@gmail.com", "1234");
        userDao.insertUser(newUser6);

        int uid1 = userDao.getUserIdByEmail("user@gmail.com");
        Customer customer = new Customer("customer","user@gmail.com","1234","Joy",R.drawable.pic_profile,null,uid1);
        customerDao.insertCustomer(customer);
        int uid2 = userDao.getUserIdByEmail("admin@gmail");
        Admin admin = new Admin("admin",userDao.getEmail(uid2),userDao.getPassword(uid2),"Admin",uid2);
        adminDao.insertAdmin(admin);
        int uid3 = userDao.getUserIdByEmail("supplier@gmail.com");
        Supplier supplier = new Supplier("supplier",userDao.getEmail(uid3),userDao.getPassword(uid3),"SkyCity Auckland Ltd","Jone Cena",null,R.drawable.pic_skycity_logo,uid3);
        supplierDao.insertSupplier(supplier);
        int uid4 = userDao.getUserIdByEmail("supplier2@gmail.com");
        Supplier supplier2 = new Supplier("supplier",userDao.getEmail(uid4),userDao.getPassword(uid4),"AJ Hackett International Ltd","James Hackett",null,R.drawable.pic_ajlogo, uid4);
        supplierDao.insertSupplier(supplier2);
        int uid5 = userDao.getUserIdByEmail("supplier3@gmail.com");
        Supplier supplier3 = new Supplier("supplier",userDao.getEmail(uid5),userDao.getPassword(uid5),"Rainbow's End","Rainbow Ben bow",null,R.drawable.pic_rainbow_logo, uid5);
        supplierDao.insertSupplier(supplier3);
        int uid6 = userDao.getUserIdByEmail("supplier4@gmail.com");
        Supplier supplier4 = new Supplier("supplier",userDao.getEmail(uid6),userDao.getPassword(uid6),"Vector Wero","Vic",null,R.drawable.pic_vectorwero_, uid6);
        supplierDao.insertSupplier(supplier4);

        //Product 1
        Product product1 = new Product(59,33,"Auckland","Tickets","Sky Tower, a magnificent 328-meter-high structure, has been the focal point of Auckland for over 25 years, delighting both locals and visitors. Recently awarded the #1 Attraction in Auckland, it offers incredible 360° views of Tāmaki Makaurau, encompassing ancient volcanoes, sparkling sunlit harbors, and beyond. Discover the full story of this modern engineering marvel, from its pioneering past to its colorful present and visionary future. Now, it’s your turn to experience it.","Sky Tower Ticket",R.drawable.pic_skycity,1);
        productDao.insertProduct(product1);
        //Product 2
        Product product2 = new Product(160,26,"Auckland","Tickets","Experience the best white water rafting Auckland has to offer at Vector Wero – two expertly designed courses built to excite and delight, right here on your doorstep! Ease into the experience with a jaunt down the mild-but-mighty Tamariki River. Race down the heart-pounding River Rush, and for the ultimate thrill finish it all off by taking a spin down the world’s highest man-made waterfall.","White Water Rafting",R.drawable.pic_rafting,4);
        productDao.insertProduct(product2);
        //Product 3
        Product product3 = new Product(80,39,"Auckland", "Tickets","With over 20 rides and attractions, it's full on fun for all ages at Rainbow's End! Whether you're on the hunt for pure adrenaline on The Big Five, or magical moments on the classic Enchanted Forest Log Flume, fans of thrilling fun will be right at home at Rainbow's End. Come see us soon!","Rainbow's End Day Pass",R.drawable.pic_rainbowsend,3);
        productDao.insertProduct(product3);
        //Product 4
        Product product4 = new Product(330,50,"Auckland", "Tickets","t’s the ultimate leap of faith – a jump from NZ’s highest building with nothing but a wire between you and the ground 192m down! SkyJump gives you 11 seconds of pure adrenaline as you plunge 53 floors down at a speed of 85kph. You’ll feel like a total boss when you stick the landing in Sky City Plaza.","Auckland Sky Jump",R.drawable.pic_skyjump,2);
        productDao.insertProduct(product4);
        //Product 5
        Product product5 = new Product(80,39,"Auckland", "Tickets","Take a walk on the wild side at the top of New Zealand’s highest building. With no handrails to separate you from the 192m drop, you’ll be doing some adrenaline-fuelled challenges designed to get your heart racing while you take in those epic 360-degree views of the City of Sails.","Auckland SkyWalk",R.drawable.pic_skywalk,2);
        productDao.insertProduct(product5);
        //Product 6 NEVIS BUNGY AND KAWARAU BUNGY COMBO, Queenstown,Tickets
        Product product6 = new Product(615,20,"Queenstown", "Tickets","The Kawarau Bridge Bungy and Nevis Bungy combine for a day in Queenstown you’re never going to forget. This is the World Home of Bungy, followed by the 134m insanity of the Nevis Bungy. These are two of the world’s most popular Jumps, and we’re making it super cheap and easy to conquer both. For info on each activity, check out the Nevis Bungy and Kawarau Bridge Bungy.","Nevis Bungy and Kawarau Bungy Combo",R.drawable.pic_ajcombo,2);
        productDao.insertProduct(product6);
        //Product 7 The Grill,Auckland,Restaurant
        Product product7 = new Product(199,50,"Auckland","Restaurant","The Grill serves exceptional New Zealand steak and seafood with a classic, modern approach. The Grill. It’s not only the food that will showcase the best of New Zealand. A creative cocktail menu has been created with a curious approach, with each cocktail a nod to New Zealand’s rich and diverse landscapes. The wine has been handpicked from local vineyards to complement dishes and complete the full dining experience. Of course, International classics will be available, on request.Relax in the hearth, complete with fireplace, or the outdoor courtyard with a water feature and Sky Tower views.","The Grill 3-Course Dinner or Lunch Deal",R.drawable.pic_the_grill,1);
        productDao.insertProduct(product7);
        //Product 8 Orbit,Auckland,Restaurant
        Product product8 = new Product(79,15,"Auckland","Restaurant","Soaring high above Auckland you'll discover Orbit, a truly iconic New Zealand brasserie offering diners sensational views - and food to match. Situated at the top of Auckland’s Sky Tower, the dining room rotates once every hour, providing a truly unique experience with amazing 360-degree panoramas of the city, the Hauraki Gulf and beyond. Orbit offers a modern set-menu dining experience in a relaxed, open atmosphere with a delicious kiwi-inspired menu that features the best local and seasonal produce. As New Zealand’s only rotating restaurant offering 360-degree views, it’s an unforgettable experience that never ceases to delight.","Orbit 360 High Tea Experience",R.drawable.pic_orbit,1);
        productDao.insertProduct(product8);
        //Product 9 SkyCity Hotel,Auckland,Hotels
        Product product9 = new Product(399,10,"Auckland","Hotels","Escape to a hotel where exceptional experiences happen every day. A place where you can enjoy genuine Kiwi hospitality and modern, spacious accommodation.Explore the sights and sounds of vibrant Auckland, including the iconic Sky Tower. Pamper yourself at rejuvenating spa facilities. Experience magnificent dining with over 20 restaurants, cafes and bars close by, not to mention theaters and the exciting SkyCity Casino. Before you return, relax and refresh with our world-class facilities and services, where your comfort is our priority.","Auckland SkyCity Hotel 2-Night Stay",R.drawable.pic_skycity_hotel_auckland,1);
        productDao.insertProduct(product9);
        //Product 10 SkyCity Hotel,Queenstown,Hotels
        Product product10 = new Product(899,20,"Queenstown","Hotels","Escape to a hotel where exceptional experiences happen every day. A place where you can enjoy genuine Kiwi hospitality and modern, spacious accommodation.Explore the sights and sounds of vibrant Queenstown, including the iconic SkyCity Casino. SkyCity Queenstown Casino is open 11am-1am everyday* so you don’t even need to wait for night to fall before you join in the fun! You'll find the world's most popular casino games, a fantastic bar & kitchen and often you'll find awesome events going on inside!. Before you return, relax and refresh with our world-class facilities and services, where your comfort is our priority.","Queenstown SkyCity Hotel 4-Night Stay",R.drawable.pic_skycity_hotel_queenstown,1);
        productDao.insertProduct(product10);

        //Create fake data 1 not expiry
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.AUGUST, 1);
        Date fakePurchaseDate = cal.getTime();
        cal.set(2025, Calendar.AUGUST, 1);
        Date fakeExpiryDate = cal.getTime();
        //Create fake data 2 expired
        cal.set(2023, Calendar.MAY, 1);
        Date fakePurchaseDate2 = cal.getTime();
        cal.set(2024, Calendar.MAY, 1);
        Date fakeExpiryDate2 = cal.getTime();

        //Create order 1
        OrderDetail orderDetail = new OrderDetail(1,3,3,1,productDao.getDiscountPrice(3),1*productDao.getDiscountPrice(3),productDao.getProductName(3),fakePurchaseDate,fakeExpiryDate);
        orderDetailDao.insertOrder(orderDetail);
        //Create order 2
        OrderDetail orderDetail2 = new OrderDetail(1,1,1,1,productDao.getDiscountPrice(1),1*productDao.getDiscountPrice(1),productDao.getProductName(3),fakePurchaseDate2,fakeExpiryDate2);
        orderDetailDao.insertOrder(orderDetail2);
    }
}