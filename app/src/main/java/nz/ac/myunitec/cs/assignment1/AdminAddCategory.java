package nz.ac.myunitec.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminAddCategory extends AppCompatActivity {
    String userType;
    int uid;
    ImageView btnBack,btnAddPic;
    EditText catName;
    TextView btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Get USERID and USERTYPE from Intent extras
        try {
            Bundle extras = getIntent().getExtras();
            uid = extras.getInt("USERID", 0);
            userType = extras.getString("USERTYPE", "");
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving user information: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Retrieve UI components
        btnBack = findViewById(R.id.back_button);
        btnAddPic = findViewById(R.id.btn_add_cat_pic);
        catName = findViewById(R.id.et_change_name);
        btnAdd = findViewById(R.id.btn_save_changes);

        // Back button event listener
        btnBack.setOnClickListener(view -> {
            try {
                // Navigate back to the previous activity (Admin Dashboard or Category list)
                onBackPressed();
            } catch (Exception e) {
                Toast.makeText(AdminAddCategory.this, "Error navigating back: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Add category picture button event listener
        btnAddPic.setOnClickListener(view -> {
            try {
                // Handle the action to add or change the category picture
                Toast.makeText(AdminAddCategory.this, "Add Category Picture clicked, New Feature coming soon! ", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(AdminAddCategory.this, "Error adding picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Add category button event listener
        btnAdd.setOnClickListener(view -> {
            try {
                // Handle the action to add a new category
                String categoryName = catName.getText().toString().trim();

                if (!categoryName.isEmpty()) {

                    Toast.makeText(AdminAddCategory.this, "Category " + categoryName + " added successfully", Toast.LENGTH_SHORT).show();

                    // Navigate back to the Category screen or Admin Dashboard after adding
                    Intent intent = new Intent(AdminAddCategory.this, Category.class);
                    intent.putExtra("USERID", uid);
                    intent.putExtra("USERTYPE", userType);
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminAddCategory.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(AdminAddCategory.this, "Error adding category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}